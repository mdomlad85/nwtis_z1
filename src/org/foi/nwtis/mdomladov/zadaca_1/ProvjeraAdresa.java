/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.zadaca_1;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.Konfiguracija;

/**
 * Adresna dretva radi u pravilnim ciklusima prema podacima iz postavki. 
 * U svakom ciklusu ispituje sve zadane adrese od strane korisnika i 
 * provjerava postoji li pojedina adresa. Slijedi ažuriranje podataka 
 * u evidenciji rada.
 * 
 * @author Marko Domladovac
 */
class ProvjeraAdresa extends KonfDretva {

    public ProvjeraAdresa(Konfiguracija konf) throws NumberFormatException{
        super(konf);
        super.setName(NaziviDretvi.PROVJERA_ADRESA);
        interval = Integer.parseInt(konf.dajPostavku(KonfiguracijskiParametri.INTERVAL_ADRESNE_DRETVE));
    }

    @Override
    public void run() {
        super.run();        
        
        while(true){
            System.out.println(this.getClass());
            
            synchronized(ServerSustava.evidencija){
                ServerSustava.evidencija
                        .getZahtjeviZaAdrese()
                        .values()
                        .forEach(
                                (ZahtjevAdresa zahtjev) -> {
                                    zahtjev.setValidnaAdresa(UrlHelper.isValid(zahtjev.getUrl()));
                                }
                        );
                ServerSustava.evidencija.notify();
            }
            
            try {
                sleep(getTrajanjeSpavanja());
            } catch (InterruptedException ex) {
                Logger.getLogger(ProvjeraAdresa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
       
}
