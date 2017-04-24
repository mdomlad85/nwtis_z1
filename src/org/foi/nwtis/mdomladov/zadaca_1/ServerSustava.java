package org.foi.nwtis.mdomladov.zadaca_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.dkermek.konfiguracije.Konfiguracija;
import org.foi.nwtis.dkermek.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dkermek.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.dkermek.konfiguracije.NemaKonfiguracije;

/**
 * Naredba: ServerSustava -konf datoteka{.txt | .xml | .bin} [-load]
 * 
 * Server na početku provjerava postoji li datoteka konfiguracije (opcija -konf) 
 * te prekida rad ako ne postoji. Ako postoji, učitava postavke iz datoteke 
 * konfiguracije. Ako je upisana opcija -load, provjerava postoji li datoteka sa 
 * serijaliziranim podacima (postavka evidDatoteka) te ju učitava ako postoji. 
 * Ako ne postoji ispisuje informaciju i nastavlja rad.
 * 
 * Server uspostavlja početno stanje na temelju postavki. Server kreira i 
 * pokreće nadzornu dretvu, kreira i pokreće adresnu dretvu, kreira i pokreće 
 * rezervnu dretvu, a zatim kreira ServerSoket na zadanom portu iz postavki i
 * čeka da se spoji korisnik. Nakon spajanja korisnika kreira se i starta radna 
 * dretva koja obrađuje zahtjev korisnika
 * 
 * @author Marko Domladovac
 */
public class ServerSustava implements Konstante{ 
    
    /**
     * evidencija zahtjeva
     * bez obzira sto nije konačna (final) klasa definira se prije pokretanja 
     * dretvi kje imaju konkurentan pristup istoj te zbog toga nema problema u
     * izvrsavanju
     * logiranje rada sustava
     */
    public static Evidencija evidencija;
    
    /**
     * stanje servera moze biti pauziran ili pokrenut
     */
    public static StanjeServera stanjeServera;
    
    /**
     * ulaz u sam server
     * poziva sa parametrima -konf naziv datoteke -load
     * npr. -konf datoteka.xml -load
     * 
     * @param args 
     */
    public static void main(String[] args) {
        
        String sintaksa1 = "^-konf ([^\\s]+\\.(?i))(txt|xml|bin)( +-load)?$";
                                                                   
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        
        String p = sb.toString().trim();
        Pattern pattern = Pattern.compile(sintaksa1);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if (status) {
            int poc = 0;
            int kraj = m.groupCount();
            for (int i = poc; i <= kraj; i++) {
                System.out.println(i + ". " + m.group(i));
            }
            
            String nazivDatoteke = m.group(1)+ m.group(2);
            boolean trebaUcitatiEvidenciju=false;
            if(m.group(3)!=null){
             trebaUcitatiEvidenciju=true;
            }
            
            ServerSustava server = new ServerSustava();
            try {
                server.pokreniServer(nazivDatoteke,trebaUcitatiEvidenciju);
            } catch (IOException ex) {
                System.out.println("Pogreska prilikom pokretanja servera: " + ex.getMessage());
                Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            }            
            
        } else {
            System.out.println("Ne odgovara!");
        }
    }

