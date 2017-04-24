/*
 * Copyright (C) 2017 Marko Domladovac
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.foi.nwtis.mdomladov.zadaca_1;

import org.foi.nwtis.dkermek.konfiguracije.Konfiguracija;

/**
 * Roditeljska klasa za dretve
 * Implementira konstante
 * 
 * @author Marko Domladovac
 */
public abstract class KonfDretva extends Thread implements Konstante{
    
    
    protected Konfiguracija konf;
    
    /**
     * varijabla za vrijeme početka rada dretve
     */
    protected long startTime;
    
    /**
     * Deklaracija intervala
     * 
     */
    protected long interval;

    /**
     * Konstruktor
     * 
     * @param konf
     */
    public KonfDretva(Konfiguracija konf) {
         this.konf=konf;
    }
    
    /**
     * pozivanje serijalizatora evidencija
     */
    protected void pozoviSerijalizatoraEvidencije(){
        SerijalizatorEvidencije evid = KolekcionarDretvi
                .getByName(NaziviDretvi.SERIJALIZATOR_EVIDENCIJE);
        if(evid != null){
            synchronized(evid){
                evid.serijalizacijaPotrebna = true;
                evid.notify();
            }
        }
    }

    /**
     * Metoda run koja poziva baznu metodu i puni početno vrijeme
     */
    @Override
    public void run() {
        super.run();
        startTime = System.currentTimeMillis();
    }

    /**
     * Računa trajanje spavanja na temelju početnog vremena i intervala
     * 
     * @return spavanje
     */
    protected long getTrajanjeSpavanja() {
        int i = 1;
        long spavanje = 0;
        do {
            spavanje = i++ * interval -(System.currentTimeMillis() - startTime) / 1000;
        } while(spavanje < 0);
        
        return spavanje;
    }
    
}
