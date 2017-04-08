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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
/**
 *
 * Klasa koja se koristi kao pomocna
 * za rad s urlovima
 * 
 * @author Marko Domladovac
 */
public class UrlHelper {    
    
    /**
     * Metoda provjerava ispravnost url-a
     * samo u sintaksnom smislu
     * 
     * @param urlStr - string url
     * @return istina ili laz
     */
    public static boolean isValid(String urlStr){
       URL u = null;
        try {
            u = new URL((String) urlStr);
        } catch (MalformedURLException ex) {
           return false;
        }
        try {  
            u.toURI();  
        } catch (URISyntaxException e) {  
            return false;  
        }  
        return true;
    }

}
