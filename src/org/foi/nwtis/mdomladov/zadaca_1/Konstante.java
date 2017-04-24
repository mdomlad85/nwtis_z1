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

/**
 * Konstante koje se koriste kroz aplikaciju
 * U obliku sucelja je iz razloga da se navedene osobine mogu dodavati
 * po potrebi
 * 
 * @author Marko Domladovac
 */
public interface Konstante {
    
    /**
     * Konstante za raspon portova
     * 
     */
    public interface RasponPortova{
        
        /**
         * Minimalni port
         * 
         */
        public static final int OD = 8000;
        
        /**
         * Maksimalni port
         * 
         */
        public static final int DO = 9999;

    }
    
    /**
     * Konfiguracijski parametri
     * 
     */
    public interface KonfiguracijskiParametri {

        /**
         * kofiguracijski parametar za port
         */
        public static final String PORT = "port";

        /**
         * kofiguracijski parametar za interval nadzorne dretve u ms
         */
        public static final String INTERVAL_NADZORNE_DRETVE = "intervalNadzorneDretve";

        /**
         * kofiguracijski parametar za maksimalno vrijeme izvršavanje jedne instance radne dretve.
         */
        public static final String MAX_VRIJEME_RADNE_DRETVE = "maksVrijemeRadneDretve";

        /**
         * kofiguracijski parametar za adresne nadzorne dretve u ms
         */
        public static final String INTERVAL_ADRESNE_DRETVE = "intervaAdresneDretve";

        /**
         * kofiguracijski parametar za maksimalan broj adresa
         */
        public static final String MAX_ADRESA = "maksAdresa";

        /**
         * kofiguracijski parametar za maksimalan broj instanci radnih dretvi
         */
        public static final String MAX_BROJ_RADNIH_DRETVI = "maksBrojRadnihDretvi";

        /**
         * kofiguracijski parametar za broj zahtjeva za serijalizaciju
         */
        public static final String BROJ_ZAHTJEVA_ZA_SERIJALIZACIJU = "brojZahtjevaZaSerijalizaciju";

        /**
         * kofiguracijski parametar za postavljanje evidencijske datoteke
         */
        public static final String PUTANJA_EVIDENCIJSKE_DATOTEKE = "evidDatoteka";

        /**
         * kofiguracijski parametar za postavljanje datoteke s administratorima
         */
        public static final String PUTANJA_ADMIN_DATOTEKE = "adminDatoteka";
    }
    
    /**
     * konstante nazivi dretvi
     */
    public interface NaziviDretvi {        
    
        /**
         * NADZOR_DRETVI
         */
        public static final String NADZOR_DRETVI = "NadzorDretvi";
        
        /**
         * PROVJERA_ADRESA
         */
        public static final String PROVJERA_ADRESA = "ProvjeraAdresa";
        
        /**
         * REZERVNA_DRETVA
         */
        public static final String REZERVNA_DRETVA = "RezervnaDretva";
        
        /**
         * SERIJALIZATOR_EVIDENCIJE
         */
        public static final String SERIJALIZATOR_EVIDENCIJE = "SerijalizatorEvidencije";
        
        /**
         * RADNA_DRETVA
         */
        public static final String RADNA_DRETVA = "RadnDretva";
    }
    
    /**
     * konstante vezane uz korisnika
     */
    public interface Korisnik {
        
        /**
         * LDAP_USERNAME
         */
        public static final String LDAP_USERNAME = "mdomladov";
    }
    
}
