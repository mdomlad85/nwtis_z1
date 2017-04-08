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

import java.util.regex.Pattern;

/**
 * Temeljni rad opisan u roditeljskoj klasi 
 * 
 * @author Marko Domladovac
 */
public class AdministratorSustava extends KorisnikSustava {
    
    /**
     * Definiranje sintakse koja je potrebna za rad u roditeljskoj klasi
     * 
     * @param p 
     */
    public AdministratorSustava(String p) {
        sintaksa = "^-admin -server ([^\\s]+) -port ([89][0-9]{3}) +-u ([\\w-]+) +-p ([\\w-#!]+) +-(pause|start|stop|stat)$";
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
    public String getZahtjev(){
        return String.format("USER %s; PASSWD %s;%s;", 
                    m.group(3), m.group(4), m.group(5).toUpperCase());
    }
}
 