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

import java.io.Serializable;
import java.util.HashSet;

/**
 * Klasa se koristi za vodenje evidencije o adresaru
 * 
 * @author Marko Domladovac
 */
class ZahtjevAdresa implements Serializable {

    /**
     * Popis korisnika koji su vezani uz zahtjevanu adresu
     */
    private final HashSet<String> korisnici;
    
    /**
     * Koliko je bilo zahtjeva vezano uz adresu
     */
    private int brojZahtjeva;
    
    /**
     * Sama adresa
     */
    private String url;
    
    /**
     * da li je adresa sintaksno ispravna
     */
    private Boolean validnaAdresa;

    public ZahtjevAdresa() {
        korisnici = new HashSet<>();
    }
        
        
    public void povecajBrojZahtjev(){
        brojZahtjeva++;
    }

    public int getBrojZahtjeva() {
        return brojZahtjeva;
    }
    
    public void dodajKorisnika(String korisnik){
        korisnici.add(korisnik);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }   

    public Boolean isValidnaAdresa() {
        return validnaAdresa;
    }

    public void setValidnaAdresa(boolean validnaAdresa) {
        this.validnaAdresa = validnaAdresa;
    }

    public String getKreator() {
       return (String) this.korisnici.toArray()[0];
    }
    
}
