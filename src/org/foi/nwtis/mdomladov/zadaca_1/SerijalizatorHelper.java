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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * Pomocna klasa za serijalizaciju objekta 
 * kao priprema za odgovor servera
 * 
 * @author Marko Domladovac
 */
public class SerijalizatorHelper {

   /**
    * Metoda prihvaca base64 string, dekodira ga,
    * ucita ga u objekt te ga na kraju casta u
    * zadani tip
    * 
    * @param <T> - tip objekta
    * @param s - base64 string
    * @return - vraca objekt tipa T
    * @throws IOException - ukoliko dode do problema prilikom ucitavanja
    * @throws ClassNotFoundException - ukoliko cast ne prode
    */
   public static <T> T objectFromString( String s ) throws IOException ,
                                                       ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        Object o;
        try (ObjectInputStream ois = new ObjectInputStream( 
                new ByteArrayInputStream(  data ) )) {
            o = ois.readObject();
        } catch (IOException ex){
            System.err.println("Narusen je integritet prilikom slanja statistike!");
            throw ex;
        }
        return (T)o;
   }

   /**
    * Metoda prihvaca bilo koju klasu koja 
    * implementira sucelje Serializable, zapisuje u stream,
    * enkodira u base64 string
    * 
    * @param o
    * @return base64 string
    * @throws IOException 
    */
    public static String objectToString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream( baos )) {
            oos.writeObject( o );
        }
        return Base64.getEncoder().encodeToString(baos.toByteArray()); 
    }
}
