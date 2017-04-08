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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marko Domladovac
 */
public class HttpDownloadHelperTest {
    
     private final String goodUrlExample = "http://www.pdf995.com/samples/pdf.pdf";
   
    private final String badUrlExample = "www.pdf995.com/samples/niente";
    
    public HttpDownloadHelperTest() {
    }
    
    /**
     * Test of downloadFile method, of class HttpDownloadHelper.
     */
    @Test
    public void testGoodDownloadFile() {
        System.out.println("Good downloadFile");
        boolean result = HttpDownloadHelper.downloadFile(goodUrlExample, "");        
        assertTrue(result);
    }   
    

    /**
     * Test of downloadFile method, of class HttpDownloadHelper.
     */
    @Test
    public void testBadDownloadFile() {
        System.out.println("bad downloadFile");
        boolean result = HttpDownloadHelper.downloadFile(badUrlExample, "");
        assertEquals(result, false);
    }
    
}
