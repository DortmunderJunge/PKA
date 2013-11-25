package webshop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import webshop.Artikel.*;
import webshop.IO.AccountdateiSerialisiert;
import webshop.IO.Direktzugriffsspeicher;
import webshop.Kunden.*;

/**
 * Schnittstelle zwischen View-/Controller und den Fachklassen
 * 
 * @author Scheben
 * 
 *         Aufgaben Verwaltet verfügbare Artikel, Accounts mit den zugehörigen
 *         Kundendaten, den am Webshop angemeldeten Kunden, den Einkaufswagen
 *         des angemeldeten Kunden
 * 
 *         Vereinfachende Annahmen: 
 *         - Liste der Accounts und Liste der Artikel werden mit festen Testdaten gefüllt 
 *         - Zu einer Zeit kann nur ein Kunde einkaufen 
 *         - Keine Vorauswahl von Kategorien möglich getArtikelliste() liefert ALLE Artikel
 * 
 */
public class Webshop {

	private String betreiber;

	/****************** Wahl adäquater Datenstrukturen ***********************/
	/**
	 * Im Webshop gespeicherte Accounts. Diese sind bereits erfasst. Neue kommen
	 * nicht hinzu. 
	 * Set statt List, da Accounts nicht mehrfach vorkommen dürfen.
	 */
	private Set<Account> accountListe = new HashSet<Account>();

	/**
	 * Im Webshop angebotene Artikel. Diese sind bereits erfasst. Neue kommen
	 * nicht hinzu. 
	 * SortedSet statt List: Artikel sollen sortiert sein und ein
	 * Artikel darf im Bestand nicht mehrfach vorkommen.
	 */
	private SortedSet<Artikel> artikelListe = new TreeSet<Artikel>();

	/**
	 * Im Webshop angemeldeter Kunde inkl. dessen Einkaufswagen Kunden können
	 * zunächst nur nacheinander einkaufen
	 */
	// Für später ggf. private HashMap<Kunde, Einkaufswagen> angemeldeteKunden =
	// new HashMap<Kunde, Einkaufswagen>();
	private Kunde aktuellerKunde;
	private Einkaufswagen aktuellerEinkaufswagen;
	AccountdateiSerialisiert file = new AccountdateiSerialisiert();
	
	public Webshop(String betreiber) throws NullPointerException, UngueltigerNameException, 
	                                        AccountException, UngueltigeBezeichnungException, 
	                                        UngueltigerPreisException 
	{
		this.betreiber = betreiber;
		fuelleAccountListe();
		fuelleArtikelbestand();
		
		// Test
		//show (accountListe);
		//show (artikelListe);
	
	}
	
