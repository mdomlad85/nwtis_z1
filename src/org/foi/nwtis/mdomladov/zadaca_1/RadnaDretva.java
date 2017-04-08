/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.zadaca_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.dkermek.konfiguracije.Konfiguracija;
import org.foi.nwtis.mdomladov.zadaca_1.ServerSustava.StanjeServera;

/**
 * Kreiranje konstruktora klase i metode za prijenos potrebnih podataka. 
 * Dretva iz dobivene veze na socketu preuzima tokove za ulazne i izlazne 
 * podatke prema korisniku. 
 * Dretva preuzima podatke koje salje korisnik putem ulaznog toka podataka, 
 * provjerava korektnost komandi iz zahtjeva. N
 * a kraju dretva šalje podatke korisniku putem izlaznog toka podataka. 
 * Za svaku vrstu komande kreira se posebna metoda koja odrađuje njenu funkcionalnost.
 * 
 * @author Marko Domladovac
 */
class RadnaDretva extends KonfDretva {
    
    private final Socket socket;
    
    private final int redniBroj;
    
    private Matcher privateMatcher;

    public RadnaDretva(Konfiguracija konf, Socket socket) {
        super(konf);
        this.socket = socket;
        int uspjesniZahtjevi = ServerSustava.evidencija.getBrojUspjesnihZahtjeva();
        
        if(uspjesniZahtjevi <= 0 || uspjesniZahtjevi > Integer.MAX_VALUE){
            redniBroj = 1;
        } else {
            redniBroj = uspjesniZahtjevi;
        }
        
        setName(String.format("%s_%d", Korisnik.LDAP_USERNAME, redniBroj));
    }
    
    public long getExecutionTime(){
        return System.currentTimeMillis()  - startTime;
    }

    @Override
    public void run() {
        super.run();        
        System.out.println(this.getClass());

        String sintaksaAdmin="^USER +(\\w+); *PASSWD +(\\w+); *(PAUSE|STOP|START|STAT);$";
        String sintaksaKorisnik="^USER ([^\\s]+); *(ADD|TEST|WAIT) +([^\\s]+);$";
        
        ServerSustava.evidencija.setNazivZadnjeDretve(this.getName());
            
        InputStream is = null;
        OutputStream os = null;
        try {
            String response = "OK;";
            is = socket.getInputStream();
            os = socket.getOutputStream();
            StringBuffer sb= new StringBuffer();
            
            while(true){
                int znak= is.read();
                if(znak==-1){
                    break;
                }
                sb.append((char)znak);
            }
            
            System.out.println("Primljena naredba:\t"+sb);

            if(isValid(sintaksaAdmin, sb)){
                if(this.konf.postojiPostavka(KonfiguracijskiParametri.PUTANJA_ADMIN_DATOTEKE)){
                    File file = new File(this.konf.dajPostavku(KonfiguracijskiParametri.PUTANJA_ADMIN_DATOTEKE));
                    if(file.exists()){
                        //TODO: ovo bi bilo dobro raditi s hashevima
                        String autentifikacija = 
                                String.format("%s;%s", privateMatcher.group(1), 
                                        privateMatcher.group(2));
                        
                        FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr);
                        
                        String line;
                        boolean adminExists = false;
			while ((line = br.readLine()) != null) {
				if(line.equals(autentifikacija)){
                                    adminExists = true;
                                    break;
                                }
			}
                        
                        if(adminExists){
                             String evidFilename = this.konf.dajPostavku(KonfiguracijskiParametri.PUTANJA_EVIDENCIJSKE_DATOTEKE);
                             AdministratorServera admin = new AdministratorServera(privateMatcher.group(3), evidFilename);
                             response = admin.aktivirajKomandu();
                        } else {
                            //ne zelimo dati vise informacija nego je potrebno
                            //ne budemo rekli sto je pogresno, nego samo da je pogresno
                            response = "ERROR 00; Korisnicki podaci su neispravni.";
                        }
                        
                    } else {
                        response = "ERROR 90; Putanja za administratore je neispravna.";
                    }
                } else {                    
                    response = "ERROR 90; Ne postoji konfiguracija za administratore.";
                }
            } else if(ServerSustava.stanjeServera == ServerSustava.StanjeServera.PAUSED){
                response = "ERROR 90; Server je pauziran.";
            } else if(isValid(sintaksaKorisnik, sb)){
                String brojRadnihDretvi = this.konf.dajPostavku(KonfiguracijskiParametri.MAX_BROJ_RADNIH_DRETVI);
                KorisnikServera korisnik = new KorisnikServera(privateMatcher.group(2), Integer.parseInt(brojRadnihDretvi));
                response = korisnik.aktivirajKomandu(privateMatcher.group(1), privateMatcher.group(3));
                
                if(korisnik.waitTime != null){
                    try {
                        sleep(korisnik.waitTime * 1000);
                        response = "OK;";
                    } catch (InterruptedException ex) {
                        response = "ERROR 13; Dretva je prekinuta";
                        Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            } else {
                response = "ERROR 90; Neispravna komanda!";
            }
             
            os.write(response.getBytes());
            os.flush();
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(is !=null){
                    is.close();
                }
                if(os !=null){
                    os.close();
                }
               
            } catch (IOException ex) {
                Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(!this.isInterrupted()){
            synchronized(ServerSustava.evidencija){
                ServerSustava.evidencija.povecajBrojUspjesnihZahtjeva();
                long ukupniRad = System.currentTimeMillis() - startTime;
                ServerSustava.evidencija.povecajUkupnoTrajanjeRadaDretvi(ukupniRad);
                ServerSustava.evidencija.notify();
            }
        }
        
        KolekcionarDretvi.removeRadnaDretva(this);
        if(ServerSustava.stanjeServera == StanjeServera.STOPPED){
            serverShutdown();
        }
    } 
    
    @Override
    public void interrupt() {
        synchronized(ServerSustava.evidencija){
            ServerSustava.evidencija.povecajBrojPrekinutihZahtjeva();
            long ukupniRad = System.currentTimeMillis() - startTime;
            ServerSustava.evidencija.povecajUkupnoTrajanjeRadaDretvi(ukupniRad);
            ServerSustava.evidencija.notify();            
        }
        super.interrupt();
    }

    private boolean isValid(String sintaksa, StringBuffer message) {
        setMatcher(sintaksa, message);
        return privateMatcher.matches();
    }
    
    private void setMatcher(String sintaksa, StringBuffer message){
        Pattern pattern= Pattern.compile(sintaksa);
        privateMatcher = pattern.matcher(message);
    }  
    
    /**
     * metoda koja serijalizira podatke i zavrsava sa radom servera
     */
    private void serverShutdown() {
        SerijalizatorEvidencije ser = KolekcionarDretvi.getByName(NaziviDretvi.SERIJALIZATOR_EVIDENCIJE);
        
        synchronized(ser){
            
            if(!ser.serijalizacijaPotrebna){
                ser.serijalizacijaPotrebna = true;
                ser.notify();
            
                System.out.println("Cekanje serijalizacije");
                while (!ser.serijalizacijaPotrebna) {            
                    try {
                       ser.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
        
        
        System.exit(0);
    }
   
}
