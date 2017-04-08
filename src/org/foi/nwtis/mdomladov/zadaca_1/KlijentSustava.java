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

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Temeljni rad opisan u roditeljskoj klasi 
 * 
 * @author Marko Domladovac
 */
class KlijentSustava extends KorisnikSustava {
    
    private enum Naredba {
        ADD,
        TEST,
        WAIT;
    }
    
    private final HashMap<String, Naredba> naredbe;

    /**
     * Definiranje sintakse koja je potrebna za rad u roditeljskoj klasi
     * 
     * @param p 
     */
    public KlijentSustava(String p) {
        this.naredbe = new HashMap<>();
        this.naredbe.put("a", Naredba.ADD);
        this.naredbe.put("t", Naredba.TEST);
        this.naredbe.put("w", Naredba.WAIT);
        
        sintaksa = "^-korisnik +-s ([A-Za-z0-9\\.]+) +-port ((8|9)[0-9]{3}) +-u +(\\w+)( +-(a|t) +(([\\w\\.\\:\\/]+))|( +-(w) +([0-9]{1,3})))$";
        poruka = p;
        Pattern pattern = Pattern.compile(sintaksa);
        m = pattern.matcher(poruka);
    }    
    
    /**
     * Definiranje zahtjeva kako bi vršna klasa mogla funkcionirati
     * 
     * @return zahtjev od korisnika
     */
    @Override
    public String getZahtjev() {
        
        Naredba naredba = this.naredbe.get(m.group(6));
        
        if(naredba == null){
            naredba = this.naredbe.get(m.group(10));
        }
        
        if(naredba != null){
            String zahtjevNaredba = null;
        
            switch(naredba){
                case ADD:
                    zahtjevNaredba = String.format("ADD %s", m.group(7));
                    break;
                case TEST:
                    zahtjevNaredba = String.format("TEST %s", m.group(7));
                    break;
                case WAIT:
                    zahtjevNaredba = String.format("WAIT %s", m.group(11));
                    break;
                default:
                    return null;
            }
            
            return String.format("USER %s; %s;", 
                    m.group(4), zahtjevNaredba);
        } 
        
        return null;
    }

}
