package org.foi.nwtis.mdomladov.zadaca_1;

import org.foi.nwtis.dkermek.konfiguracije.Konfiguracija;

/**
 *Sluzi za serijalizaciju podataka. 
 * Izvrsava serijalizaciju evidencije prema potrebi, 
 * a u meduvremenu ceka.
 * 
 * @author Marko Domladovac
 */
class SerijalizatorEvidencije extends KonfDretva {
    
    volatile boolean serijalizacijaPotrebna;

    public SerijalizatorEvidencije(Konfiguracija konf) {
        super(konf);
        super.setName(NaziviDretvi.SERIJALIZATOR_EVIDENCIJE);
    }
    
    @Override
    public void run() {
        try {
                while (true) {
                    synchronized (this) {
                        while (!serijalizacijaPotrebna){                            
                            wait();
                        }
                        System.out.println(this.getClass());
                        String filename = this.konf
                                .dajPostavku(KonfiguracijskiParametri.PUTANJA_EVIDENCIJSKE_DATOTEKE);
                        
                        boolean jeSerijaliziran = FileHelper.saveObject(filename, ServerSustava.evidencija);
                        
                        System.out.println(
                                String.format("Objekt %s serijalizaran uspješno;", jeSerijaliziran ? "je" : "nije")
                        );
                        
                        serijalizacijaPotrebna = false;
                        notify();
                    }
                }
            } 
        catch (InterruptedException ex) {
        
        } 
    }
}
