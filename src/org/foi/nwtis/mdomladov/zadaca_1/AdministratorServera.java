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
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko Domladovac
 * 
 * Administrator servera spaja se na server putem socketa i šalje komandu 
 * serveru na temelju upisanih parametara i traži izvršavanja određene akcije:
 * -----------------------------------------------------------------------------
 * USER korisnik; PASSWD lozinka; PAUSE;
 * upisan parametar -pause pa provjerava se postoji li korisnik i 
 * njemu pridružena lozinka u datoteci administratora (postavka adminDatoteka). 
 * Ako je u redu i server nije u stanju pause, privremeno prekida prijem svih 
 * komandi osim administratorskih. Korisniku se vraća odgovor OK.  Kada nije u 
 * redu, korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor 
 * ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako je u stanju pause 
 * vraća se odgovor ERROR 01; tekst (tekst objašnjava razlog pogreške). 
 * -----------------------------------------------------------------------------
 * USER korisnik; PASSWD lozinka; START;
 * upisan parametar -start pa provjerava se postoji li korisnik i njemu 
 * pridružena lozinka u datoteci administratora (postavka adminDatoteka). 
 * Ako je u redu i server je u stanju pause, nastavlja prijem svih komandi. 
 * Korisnku se vraća odgovor OK.  Kada nije u redu, korisnik nije administrator
 * ili lozinka ne odgovara, vraća se odgovor 
 * ERROR 00; tekst (tekst objašnjava razlog pogreške). 
 * Ako nije u stanju pause vraća se odgovor ERROR 02; 
 * tekst (tekst objašnjava razlog pogreške). 
 * -----------------------------------------------------------------------------
 * USER korisnik; PASSWD lozinka; STOP;
 * upisan parametar -stop pa provjerava se postoji li korisnik i njemu 
 * pridružena lozinka u datoteci administratora (postavka adminDatoteka). 
 * Ako je u redu prekida prijem komandi, serijalizira evidenciju rada i 
 * završava rad. Korisniku se vraća odgovor OK.  Kada nije u redu, korisnik 
 * nije administrator ili lozinka ne odgovara, vraća se odgovor 
 * ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako nešto nije u 
 * redu s prekidom rada ili serijalizacijom vraća se odgovor 
 * ERROR 03; tekst (tekst objašnjava razlog pogreške). 
 * -----------------------------------------------------------------------------
 * USER korisnik; PASSWD lozinka; STAT;
 * upisan parametar -stat pa provjerava se postoji li korisnik i njemu 
 * pridružena lozinka u datoteci administratora (postavka adminDatoteka). 
 * Ako je u redu korisniku se vraća odgovor OK; LENGTH nnnn CRLF i zatim vraća 
 * serijalizirane podatke o evidenciji rada. nnnn predstavlja broj byte-ova koje 
 * zauzima serijalizirana evidencija rada. Kada nije u redu, korisnik nije 
 * administrator ili lozinka ne odgovara, vraća se odgovor ERROR 00; tekst 
 * (tekst objašnjava razlog pogreške). Ako nešto nije u redu s evidencijom 
 * rada vraća se odgovor ERROR 04; tekst (tekst objašnjava razlog pogreške). 
 * Ako je evidencija rada u redu 
 * admninistrator ispisuje sve podatke u preglednom obliku.

 */
public class AdministratorServera {

    private final String evidencijaFilename;
    
    private enum Naredba {
        START,
        STOP,
        PAUSE,
        STAT
    }
    
    private HashMap<String, Naredba> naredbe;
    
    private Naredba naredba;

    /**
     * Konstruktor 
     * @param naredba - naredba koja je pozvana u sjednici
     * @param evidencijaFilename - putanja do evidencijske datoteke
     */
    public AdministratorServera(String naredba, String evidencijaFilename) {
        naredbe = new HashMap();
        naredbe.put("start", Naredba.START);
        naredbe.put("stop", Naredba.STOP);
        naredbe.put("pause", Naredba.PAUSE);
        naredbe.put("stat", Naredba.STAT);
        
        this.naredba = naredbe.get(naredba.toLowerCase());
        this.evidencijaFilename = evidencijaFilename;
    }
    
    /**
     * akcija aktiviranja komande - vrača odgovor
     * 
     * @return response 
     */
    public String aktivirajKomandu(){
        
        String response = null;
        
        switch(naredba){
            case START:
                if(ServerSustava.stanjeServera == ServerSustava.StanjeServera.STARTED){
                    response = "ERROR 02; Server je vec u stanju STARTED";
                } else {
                    ServerSustava.stanjeServera = ServerSustava.StanjeServera.STARTED;
                    response = "OK;";
                }
                break;
            case STOP:
                ServerSustava.stanjeServera = ServerSustava.StanjeServera.STOPPED;
                response = "OK;";
                break;
            case PAUSE:
                if(ServerSustava.stanjeServera == ServerSustava.StanjeServera.PAUSED){
                    response = "ERROR 01; Server je vec u stanju PAUSED";
                } else {
                    ServerSustava.stanjeServera = ServerSustava.StanjeServera.PAUSED;
                    response = "OK;";
                }
                break;
            case STAT:
                boolean isValid = false;
                String serijaliziranObjekt = null;
                File file = new File(evidencijaFilename);
                if(file.exists()){
                    Evidencija evidencija = FileHelper.loadObject(evidencijaFilename);
                    if(evidencija != null){
                        try {
                            serijaliziranObjekt =
                                    SerijalizatorHelper.objectToString(evidencija);
                        isValid = true;
                        } catch (IOException ex) {
                            Logger.getLogger(AdministratorServera.class.getName()).log(Level.SEVERE, null, ex);
                            isValid = false;
                        }
                    }
                }
                
                if(isValid){
                    response = String.format("OK;LENGTH %d<CRLF>%s", 
                            file.length(), serijaliziranObjekt);
                } else {
                    response = "ERROR 03;Evidencijska datoteka nije ispravna";
                }               
                
                break;
        }
        
        return response;
    }
}
