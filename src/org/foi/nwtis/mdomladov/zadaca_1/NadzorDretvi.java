package org.foi.nwtis.mdomladov.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.Konfiguracija;
import static java.lang.Thread.sleep;

/**
 * Nadzorna dretva radi u pravilnim ciklusima prema podacima iz postavki. 
 * U svakom ciklusu ispituje sve radne dretve i provjerava da li je vrijeme 
 * koje je potrošila pojedina dretva od početka spajanja korisnika dulje od 
 * maksimalno dopuštenog vremena. Ako je takav slučaj, nadzorna dretva javlja 
 * radnoj dretvi da odmah prekine obradu zahtjeva, ažurira podatke u evidenciji 
 * rada i da zatim zatvori vezu na socketu
 * 
 * @author Marko Domladovac
 */
class NadzorDretvi extends KonfDretva{
    
    private final int maxTrajanjeRadneDretve;

    public NadzorDretvi(Konfiguracija konf) {
        super(konf);
        super.setName(NaziviDretvi.NADZOR_DRETVI);
         trajanjeSpavanja = Integer.parseInt(konf.dajPostavku(KonfiguracijskiParametri.INTERVAL_NADZORNE_DRETVE));
         maxTrajanjeRadneDretve = Integer.parseInt(konf.dajPostavku(KonfiguracijskiParametri.MAX_VRIJEME_RADNE_DRETVE));
    }

    @Override
    public void run() {
        super.run();
       
        //TODO provjeriti ime
        while(true){            
            System.out.println(this.getClass());
            
            for (RadnaDretva radnaDretva : KolekcionarDretvi.radneDretve) {
                 if(radnaDretva.getExecutionTime() > maxTrajanjeRadneDretve){
                    radnaDretva.interrupt();
                }
            }
            
            try {   
                sleep(getTrajanjeSpavanja());
                
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
