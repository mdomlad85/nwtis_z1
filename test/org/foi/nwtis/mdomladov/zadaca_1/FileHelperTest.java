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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marko Domladovac
 */
public class FileHelperTest {
    
    public FileHelperTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of loadObject method, of class FileHelper.
     */
    @Test
    public void testGoodLoadObject() {
        System.out.println("God loadObject");
        String filename = "evidencija.bin";
        Evidencija result = FileHelper.loadObject(filename);
        assertNotNull(result);
    }   
    

    /**
     * Test of loadObject method, of class FileHelper.
     */
    @Test
    public void testBadLoadObject() {
        System.out.println("loadObject");
        String filename = "evidencija";
        Evidencija result = FileHelper.loadObject(filename);
        assertNull(result);
    }    

    /**
     * Test of saveObject method, of class FileHelper.
     */
    @Test
    public void testSaveObject() {
        System.out.println("Good saveObject");
        String filename = "evidencija_new.bin";
        Evidencija obj = new Evidencija();
        boolean result = FileHelper.saveObject(filename, obj);
        assertTrue(result);
    }
}
