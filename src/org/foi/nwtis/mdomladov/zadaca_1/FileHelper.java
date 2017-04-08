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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Klasa se koristi kao pomoc pri serijalizaciji
 * i deserijalizaciji objekta 
 * 
 * @author Marko Domladovac
 */
public class FileHelper {

    /**
     * Metoda ucitava navedeni objekt
     * iz datoteke u memoriju
     * 
     * @param <T> definira tip objekta
     * @param filename - putanja datoteke
     * @return T - vraca deserijalizirani objekt
     */
    public static <T> T loadObject(String filename){
        T retVal = null;
        ObjectInputStream s = null;
        try {
            File file = new File(filename);
            if(file.exists()){
                FileInputStream in = new FileInputStream(file);
                s = new ObjectInputStream(in);
                retVal = (T)s.readObject();   
            }           
            
        } catch (FileNotFoundException ex) {
            System.err.println("Error: " + ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error: " + ex.getMessage());
        } finally {
            if(s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                    System.err.println("Error: " + ex.getMessage());
                }
            }
        }
        return retVal;
    } 
    
    /**
     *Metoda sprema objekt iz memorije
     * u datoteku
     * 
     * @param filename - putanja datoteke
     * @param obj - objekt koji se sprema
     * @return boolean - ako je uspjesno vraca istinu
     */
    public static synchronized boolean saveObject(String filename, Object obj){
        ObjectOutputStream s = null;
        try {
            File file = new File(filename);
            if(!file.exists()){
                file.createNewFile();
            }
            
            FileOutputStream out = new FileOutputStream(file);
            s = new ObjectOutputStream(out);
            s.writeObject(obj);
        } catch (FileNotFoundException ex) {
                System.err.println("Doslo je do pogreske prilikom spremanja objekta u datoteku");
        } catch (IOException ex) {
                System.err.println("Doslo je do pogreske prilikom spremanja objekta u datoteku");
        } finally {
            if(s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                     System.err.println("Doslo je do pogreske prilikom spremanja objekta u datoteku");
                }
            }
        }
        return true;
    }
}
