# NWTIS Vježba 4. / Zadaća 1.

### Naziv: višedretveni sustav "Upravljanje IoT sustavima" putem socket-a
### Naziv projekta: {LDAP_korisničko_ime}_zadaca_1

Sve nove klase trebaju biti u paketu org.foi.nwtis.{LDAP_korisničko_ime}.zadaca_1. Za rad s postavkama treba koristiti Java biblioteku iz vjezba_03_2. Vlastite biblioteke treba smjestiti na direktorij .\lib unutar projekta. Klase i metode trebaju biti komentirane u javadoc formatu. Prije predavanja projekta potrebno je napraviti Clean na projektu. Zatim cijeli projekt (korijenski direktorij treba biti {LDAP_korisničko_ime}_zadaca_1) sažeti u .zip (NE .rar) format s nazivom {LDAP_korisničko_ime}_zadaca_1.zip i predati u Moodle. Uključiti izvorni kod, primjere datoteka konfiguracijskih podataka (.txt i .xml) i popunjeni obrazac za zadaću pod nazivom {LDAP_korisničko_ime}_zadaca_1.[doc | pdf] (u korijenskom direktoriju projekta).

#### Opis rada sustava:

Sustav ima dva odvojena dijela: poslužiteljski/serverski i korisnički. Shema sustava prikazana je na slici.

Shema sustava

Serverski dio ima naziv klase i parametare kod izvršavanja:

ServerSustava -konf datoteka{.txt | .xml | .bin} [-load]

Server na početku provjerava postoji li datoteka konfiguracije (opcija -konf) te prekida rad ako ne postoji. Ako postoji, učitava postavke iz datoteke konfiguracije. Ako je upisana opcija -load, provjerava postoji li datoteka sa serijaliziranim podacima (postavka evidDatoteka) te ju učitava ako postoji. Ako ne postoji ispisuje informaciju i nastavlja rad.

Upoznati se s klasom ServerSocket (http://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html).

Server uspostavlja početno stanje na temelju postavki. Server kreira i pokreće nadzornu dretvu, kreira i pokreće adresnu dretvu, kreira i pokreće rezervnu dretvu, a zatim kreira ServerSoket na zadanom portu iz postavki i čeka da se spoji korisnik. Nakon spajanja korisnika kreira se i starta radna dretva koja obrađuje zahtjev korisnika. U nazivu radne dretve mora biti LDAP korisničko ime autora, "-" i redni broj dretve (duljina 16 bitova, samo pozitivne vrijednosti)  koji se ne ponavlja osim ako dođe do okretanja vrijednosti tj. prijelaza preko najveće vrijednosti zadanog tipa podatka). Dretva iz dobivene veze na socketu preuzima tokove za ulazne i izlazne podatke prema korisniku. Na temelju ulaznih podataka provodi se analiza zahtjeva korisnika. Dozvoljene komande opisane su u posebnom dijelu. Ako sintaksa nije ispravna ili komanda nije dozvoljena tada se korisniku vraća odgovor ERROR 90; tekst (tekst objašnjava razlog pogreške). Kada dretva obavi traženu akciju ona ažurira podatke u evidenciji rada. Evidenciju rada sadrži podatke o ukupnom broju zahtjeva, broju uspješnih zahtjeva, broju prekinutih zahtjeva, broju zahtjeva s pojedine adrese s koje je poslan zahtjev, status pojedine adrese (URL) koju je korisnik(klijent) dodao, broju zahtjeva s pojedine adrese s koje je poslan zahtjevzadnji broj radne dretve, ukupno vrijeme rada radnih dretvi. Postupak ažuriranja mora se provoditi međusobno isključivo u odnosu na druge dretve koje upisuju podatke u evidenciju. Za podatke u evidenciji rada treba koristiti vlastitu klasu koja se može serijalizirati. Dretva nakon što upiše podatke u evidenciju rada provjerava treba li obaviti serijalizaciju. Ako je potrebno kreira  i pokreće dretvu za serijalizaciju podataka. Postupak serijalizacije mora se provoditi međusobno isključivo u odnosu na druge dretve koje upisuju podatke u evidenciju rada. Nakon što radna dretva obradi pridruženi zahtjev korisnika završava svoj rad.