    /**
     * Server uspostavlja pocetno stanje na temelju postavki. 
     * Server kreira i pokrece nadzornu dretvu, kreira i pokrece adresnu dretvu, 
     * kreira i pokreće rezervnu dretvu, 
     * a zatim kreira ServerSoket na zadanom portu iz postavki i 
     * ceka da se spoji korisnik. Nakon spajanja korisnika kreira se i 
     * starta radna dretva
     * 
     * @param nazivDatoteke
     * @param trebaUcitatiEvidenciju
     * @throws IOException 
     */
    private void pokreniServer(String nazivDatoteke, boolean trebaUcitatiEvidenciju) throws IOException {
        
        try {
            stanjeServera = StanjeServera.STARTED;
            boolean newInstanceNeeded = false;
            
            Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(nazivDatoteke);
            if(trebaUcitatiEvidenciju){
                 String filename = konf
                            .dajPostavku(KonfiguracijskiParametri.PUTANJA_EVIDENCIJSKE_DATOTEKE);
                 evidencija = FileHelper.loadObject(filename);
                 if(evidencija == null){
                     System.err.println("Navedena evidencijska datoteka nije ispravna.");
                     newInstanceNeeded = true;
                 }
            } else {
                newInstanceNeeded = true;
            }
            
            if(newInstanceNeeded){
                evidencija = new Evidencija();
            }
            
            int maxBrojDretvi = konf.postojiPostavka(KonfiguracijskiParametri.MAX_BROJ_RADNIH_DRETVI) 
                    ? ParserHelper.numericOrElse(konf.dajPostavku(KonfiguracijskiParametri.MAX_BROJ_RADNIH_DRETVI), 1) 
                    : 1;
            
            int port = ParserHelper.numericOrElse(konf.dajPostavku(KonfiguracijskiParametri.PORT), -1);
            
            if(port == -1 || port < RasponPortova.OD || port > RasponPortova.DO ){
                String msg = String.format("Konfiguracija porta nije ispravna. Port mora biti broj izmedju %d i %d", 
                                RasponPortova.OD, RasponPortova.DO);
                
                throw new NeispravnaKonfiguracija(msg);
                
            }            
                       
            KolekcionarDretvi
                    .putAktivnaDretva(NaziviDretvi.NADZOR_DRETVI, new NadzorDretvi(konf));
            KolekcionarDretvi
                    .putAktivnaDretva(NaziviDretvi.PROVJERA_ADRESA, new ProvjeraAdresa(konf));
            SerijalizatorEvidencije serijalizatorEvidencije = new SerijalizatorEvidencije(konf);
            KolekcionarDretvi
                    .putAktivnaDretva(NaziviDretvi.SERIJALIZATOR_EVIDENCIJE, serijalizatorEvidencije);
            RezervnaDretva rezervnaDretva = new RezervnaDretva(konf);
            KolekcionarDretvi
                    .putAktivnaDretva(NaziviDretvi.REZERVNA_DRETVA, rezervnaDretva);
            
            KolekcionarDretvi.getAktivneDretve().stream().forEach((dretva) -> {
                dretva.start();
            });
            
            ServerSocket ss = new ServerSocket(port);
            
            int brojZahtjevaZaSerijalizaciju =
                    Integer.parseUnsignedInt(konf.dajPostavku(KonfiguracijskiParametri.BROJ_ZAHTJEVA_ZA_SERIJALIZACIJU));
            
            int trenutniZahtjev = 0;
            
            while(true) {
                Socket socket=ss.accept();
                
                if(++trenutniZahtjev % brojZahtjevaZaSerijalizaciju == 0){
                      synchronized(serijalizatorEvidencije){
                        serijalizatorEvidencije.serijalizacijaPotrebna = true;
                        serijalizatorEvidencije.notify();
                    }
                }
                
                if(KolekcionarDretvi.getRadneDretve().size() == maxBrojDretvi){
                    synchronized(rezervnaDretva){
                        rezervnaDretva.rezervaPotrebna = true;
                        rezervnaDretva.notify();
                    }
                 } else {
                    RadnaDretva radna = new RadnaDretva(konf, socket);
                    KolekcionarDretvi.putRadnaDretva(radna);
                    radna.start();
                 }
            }            
            
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(ServerSustava.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Greska: " + ex.getMessage());
        }
    }
    
    /**
     * Stanja servera
     */
    public enum StanjeServera {

        /**
         * Server je pokrenut
         */
        STARTED,

        /**
         * Server je pauziran i prima samo administratorske zahtjeve
         */
        PAUSED,

        /**
         * Server se gasi
         */
        STOPPED
    }
}
