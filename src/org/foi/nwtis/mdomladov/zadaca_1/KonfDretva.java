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

import org.foi.nwtis.mdomladov.konfiguracije.Konfiguracija;

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
    
    protected long trajanjeSpavanja;

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
    
    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        super.run();
        startTime = System.currentTimeMillis();
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }   

    protected long getTrajanjeSpavanja() {
        int i = 1;
        long spavanje = 0;
        do {
            spavanje = i++ * trajanjeSpavanja -(System.currentTimeMillis() - startTime) / 1000;
        } while(spavanje < 0);
        
        return spavanje;
    }
    
}
