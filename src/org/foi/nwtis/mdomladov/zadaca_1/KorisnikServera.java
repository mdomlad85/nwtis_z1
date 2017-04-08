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

import java.util.HashMap;

/**
 * Korisnik servera spaja se na server putem socketa i nakon toga šalje komandu 
 * serveru na temelju upisanih parametara i traži izvršavanja određene akcije:
 * -----------------------------------------------------------------------------
 * USER korisnik; ADD adresa; 
 * upisan parametar -a URL . Dodaje adresu (URL) u popis adresa za koje se 
 * provjerava postoje li. Ako ne postoji adresa i ima slobodnog mjesta za adresu 
 * vraća mu se odgovor OK;  Ako ne postoji adresa ali nema slobodnog mjesta za 
 * adresu vraća mu se odgovor ERROR 10; tekst (tekst objašnjava razlog pogreške) 
 * Ako već postoji adresa vraća mu se odgovor ERROR 11; tekst (tekst objašnjava 
 * razlog pogreške)
 * -----------------------------------------------------------------------------
 * USER korisnik; TEST adresa;  
 * upisan parametar -t URL . Vraća status zadnje provjere zadane adrese (URL). 
 * Ako postoji adresa vraća mu se odgovor OK;  {YES | NO}; Ako ne postoji 
 * adresa vraća mu se odgovor ERROR 12; tekst (tekst objašnjava razlog pogreške)
 * -----------------------------------------------------------------------------
 * USER korisnik; WAIT nnnnn;  
 * upisan parametar -w nnn . Radna dretva treba čekati zadani broj sekundi 
 * pretvorenih u milisekunde. Ako je uspješno odradila čekanje vraća mu se 
 * odgovor OK; Ako nije uspjela odraditi čekanje vraća mu se odgovor ERROR 13; 
 * tekst (tekst objašnjava razlog pogreške). 

 * @author Marko Domladovac
 */
public class KorisnikServera {
    
    private enum Naredba {
        ADD,
        TEST,
        WAIT,
        STAT
    }
    
    private final HashMap<String, Naredba> naredbe;
    
    private final Naredba naredba;
    
    private final int maxAdresa;
    
    Integer waitTime;

    public KorisnikServera(String naredba, int maxAdresa) {
        naredbe = new HashMap();
        naredbe.put("add", Naredba.ADD);
        naredbe.put("test", Naredba.TEST);
        naredbe.put("wait", Naredba.WAIT);
        
        this.maxAdresa = maxAdresa;
        this.naredba = naredbe.get(naredba.toLowerCase());
    }
    
    public String aktivirajKomandu(String korisnik, Object param){
        
        String response = null;
        
        synchronized(ServerSustava.evidencija){
            switch(naredba){
                case ADD:
                    if(ServerSustava.evidencija.postojiAdresa(param.toString())){
                        response = "ERROR 11; Adresa vec postoji!";                    
                    } else {
                        if(ServerSustava.evidencija.getZahtjeviZaAdrese().size() < maxAdresa){
                            ServerSustava.evidencija.dodajZahtjevZaAdrese(param.toString(), korisnik);
                            response = "OK;";
                        } else {
                            response = "ERROR 10; Nema slobodnog mjesta u adresaru!";
                        }
                    }
                    break;
                case TEST:
                    if(ServerSustava.evidencija.postojiAdresa(param.toString())){

                        ZahtjevAdresa adresa = ServerSustava
                                .evidencija
                                .getZahtjeviZaAdrese()
                                .get(param);

                        if(adresa.isValidnaAdresa() != null){
                            response = String.format("OK;%s", adresa.isValidnaAdresa() 
                                    ? "YES" 
                                    : "NO");
                        } else {
                            response = "ERROR 12; Adresa jos nije provjerena!";
                        }
                    } else {
                        response = "ERROR 12; Nema adrese u adresaru!";
                    }
                    break;
                case WAIT:
                    try{
                        waitTime = Integer.parseInt(param.toString());
                    } catch(NumberFormatException nex){
                        waitTime = null;
                    }
                    break;
                default:
                    response = "ERROR 90; Nepoznata naredba";
                    break;
            }
            ServerSustava.evidencija.notify();
        }
        
        return response;
    }
}
