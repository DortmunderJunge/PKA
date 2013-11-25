package webshop.Kunden;

import java.io.Serializable;

// ueb08: Serializable implementieren
public class Account implements Serializable {
	
	private Kunde kunde = null;
	private String emailAdresse = null;
	private String passwort = null;
	
	public Account(String emailAdresse, String passwort) throws AccountException {
		// Es könnte auch auf weitergehende Bedingungen geprüft werden wie z.B. 
		// ob in Emailadresse ein @ enthalten ist o.ä.
		// Bei Passwörtern könnte auf eine Mindestlänge, Mix von Buchstaben und Ziffern etc. geprüft werden
		if (emailAdresse.equals("")) throw new AccountException("Ungueltige Emailadresse");
		this.emailAdresse = emailAdresse;
		if (passwort.equals("")) throw new AccountException("Ungueltiges Passwort");
		this.passwort = passwort;
	}
	
	public Account(String emailAdresse, String passwort, Kunde kunde) throws NullPointerException, AccountException {
		this(emailAdresse, passwort);
		if (kunde == null) throw new NullPointerException("Beim Anlegen eines Accounts wurde kein gueltiges Kundenobjekt übergeben");
		this.kunde = kunde;
	} 
	
	public Kunde getKunde() {
		return kunde;
	}
	
	public String getEmailAdresse() {
		return emailAdresse;
	}
	
	public void setEmailAdresse(String emailAdresse) throws AccountException {
		if (emailAdresse.equals("")) throw new AccountException("Emailadresse ändern: Ungueltige Emailadresse");
		this.emailAdresse = emailAdresse;
	}
	
	public String getPasswort() {
		return passwort;
	}
	
	public void setPasswort(String passwort) throws AccountException {
		if (passwort.equals("")) throw new AccountException("Passwort ändern: Ungueltiges Passwort");
		this.passwort = passwort;
	}
	
	// Um in der Menge der Accounts über "match" das Objekt
	// mit dem passenden Account zu finden, über das der zugehörige
	// Kunde ermittelt werden kann
	public boolean match(Account k) {
		String st = emailAdresse + passwort;
		String kst = ((Account) k).emailAdresse + ((Account) k).passwort;
		return st.equals(kst);
	}

	// Um zu gewährleisten, dass keine 2 Accounts mit derselben
	// Emailadresse gespeichert werden
	public boolean equals(Object k) {
		if (k == null)
			return false;
		if (k instanceof Account) {
			String st = emailAdresse;
			String kst = ((Account) k).emailAdresse;
			return st.equals(kst);
		} else
			return false;
	}

	// Accounts werden in HashSet gespeichert,
	// also hashCode nötig. Dieser muss für inhaltsgleiche
	// Objekte denselben Wert ergeben
	public int hashCode() {
		String st = emailAdresse;
		return st.hashCode();
	}

	public String toString() {
		return "Emailadresse = " + emailAdresse + ", Passwort = " + passwort;
	}

}