	// Testausgabe um zu prüfen, dass keine doppelten Elemente in Listen/Mengen
	// Da diese Funktion nur zum Test da ist, nicht in UI verlagert
	private <E> void show (Set<E> liste) {
		System.out.println("\n******* Testausgabe *********");
		Iterator<E> it = liste.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	// ueb08 Initialisierung, falls noch keine Acccountdatei existiert
	/**
	 * Accountliste mit festen Testdaten füllen
	 * @throws UngueltigerNameException 
	 * @throws AccountException 
	 * @throws NullPointerException 
	 */
	private void initialisiereAccounts() throws UngueltigerNameException, NullPointerException, AccountException {
		Kunde kunde;
		Account account;
		kunde = new Kunde("Anton Meyer");
		kunde.setAdresse("München", 81245,"Schirmerweg 20");
		account = new Account("a.meyer@web.de", "derKluge", kunde);
		accountListe.add(account);

		kunde = new Kunde("Hansi Mueller");
		kunde.setAdresse("Kattendorf", 2358, "Dorfstr. 13");
		account = new Account("hansi.müller@web.de", "2H355sRe", kunde);
		accountListe.add(account);

		// Für Test; kurze Eingaben
		kunde = new Kunde("Ernst Otto");
		kunde.setAdresse("Aachen", 52062, "Hermannstraße 26");
		account = new Account("eo", "xx", kunde);
		accountListe.add(account);
		
		// Darf nicht nochmal gespeichert werden, da Emailadresse
		// bereits vergeben! Für Testzwecke
		kunde = new Kunde("Erich Oman");
		account = new Account("eo", "zz", kunde);
		accountListe.add(account);
	}
	
	// TODO ueb08
	private void fuelleAccountListe() throws NullPointerException, UngueltigerNameException, AccountException {
		// Gibt es bereits eine Datei mit serialisierten Accountdaten, 
		// sollen diese aus der Datei geladen werden.
		// Gibt es die Datei noch nicht, soll die Accountliste initialisiert werden
		// (siehe Methode initialisiereAccounts()).
		accountListe = file.deserialisiereAccounts();
		if (accountListe.size() == 0) {
			initialisiereAccounts();
		}
	}
	
	// TODO ueb08
	public void speichereAccounts() {
		// Alle Account-Objekte aus 'accountListe' zusammen mit den zugehörigen 
		// Kundendaten serialisieren und in einer Datei abspeichern.
		// Methode soll beim Schließen des Hauptfensters aufgerufen werden.
		file.serialisiereAccounts(accountListe);
	}
	
	/**
	 * Artikeldaten jetzt aus Datei lesen
	 * @throws UngueltigerPreisException 
	 * @throws UngueltigeBezeichnungException 
	 */
	private void fuelleArtikelbestand() throws UngueltigeBezeichnungException, UngueltigerPreisException {
		Direktzugriffsspeicher dzs = new Direktzugriffsspeicher();
		int maxSchluessel = dzs.getMaxSchluessel();
		for (int schluessel = 0; schluessel < maxSchluessel; schluessel++) {
			Artikel a = dzs.satzLesen(schluessel);
			if (a != null) {
				artikelListe.add(a);
			}
		}
		dzs.endeAnwendung();
	}	
	
	/**
	 * Liste der im Webshop verfügbaren Artikel zurückgeben
	 * 
	 * @return Artikelliste
	 */
	public List<Artikel> getArtikelliste() {
		LinkedList<Artikel> artikelLi = new LinkedList<Artikel>();
		Iterator<Artikel> it = artikelListe.iterator();
		while (it.hasNext()) {
			artikelLi.add(it.next());
		}
		return artikelLi;
	}

	/**
	 * Der zum Account gehörende Kunde wird ermittelt und im Attribut
	 * aktuellerKunde gespeichert. Zudem wird dem Kunden ein leerer
	 * Einkaufswagen zugeordnet
	 * 
	 * @param account
	 */
	public void anmelden(Account account) {
		Iterator<Account> it = accountListe.iterator();
		boolean found = false;
		Account ac = null;
		while (it.hasNext() && !found) {
			ac = it.next();
			found = ac.match(account);
		}
		if (found) {
			aktuellerKunde = ac.getKunde();
			aktuellerEinkaufswagen = new Einkaufswagen();
		}
	}

	/**
	 * Prüft, ob der Account dem Webshop bekannt ist, ob er also in der Liste
	 * der Accounts vorkommt
	 * 
	 * @param account
	 * @return true, wenn Account dem Webshop bekannt ist, false sonst
	 */
	public boolean istGueltig(Account account) {
		Iterator<Account> it = accountListe.iterator();
		boolean found = false;
		Account ac = null;
		while (it.hasNext() && !found) {
			ac = it.next();
			found = ac.match(account);
		}
		return found;
	}

	/**
	 * Einkaufswagen freigeben und aktuellen Kunden löschen
	 */
	public void abmelden() {
		aktuellerKunde = null;
		aktuellerEinkaufswagen = null;
	}

	public String getKundenName() {
		return aktuellerKunde.getName();
	}

	/**
	 * Gibt Liste der Artikel zurück, die sich im Einkaufswagen befinden
	 * 
	 * @return Artikel aus Einkaufswagen
	 */
	public List<Artikel> getInhaltEinkaufswagen() {
		return aktuellerEinkaufswagen.getInhalt();
	}

	public boolean einkaufswagenIstLeer() {
		return aktuellerEinkaufswagen.istLeer();
	}

	public void leereEinkaufswagen() {
		aktuellerEinkaufswagen.allesLoeschen();
	}

	/**
	 * Alle vom Kunden ausgewählten Artikel dem Einkaufswagen hinzufügen
	 * 
	 * @param ausgewaehlteArtikel
	 *            Liste der vom Kunden ausgewählten Artikel
	 */
	public void inEinkaufswagen(List<Artikel> ausgewaehlteArtikel) {
		for (Artikel artikel : ausgewaehlteArtikel) {
			aktuellerEinkaufswagen.einfuegen(artikel);
		}
	}

	/**
	 * Übergebenen Artikel aus Einkaufswagen nehmen
	 * 
	 * @param zuLoeschenderArtikel
	 */
	public void ausEinkaufswagenNehmen(Artikel zuLoeschenderArtikel) {
		aktuellerEinkaufswagen.loeschen(zuLoeschenderArtikel);
	}

	public String getBetreiber() {
		return betreiber;
	}

}
