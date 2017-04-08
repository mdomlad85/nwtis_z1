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

import java.util.concurrent.TimeUnit;

/**
 *Klasa se koristi za pomoc pri parsiranju
 * 
 * @author Marko Domladovac
 */
public class ParserHelper {
    
    /**
     * metoda parsira u int ili vraca zadanu vrijednost
     * ukoliko dode do pogreske
     * 
     * @param val - vrijednost
     * @param other - zamjenska vrijednost
     * @return parsirani string ili zamjensku vrijednost
     */
    public static int numericOrElse(String val, int other){
        try{        
            return Integer.parseInt(val);        
        } catch (NumberFormatException e){
            return other;
        }
    }
    
    /**
     * Metoda prihvača milisekunde i pretvara
     * u formatirani string oblika: H:m:s.ms
     * 
     * @param millis - milisekunde
     * @return formatirani string oblika: H:m:s.ms
     */
    public static String formatMilisecondsToString(long millis) { 
        
        long h = TimeUnit.MILLISECONDS.toHours(millis);
        long m = TimeUnit.MILLISECONDS.toMinutes(millis) 
                - TimeUnit.HOURS.toMinutes(h);
        
        long s = TimeUnit.MILLISECONDS.toSeconds(millis) 
                - TimeUnit.MINUTES.toSeconds(m);
        
        long ms = millis - TimeUnit.SECONDS.toMillis(s);
        
        return String.format("%02d:%02d:%02d.%d", h, m, s, ms);
    }
}
