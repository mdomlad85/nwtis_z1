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
    
    /**
     *
     * Deklaracija kolekcije aktivnih dretvi
     * 
     */
    private static HashMap<String, KonfDretva> aktivneDretve;   
    
    /**
     *
     * Deklaracija kolekcije radnih dretvi
     * 
     */
    protected static ArrayList<RadnaDretva> radneDretve;
    
    /**
     * Dohvaćanje aktivnih dretvi tj. dretvi sustava
     * @param <T>
     * @param naziv
     * @return
     */
    public static <T> T getByName(String naziv){
        initializeCollections();
        return (T)aktivneDretve.get(naziv);
    }
    
    /**
     * Brisanje radne dretve iz kolekcije
     * 
     * @param dretva
     * @return
     */
    public static synchronized boolean removeRadnaDretva(RadnaDretva dretva){
        initializeCollections();
        return radneDretve.remove(dretva);
    }
    
    /**
     * Dodavanje aktivne dretve u kolekciju
     * 
     * @param naziv
     * @param dretva
     */
    public static synchronized void putAktivnaDretva(String naziv, KonfDretva dretva){
        initializeCollections();
        aktivneDretve.put(naziv, dretva);
    }    
    
    /**
     * Dodavanje radne dretve u kolekciju
     * 
     * @param dretva
     */
    public static synchronized void putRadnaDretva(RadnaDretva dretva){
        initializeCollections();
        radneDretve.add(dretva);
    }
    
    /**
     * Dohvaćanje aktivne dretve
     * 
     * @return
     */
    public static Collection<KonfDretva> getAktivneDretve(){
        initializeCollections();
        return aktivneDretve.values();
    }
    
    /**
     * Dohvaćanje radne dretve
     * 
     * @return
     */
    public static ArrayList<RadnaDretva> getRadneDretve(){
        initializeCollections();
        return radneDretve;
    }

    /**
     * Inicijalizacija kolekcija dretvi
     */
    private static synchronized void initializeCollections() {
        if(aktivneDretve == null){
            aktivneDretve = new HashMap<>();
        }
        
         if(radneDretve == null){
            radneDretve = new ArrayList<>();
        }
    }
}
