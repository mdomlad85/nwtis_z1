package org.foi.nwtis.mdomladov.zadaca_1;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.Konfiguracija;

/**
 * Kreira se konstruktor klase u koji se prenose podaci konfiguracije. 
 * Služi za slucaj kada nema slobodne radne dretve kako bi se moglo javiti korisniku. 
 * Ona ne zavrsava svoj nego ceka do sljedećeg poziva.
 * 
 * @author Marko Domladovac
 */
class RezervnaDretva extends KonfDretva {
    
    public Socket socket;
    
    volatile boolean rezervaPotrebna;

    public RezervnaDretva(Konfiguracija konf) {
        super(konf);
        super.setName(NaziviDretvi.REZERVNA_DRETVA);
    } 

    @Override
    public void run() {
        super.run();
        try{
            while (true) {
                synchronized (this) {
                    while (!rezervaPotrebna){                            
                        wait();
                    }
                    System.out.println(this.getClass());
                    try (OutputStream os = socket.getOutputStream()) { 
                        os.write("ERROR 20; Nema vise slobodnih dretvi".getBytes());
                        os.flush();
                        socket.shutdownOutput();
                    } catch (IOException ex) {
                        Logger.getLogger(RadnaDretva.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    rezervaPotrebna = false;
                }
            } 
        } catch (InterruptedException ex) {
            System.err.println("Rezervna dretva je prekinuta: " + ex.getMessage());
        }
       
    }   
}