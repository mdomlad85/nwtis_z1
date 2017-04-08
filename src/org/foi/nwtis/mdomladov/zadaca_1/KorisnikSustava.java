/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Korisnički program se može izvršavati više puta kako bi se koristili različiti 
 * dijelovi. Izvršavanje programa započinje utvrđivanjem vrste rada 
 * (admin, klijent, prikaz) na bazi opcija u parametarima. Komunikacija 
 * pojedinih vrsta korisnika (administrator, klijent, prikaz) i servera 
 * temelji se na jednostavnom protokolu koji ima određenu sintaksu, 
 * a sadrži skup komandi. Komande i njihova sintaksa objašnjeni su kod 
 * pojedine vrsta korisnika. Logično je da se prvo pokreće server, a nakon 
 * njega korisnici. Komunikaciju otvara jedna od vrsta korisnika tako da šalje 
 * zahtjev serveru u obliku određene komande. Server provodi analizu zahtjeva i 
 * ako je zahtjev u redu provodi određenu operaciju te vraća status operacije i 
 * ostale potrebne podatke. Kada zahtjev nije u redu vraća primjereni status 
 * operacije.
 * 
 * Klasa je apstraktna
 * 
 * @author Marko Domladovac
 */
public abstract class KorisnikSustava {
    
    protected String poruka;
    
    protected String sintaksa;
    
    protected Matcher m;
    
    /**
     * Abstraktna metoda i svaka klasa koja nasljeđuje
     * mora definirati kako izgleda zahtjev
     * 
     * @return  
     */    
    public abstract String getZahtjev();    

    public String getServer() {
        return m.group(1);
    }

    public int getPort() {
        return Integer.parseInt(m.group(2));
    }

    public boolean isValid() {
        return m.matches();
    }
    
    public void setMatcher(Matcher m){
        this.m = m;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        // TODO code application logic here
          //TODO ne zaboravi ostale moguće parametre                                                         
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }        
        String p = sb.toString().trim();
        
        KorisnikSustava korisnik = null;
        boolean prikaziSamoEvidenciju = false;
        if(p.contains("-admin")){
            korisnik = new AdministratorSustava(p);
        } else if(p.contains("-korisnik")){
            korisnik = new KlijentSustava(p);
        } else if(p.contains("-prikaz")){
            korisnik = new PrikazSustava(p);
            prikaziSamoEvidenciju = true;  
        } else {
            System.err.println("Nepoznat korisnik sustava");
            return;
        }
        
        if(korisnik.isValid()){
            if(prikaziSamoEvidenciju){
                prikaziEvidenciju(korisnik.getZahtjev());
            } else {
                pokreniKorisnika(korisnik);
            }
        } else {
            System.out.println("Neispravna naredba!");
        }
    }

    private static void pokreniKorisnika(KorisnikSustava korisnik) {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
       
        try {
            socket= new Socket(korisnik.getServer(),korisnik.getPort());
            
            is = socket.getInputStream();
            os = socket.getOutputStream();
            os.write(korisnik.getZahtjev().getBytes());
            os.flush();
            socket.shutdownOutput();
            
            StringBuffer sb= new StringBuffer();
            
          
            while(true){
                int znak= is.read();
                if(znak==-1){
                    break;
                }
                sb.append((char)znak);
            }
            
             prikaziOdgovor(sb.toString());
            
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Neispravna naredba");
        
    }finally {
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
    
    }

    private static void prikaziEvidenciju(String filename) {
        
        Evidencija evid = null;        
        boolean isDownloaded = HttpDownloadHelper.downloadFile(filename, "");
        
        if(isDownloaded){
           evid = FileHelper.loadObject(HttpDownloadHelper.filename); 
           File file = new File(filename);
           file.delete();
        } else {
            evid = FileHelper.loadObject(filename);
        }
        
        if(evid != null){
            System.out.println(evid);
        } else {
            System.err.println("Datoteka nije ispravna!");
        }
    }

    private static void prikaziOdgovor(String odgovor) {
        String okPattern = "^OK; *((LENGTH (\\d+)+<CRLF>((?:[A-Za-z0-9+\\/]{4})*(?:[A-Za-z0-9+\\/]{2}==|[A-Za-z0-9+\\/]{3}=)?))|(YES|NO))?$";
        String errPatern = "^ERROR *\\d{2}; *(.+)$";
        
        Pattern okP = Pattern.compile(okPattern);
        Pattern errP = Pattern.compile(errPatern);
        
        Matcher okM = okP.matcher(odgovor);
        Matcher errM = errP.matcher(odgovor);
        
        if(okM.matches()){
           String provjeraAdresa = okM.group(5);
           String evidStr = okM.group(4);
           if(provjeraAdresa != null){
               System.out.println(String.format("Tražena adresa je %sispravna", 
                       "NO".equals(provjeraAdresa.toUpperCase()) ? "ne" : ""));
           }else if(evidStr != null){
               try {
                   Evidencija evid = SerijalizatorHelper.objectFromString(evidStr);
                   System.out.println(evid);
               } catch (IOException | ClassNotFoundException ex) {
                   Logger.getLogger(KorisnikSustava.class.getName()).log(Level.SEVERE, null, ex);
               }
           } else {
               System.out.println("Zahtjev je uspjesno obraden");
           }
           
           
        } else if(errM.matches()){
            System.err.println(String.format("Pogreska: %s", errM.group(1)));
        } else {
            System.out.println("Nepoznat odgovor");
        }
    }
}
