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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * utility za skidanje datoteke sa servera
 * @author www.codejava.net
 */
public class HttpDownloadHelper {
    private static final int BUFFER_SIZE = 4096;

    /**
     * filename - koristi se za pohranu putanje 
     * do dokumenta koji se downloada na server
     */
    public static String filename;

    /**
     * Preuzimanje datoteke s url-a
     * @param fileURL HTTP URL datoteke za preuzeti
     * @param saveDir direktorij gdje se snima
     * @return boolean
     */
    public static boolean downloadFile(String fileURL, String saveDir) {
        
        try {
            URL url = new URL(fileURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            
            // provjeri response da li je ok
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength();
                
                if (disposition != null) {
                    // uzima naziv datoteke iz headera
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                            fileURL.length());
                }
                
                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);                
                
                try ( // otvaranje ulaza
                        InputStream inputStream = httpConn.getInputStream()) {
                    filename = fileName;
                    
                    String saveFilePath = saveDir + File.separator + fileName;
                    if(saveDir.isEmpty()){
                        saveFilePath = fileName;
                    }
                    
                    // otvaranje izlaza
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);
                    
                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    
                    outputStream.close();
                } catch (Exception ex){
                    return false;
                }
                
                System.out.println("Dokument je preuzet");
            } else {
                return false;
            }
            httpConn.disconnect();
            return true;
        } catch (MalformedURLException ex) {
                return false;
        } catch (IOException ex) {
                return false;
        }
    }
}
