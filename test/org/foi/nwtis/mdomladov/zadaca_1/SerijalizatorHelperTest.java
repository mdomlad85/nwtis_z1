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

import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marko Domladovac
 */
public class SerijalizatorHelperTest {
    
    private Evidencija evidencija;
    
    private String serijaliziranBase64Str;
    
    private final int ukupnoTrajanje = 1000;
    
    private final int brojUspjesnihZahtjeva = 200;
    
    private final int brojNeuspjesnihZahtjeva = 50;
    
    private final String nazivZadnjeDretve = "test";
    
    
    public SerijalizatorHelperTest() {
        evidencija = new Evidencija();
    }
    
    @Before
    public void setUp() {
        evidencija.povecajUkupnoTrajanjeRadaDretvi(ukupnoTrajanje);
        evidencija.setNazivZadnjeDretve(nazivZadnjeDretve);
        
        for (int i = 0; i < brojUspjesnihZahtjeva; i++) {
            evidencija.povecajBrojUspjesnihZahtjeva();
        }
        
        for (int i = 0; i < brojNeuspjesnihZahtjeva; i++) {
            evidencija.povecajBrojPrekinutihZahtjeva();
        }
        
        try {
            serijaliziranBase64Str = SerijalizatorHelper.objectToString(evidencija);
        } catch (IOException ex) {
            System.err.println("Nesto nije u redu s metodom objectToString");
        }
    }
    
    @After
    public void tearDown() {
        evidencija = null;
    }

    /**
     * Test of objectFromString method, of class SerijalizatorHelper.
     * @throws java.lang.Exception
     */
    @Test
    public void testGoodObjectFromString() throws Exception {
        System.out.println("good objectFromString");
        Evidencija expResult = evidencija;
        Evidencija result = SerijalizatorHelper.objectFromString(serijaliziranBase64Str);
        assertEquals(expResult.getBrojPrekinutihZahtjeva(), result.getBrojPrekinutihZahtjeva());
        assertEquals(expResult.getBrojUspjesnihZahtjeva(), result.getBrojUspjesnihZahtjeva());
        assertEquals(expResult.getUkupnoTrajanjeRadaDretvi(), result.getUkupnoTrajanjeRadaDretvi());
        assertEquals(expResult.getNazivZadnjeDretve(), result.getNazivZadnjeDretve());
        assertEquals(expResult.getUkupnoZahtjeva(), result.getUkupnoZahtjeva());
    }
    
    /**
     * Test of objectFromString method, of class SerijalizatorHelper.
     * @throws java.lang.Exception
     */
    @Test
    public void testBadObjectFromString() throws Exception {
        System.out.println("bad objectFromString");
        Evidencija expResult = new Evidencija();
        Evidencija result = SerijalizatorHelper.objectFromString(serijaliziranBase64Str);
        assertNotEquals(expResult.getBrojPrekinutihZahtjeva(), result.getBrojPrekinutihZahtjeva());
        assertNotEquals(expResult.getBrojUspjesnihZahtjeva(), result.getBrojUspjesnihZahtjeva());
        assertNotEquals(expResult.getUkupnoTrajanjeRadaDretvi(), result.getUkupnoTrajanjeRadaDretvi());
        assertNotEquals(expResult.getNazivZadnjeDretve(), result.getNazivZadnjeDretve());
        assertNotEquals(expResult.getUkupnoZahtjeva(), result.getUkupnoZahtjeva());
    }

    /**
     * Test of objectToString method, of class SerijalizatorHelper.
     * @throws java.lang.Exception
     */
    @Test
    public void testGoodObjectToString() throws Exception {
        System.out.println("good objectToString");
        String expResult = serijaliziranBase64Str;
        String result = SerijalizatorHelper.objectToString(evidencija);
        assertEquals(expResult, result);
    }

    /**
     * Test of objectToString method, of class SerijalizatorHelper.
     * @throws java.lang.Exception
     */
    @Test
    public void testBadObjectToString() throws Exception {
        System.out.println("bad objectToString");
        String expResult = "Fail test";
        String result = SerijalizatorHelper.objectToString(evidencija);
        assertNotEquals(expResult, result);
    }
    
}