Ukoliko nema raspoložive radne dretve, izvođenje se prebacuje na rezervnu dretvu koja vraća korisniku informaciju o nepostojanju slobodne radne dretve i zatim zatvara vezu na socketu s korisnikom. Slijedi ažuriranje podataka u evidenciji rada a po potrebi se provodi serijalizacija. Nakon toga rezervna dretva prelazi u čekanje (nema vremena spavanja kao kod ostalih dretvi jer dretva čeka dok će sljedeći puta biti potrebna). Nadzorna dretva radi u pravilnim ciklusima prema podacima iz postavki. U svakom ciklusu ispituje sve radne dretve i provjerava da li je vrijeme koje je potrošila pojedina dretva od početka spajanja korisnika dulje od maksimalno dopuštenog vremena. Ako je takav slučaj, nadzorna dretva javlja radnoj dretvi da odmah prekine obradu zahtjeva, ažurira podatke u evidenciji rada i da zatim zatvori vezu na socketu. Adresna dretva radi u pravilnim ciklusima prema podacima iz postavki. U svakom ciklusu ispituje sve zadane adrese od strane korisnika i provjerava postoji li pojedina adresa. Slijedi ažuriranje podataka u evidenciji rada. Treba pripremiti JUnit testove za minimalno 5 metoda kod serverskog dijela s različitih tvrdnjama i to za one metode koje nisu zadužene za kontrolu parametara.

U zadaći ne smiju se koristiti klase Timer, TimerTask i druge izvedene klase za upravljanje dretvama i sl! 

U radu programa koristi se datoteka s postavkama koja mora imali ekstenziju txt, xml ili bin a može biti lokalna, s apsolutnim ili relativnim nazivom ili na poslužitelju s internetskom adresom (npr. NWTiS_dkermek_zadaca_1.txt, E:\dkermek_zadaca_1.xml, http:\\localhost\zadaca_1.xml). Datoteka može sadržavati sljedeće elemente:

port - port na kojem radi server, između 8000 i 9999
intervalNadzorneDretve - broj milisekundi za ciklus nadzorne dretve
maksVrijemeRadneDretve - maksimalni broj milisekundi koliko može trajati radna dretva 
intervaAdresneDretve - broj milisekundi za ciklus adresne dretve
maksAdresa - maksimalni broj adresa za koje sadresna dretva može ispitivati postoje li  
maksBrojRadnihDretvi - maksimalan broj istovremenih radnih dretvi kod servera koje mogu opsluživati korisnike
brojZahtjevaZaSerijalizaciju - broj zahtjeva na serveru nakon kojeg se kreira i pokreće dretva za serijalizaciju podataka
evidDatoteka - datoteka u koju se provodi serijalizacija evidencije rada. Datoteka je lokalna, s apsolutnim ili relativnim nazivom (npr. evidencija.bin, d:\NWTiS\evidencija.bin,...)
adminDatoteka - datoteka s podacima o administratora sustava (korisničko ime; lozinka). Datoteka je lokalna, s apsolutnim ili relativnim nazivom (npr. evidencija.bin, d:\NWTiS\evidencija.bin,...).
Korisnički dio se sastoji od 3 dijela od kojih se samo jedan može odabrati kod pokretanja programa:

