/*
 * Copyright (C) 2017 mitz
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

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mitz
 */
public class KorisnikSustavaTest {
    
    public KorisnikSustavaTest() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try{
            Thread t = new Thread() {
                @Override
                public void run() {
                     String[] args = {"-konf NWTiS_mdomlad_zadaca_1.txt -load"};
                     ServerSustava.main(args);
                }
            };
            t.start();
        } catch(Exception ex){
          //We will kill thread   
        }
    }

    /**
     * Test of main method, of class KorisnikSustava.
     */
    @Test
    public void testKorisnikSustavaMain() {
        System.out.println("test KorisnikSustava");
        String[] args = {"-admin -server localhost -port 8000 -u pero -p 123456 -pause"};
        
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(KorisnikSustavaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        KorisnikSustava.main(args);
        assertTrue(ServerSustava.stanjeServera == ServerSustava.StanjeServera.PAUSED);
    }
    
}
