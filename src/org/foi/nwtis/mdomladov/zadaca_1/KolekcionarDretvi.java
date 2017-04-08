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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Klasa za čuvanje podataka i stanja o dretvama
 * 
 * @author Marko Domladovac
 */
public class KolekcionarDretvi {
    
    private static HashMap<String, KonfDretva> aktivneDretve;   
    
    protected static ArrayList<RadnaDretva> radneDretve;
    
    public static <T> T getByName(String naziv){
        initializeCollections();
        return (T)aktivneDretve.get(naziv);
    }
    
    public static KonfDretva deleteByName(String naziv){
        initializeCollections();
        return aktivneDretve.remove(naziv);
    }    
    
    public static boolean removeRadnaDretva(RadnaDretva dretva){
        initializeCollections();
        return radneDretve.remove(dretva);
    }
    
    public static void putAktivnaDretva(String naziv, KonfDretva dretva){
        initializeCollections();
        aktivneDretve.put(naziv, dretva);
    }    
    
    public static void putRadnaDretva(RadnaDretva dretva){
        initializeCollections();
        radneDretve.add(dretva);
    }
    
    public static Collection<KonfDretva> getAktivneDretve(){
        initializeCollections();
        return aktivneDretve.values();
    }
    
    public static ArrayList<RadnaDretva> getRadneDretve(){
        initializeCollections();
        return radneDretve;
    }

    private static void initializeCollections() {
        if(aktivneDretve == null){
            aktivneDretve = new HashMap<>();
        }
        
         if(radneDretve == null){
            radneDretve = new ArrayList<>();
        }
    }
}