administrator (opcija -admin)
klijent (opcija -korisnik)
prikaz (opcija -prikaz).
Upoznati se s klasom Socket (http://docs.oracle.com/javase/8/docs/api/java/net/Socket.html). 

Korisnički program se može izvršavati više puta kako bi se koristili različiti dijelovi. Izvršavanje programa započinje utvrđivanjem vrste rada (admin, klijent, prikaz) na bazi opcija u parametarima. Komunikacija pojedinih vrsta korisnika (administrator, klijent, prikaz) i servera temelji se na jednostavnom protokolu koji ima određenu sintaksu, a sadrži skup komandi. Komande i njihova sintaksa objašnjeni su kod pojedine vrsta korisnika. Logično je da se prvo pokreće server, a nakon njega korisnici. Komunikaciju otvara jedna od vrsta korisnika tako da šalje zahtjev serveru u obliku određene komande. Server provodi analizu zahtjeva i ako je zahtjev u redu provodi određenu operaciju te vraća status operacije i ostale potrebne podatke. Kada zahtjev nije u redu vraća primjereni status operacije. Kontrola parametara treba se obaviti u posebnim funkcijama unutar klasa koje su zadužene za pojedine dijelove programa. Treba pripremiti JUnit testove za minimalno kod korisničkog dijelas različitih tvrdnjama i to za one metode koje nisu zadužene za kontrolu parametara.

Program se izvršava kao klasa i radi kao administrator servera ako zadovoljava sljedeći oblik parametara:

KorisnikSustava -admin -server [ipadresa | adresa] -port port -u korisnik -p lozinka [-pause | -start | -stop | -stat ]

Dozvoljene vrijednost za opcije:

ipadresa je adresa IPv4 (npr. 127.0.0.1, 192.168.15.1)
adresa je opisni naziv poslužitelja (npr. localhost, dkermek.nwtis.foi.hr)
port može biti u intervalu između 8000 i 9999.
korisnik  može sadržavati mala i velika slova, brojeve i znakove: _, -
lozinka  može sadržavati mala i velika slova, brojeve i znakove: _, -, #, !
Administrator servera spaja se na server putem socketa i šalje komandu serveru na temelju upisanih parametara i traži izvršavanja određene akcije:

USER korisnik; PASSWD lozinka; PAUSE;
upisan parametar -pause pa provjerava se postoji li korisnik i njemu pridružena lozinka u datoteci administratora (postavka adminDatoteka). Ako je u redu i server nije u stanju pause, privremeno prekida prijem svih komandi osim administratorskih. Korisniku se vraća odgovor OK.  Kada nije u redu, korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako je u stanju pause vraća se odgovor ERROR 01; tekst (tekst objašnjava razlog pogreške). 
USER korisnik; PASSWD lozinka; START;
upisan parametar -start pa provjerava se postoji li korisnik i njemu pridružena lozinka u datoteci administratora (postavka adminDatoteka). Ako je u redu i server je u stanju pause, nastavlja prijem svih komandi. Korisnku se vraća odgovor OK.  Kada nije u redu, korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako nije u stanju pause vraća se odgovor ERROR 02; tekst (tekst objašnjava razlog pogreške). 
USER korisnik; PASSWD lozinka; STOP;
upisan parametar -stop pa provjerava se postoji li korisnik i njemu pridružena lozinka u datoteci administratora (postavka adminDatoteka). Ako je u redu prekida prijem komandi, serijalizira evidenciju rada i završava rad. Korisniku se vraća odgovor OK.  Kada nije u redu, korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako nešto nije u redu s prekidom rada ili serijalizacijom vraća se odgovor ERROR 03; tekst (tekst objašnjava razlog pogreške). 
USER korisnik; PASSWD lozinka; STAT;
upisan parametar -stat pa provjerava se postoji li korisnik i njemu pridružena lozinka u datoteci administratora (postavka adminDatoteka). Ako je u redu korisniku se vraća odgovor OK; LENGTH nnnn<CRLF> i zatim vraća serijalizirane podatke o evidenciji rada. nnnn predstavlja broj byte-ova koje zauzima serijalizirana evidencija rada. Kada nije u redu, korisnik nije administrator ili lozinka ne odgovara, vraća se odgovor ERROR 00; tekst (tekst objašnjava razlog pogreške). Ako nešto nije u redu s evidencijom rada vraća se odgovor ERROR 04; tekst (tekst objašnjava razlog pogreške). Ako je evidencija rada u redu admninistrator ispisuje sve podatke u preglednom obliku.
Program se izvršava kao klasa i radi kao klijent servera ako zadovoljava sljedeći oblik parametara:

KorisnikSustava -korisnik -s [ipadresa | adresa] -port port -u korisnik [[-a | -t] URL] | [-w nnn]

Dozvoljene vrijednost za opcije:

ipadresa je adresa IPv4 (npr. 127.0.0.1, 192.168.15.1)
adresa je opisni naziv poslužitelja (npr. localhost, dkermek.nwtis.foi.hr)
port može biti u intervalu između 8000 i 9999.
URL je adresa web dokumenta (npr: http://arka.foi.hr, http://localhost/pero.html, http://www.foi.unizg.hr/sites/default/files/styles/front_page_slider/public/znanost_slajder_0.png)
nnn je broj sekundi koje čeka radna dretva, može biti u intervalu 1 do 600 
Klijent servera spaja se na server putem socketa i nakon toga šalje komandu serveru na temelju upisanih parametara i traži izvršavanja određene akcije:

USER korisnik; ADD adresa;  
upisan parametar -a URL . Dodaje adresu (URL) u popis adresa za koje se provjerava postoje li. Ako ne postoji adresa i ima slobodnog mjesta za adresu vraća mu se odgovor OK;  Ako ne postoji adresa ali nema slobodnog mjesta za adresu vraća mu se odgovor ERROR 10; tekst (tekst objašnjava razlog pogreške). Ako već postoji adresa vraća mu se odgovor ERROR 11; tekst (tekst objašnjava razlog pogreške)
USER korisnik; TEST adresa;  
upisan parametar -t URL . Vraća status zadnje provjere zadane adrese (URL). Ako postoji adresa vraća mu se odgovor OK;  {YES | NO}; Ako ne postoji adresa vraća mu se odgovor ERROR 12; tekst (tekst objašnjava razlog pogreške). 
USER korisnik; WAIT nnnnn;  
upisan parametar -w nnn . Radna dretva treba čekati zadani broj sekundi pretvorenih u milisekunde. Ako je uspješno odradila čekanje vraća mu se odgovor OK; Ako nije uspjela odraditi čekanje vraća mu se odgovor ERROR 13; tekst (tekst objašnjava razlog pogreške). 
Program se izvršava kao klasa i radi kao formatirani prikaz serijaliziranih podataka evidencije servera ako zadovoljava sljedeći oblik parametara:

KorisnikSustava -prikaz -s datoteka

Dozvoljene vrijednost za opcije:

datoteka u koju je provedena serijalizacija podataka evidencije o radu, može biti lokalna, s apsolutnim ili relativnim nazivom, ili na poslužitelju s internetskom adresom (npr. evidencija.bin, d:\NWTiS\radKorisnika.bin, http:\\localhost\evidencija.bin). 
Provodi deserijalizaciju podataka evidencije rada i ispisuje sve podatke iz evidencije rada u preglednom obliku.

Za pretvaranje serjializiraniih podataka iz evidencije u čitljiv i formatirani oblik. Za formatiranje:

datumskih podataka preporučuje se klasa java.text.SimpleDateFormat (http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
brojčanih podataka preporučuje se klasa java.text.DecimalFormat (http://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html).
Zadaća_1: višedretveni sustav upravljanja putem socket-a
U NetBeans kreiranje projekta {LDAP_korisničko_ime}_zadaca_1 kao Java aplikacije, na direktorij {LDAP_korisničko_ime}, bez kreiranja glavne klase i kao glavni projekt
Kreiranje paketa org.foi.nwtis.{LDAP_korisnik}.zadaca_1
Kreirati direktorij .\lib u korijenskom direktoriju projekta
Kopirati Java biblioteku iz prethodnog zadatka (vjezba_03_2.jar) u direktorij .\lib
Dodavanje Java biblioteke iz prethodnog zadatka: desna tipka na mišu na Libraries / Add Jar/Forder / Odabrati .\lib\vjezba_03_2.jar
Java RegEx tutorial http://docs.oracle.com/javase/tutorial/essential/regex/
Korisni primjeri za RegEx http://www.mkyong.com/regular-expressions/10-java-regular-expression-examples-you-should-know/
Kreiranje klase ServerSustava. Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. Učitavaju se postavke iz datoteke. Server kreira i pokreće nadzornu dretvu (klasa NadzorDretvi), kreira i pokreće adresnu dretvu (klasa ProvjeraAdresa), kreira i pokreće rezervnu dretvu (klasa RezervnaDretva), kreira i pokreće dretvu za serijalizaciju evidencije (klasa SerijalizatorEvidencije). Otvara se ServerSocket (slično primjerima ClientTester.java i TinyHttpd.java s 4. predavanja) na izabranom portu i čeka zahtjev korisnika u beskonačnoj petlji. Kada se korisnik spoji na otvorenu vezu, kreira se objekt dretve klase RadnaDretva, veza se predaje objektu i pokreće se izvršavanje dretve. Dretve opslužuju zahtjev korisnika. Dretva nakon što obradi pridruženi zahtjev korisnika završava svoj rad i briše se.  Ako nema raspoložive radne dretve, izvođenje se prebacuje na rezervnu dretvu koja vraća korisniku informaciju o nepostojanju slobodne radne dretve tako da korisniku vraća odgovor ERROR 20; tekst (tekst objašnjava razlog pogreške). Nakon toga server ponovno čeka na uspostavljanje veze i postupak se nastavlja. 
Kreiranje klase RadnaDretva  kao dretve. Kreiranje konstruktora klase i metode za prijenos potrebnih podataka. Dretva iz dobivene veze na socketu preuzima tokove za ulazne i izlazne podatke prema korisniku. Dretva preuzima podatke koje šalje korisnik putem ulaznog toka podataka, provjerava korektnost komandi iz zahtjeva. Preporučuje se koristiti dopuštene izraze. Za prvo testiranje servera može se koristiti primjer s 4. predavanja Primjer33_3.java. Na kraju dretva šalje podatke korisniku putem izlaznog toka podataka. Za svaku vrstu komande kreira se posebna metoda koja odrađuje njenu funkcionalnost.
Kreiranje klase Evidencija. Služi za evidenciju podataka i može se serijalizirati.Treba odrediti dodatne klase i varijable u koje će se pridružiti vrijednosti.  Potrebno je voditi brigu o međusobnom isključivanju dretvi kod pristupa evidenciji rada i sl.
Kreiranje klase SerijalizatorEvidencije kao dretve. Kreira se konstruktor klase u koji se prenose podaci konfiguracije. Služi za serijalizaciju podataka. Izvršava serijalizaciju evidencije prema potrebi, a u međuvremenu čeka. Potrebno je voditi brigu o međusobnom isključivanju dretvi kod pristupa evidenciji rada i sl.
Kreiranje klase NadzorDretvi kao dretve. Kreira se konstruktor klase u koji se prenose podaci konfiguracije. Služi za nadzor radnih dretvi u pravilnim vremenskim ciklusima. Potrebno je voditi brigu o međusobnom isključivanju dretvi kod pristupa evidenciji rada i sl.
Kreiranje klase ProvjeraAdresa kao dretve. Kreira se konstruktor klase u koji se prenose podaci konfiguracije. Služi za provjeru zadanih adresa u pravilnim vremenskim ciklusima. Potrebno je voditi brigu o međusobnom isključivanju dretvi kod pristupa evidenciji rada i sl.
Kreiranje klase RezervnaDretva kao dretve. Kreira se konstruktor klase u koji se prenose podaci konfiguracije. Služi za slučaj kada nema slobodne radne dretve kako bi se moglo javiti korisniku. Ona ne završava svoj nego čega do sljedećeg poziva. Potrebno je voditi brigu o međusobnom isključivanju dretvi kod pristupa evidenciji rada i sl.
Kreiranje klase KorisnikSustava. Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. Na temelju opcije kreira se objekt potrebne klase AdministratorSustava, KlijentSustava ili PregledSustava, te se nastavlja s izvršavanjem tog objekta.
Kreiranje klase AdministratorSustava. Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. Objekt klase spaja se na server i šalje komandu(e) u zahtjevu. Primljeni odgovori se ispisuju na ekranu korisnika. Za svaku vrstu opcija kreira se posebna metoda koja odrađuje njenu funkcionalnost.
Kreiranje klase KlijentSustava. Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. Spaja na server i nakon toga šalje komandu serveru putem socketa  i traži izvršavanja određene akcije. Primljeni odgovor se ispisuju na ekranu korisnika.
Kreiranje klase PregledSustava. Prvo se provjeravaju upisane opcije, preporučuje se koristiti dopuštene izraze. Otvara i čita datoteku sa serijaliziranim podacima evidencije i ispisuje ih u prikladnom/čitljivom i formatiranom obliku na ekran/standardni izlaz korisnika.
Kod prvog pokretanja programa s osobinom servera javlja se Sigurnosna stijena (Firewall) s pitanjem o blokiranju ili dozvoli rada programa. Potrebno je dozvoliti rad programu (Java SE...). Drugi način je da to uradimo unaprijed putem postavki veze koju koristimo (LAN. wireless) za Advanced, Settings, u kojima dodamo port (Add Port) koji će biti otvoren (slika).
Postavke za otvaranje porta
