/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.zadaca_1;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Služi za evidenciju podataka i može se serijalizirati
 * 
 * @author Marko Domladovac
 */
public class Evidencija implements Serializable {
    private int ukupnoZahtjeva = 0;
    private int brojUspjesnihZahtjeva=0;
    private int brojPrekinutihZahtjeva=0;
    private long ukupnoTrajanjeRadaDretvi=0;
    private String nazivZadnjeDretve;
    private final HashMap<String,ZahtjevAdresa> zahtjeviZaAdrese= new HashMap<>();

    /**
     * Vraća ukupan broj zahtjeva poslan prema serveru
     * 
     * @return ukupnoZahtjeva
     */
    public int getUkupnoZahtjeva() {
        return ukupnoZahtjeva;
    }

    /**
     * Vraća broj uspješno obrađenih zahtjeva
     * 
     * @return brojUspjesnihZahtjeva
     */
    public int getBrojUspjesnihZahtjeva() {
        return brojUspjesnihZahtjeva;
    }    

    /**
     * Povećava broj uspješno obrađenih zahtjeva
     * i naravno broj ukupnih zahtjeva
     */
    public synchronized void povecajBrojUspjesnihZahtjeva() {
        this.brojUspjesnihZahtjeva++;
        this.ukupnoZahtjeva++;
    }

    /**
     * Vraća broj prekinutih zahtjeva
     * 
     * @return brojPrekinutihZahtjeva
     */
    public int getBrojPrekinutihZahtjeva() {
        return brojPrekinutihZahtjeva;
    }  

    /**
     * Povećava broj uspješno obrađenih zahtjeva
     * i naravno broj prekinutih zahtjeva zahtjeva
     */
    public synchronized void povecajBrojPrekinutihZahtjeva() {
        this.brojPrekinutihZahtjeva++;
        this.ukupnoZahtjeva++;
    }

    /**
     * Vraća ukupno trajanje svih dretvi u milisekundama
     * 
     * @return ukupnoTrajanjeRadaDretvi
     */
    public long getUkupnoTrajanjeRadaDretvi() {
        return ukupnoTrajanjeRadaDretvi;
    }

    /**
     * Povećava ukupno trajanje rada dretvi za dodanu vrijednost u milisekundama
     * @param ukupnoTrajanjeRadaDretve
     */
    public synchronized void povecajUkupnoTrajanjeRadaDretvi(long ukupnoTrajanjeRadaDretve) {
        this.ukupnoTrajanjeRadaDretvi += ukupnoTrajanjeRadaDretve;
    }

    /**
     * Dohvaća naziv zadnje dretve
     * 
     * @return  nazivZadnjeDretve
     */
    public String getNazivZadnjeDretve() {
        return nazivZadnjeDretve;
    }

    /**
     * Postavlja naziv zadnje dretve
     * 
     * @param nazivZadnjeDretve
     */
    public void setNazivZadnjeDretve(String nazivZadnjeDretve) {
        this.nazivZadnjeDretve = nazivZadnjeDretve;
    }

    /**
     * Vraća kolekciju zahtjeva za adresama (url i korisnik)
     * @return zahtjeviZaAdrese
     */
    public HashMap<String, ZahtjevAdresa> getZahtjeviZaAdrese() {
        return zahtjeviZaAdrese;
    }

    /**
     * Sprema u kolekciju zahtjev
     * @param url
     * @param korisnik 
     */
    public void dodajZahtjevZaAdrese(String url, String korisnik) {
        
            ZahtjevAdresa zahtjev = zahtjeviZaAdrese.get(url);
            
            if(zahtjev == null){
                zahtjev = new ZahtjevAdresa();
                zahtjev.setUrl(url);
                zahtjev.dodajKorisnika(korisnik);
            }
            
            zahtjev.povecajBrojZahtjev();
            
            zahtjeviZaAdrese.put(url, zahtjev);
    }

    /**
     * 
     * @return izgled ispisa
     */
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        addLine("Uspjesnih zahtjeva:\t", brojUspjesnihZahtjeva, sb);
        addLine("Neuspjesnih zahtjeva:\t", brojPrekinutihZahtjeva, sb);
        addLine("Ukupno zahtjeva:\t", ukupnoZahtjeva, sb);
        addLine("Ukupno trajanje rada dretvi:", 
                ParserHelper.formatMilisecondsToString(ukupnoTrajanjeRadaDretvi), sb);
        addLine("Naziv zadnje dretve:\t", nazivZadnjeDretve, sb);
        
        if(zahtjeviZaAdrese.size() > 0){
            sb.append("\n\r--------Zahtjevi za adrese----------\n\r");
            Object[] header = {"Kreirao", "Broj zahtjeva", "Url"};
            addLine(header, sb);
            for (ZahtjevAdresa zahtjev : zahtjeviZaAdrese.values()) {
                Object[] values = {
                    zahtjev.getKreator(), 
                    zahtjev.getBrojZahtjeva(), 
                    zahtjev.getUrl() 
                };

                addLine(values, sb);
            }
        }
        
        return sb.toString();
    }
    
    private void addLine(String title, Object value, StringBuilder sb){
        sb.append(title);
        sb.append("\t");
        sb.append(value);
        sb.append("\n\r");
    }
    
    private void addLine(Object[] values, StringBuilder sb){
        
        for (int i = 0; i < values.length; i++) {
            if(i > 0){
               sb.append("\t\t"); 
            }
             sb.append(values[i]);
        }
        
        sb.append("\n\r");
    }

    /**
     * Provjera da li postoji tražena adresa
     * @param url
     * @return 
     */
    public boolean postojiAdresa(String url) {
        return zahtjeviZaAdrese.containsKey(url);
    }
}
