package webshop.Artikel;

public class Buch extends Artikel {
	
	private String titel;
	private String autoren;
	private String verlag;

	public Buch (double preis, String titel, String autoren, String verlag) throws UngueltigeBezeichnungException, UngueltigerPreisException {
		super (Kategorie.Buch, titel, preis);
		this.titel = titel;
		this.autoren = autoren;
		this.verlag = verlag;
	}
	
	// Wenn Objekt aus Datei ausgelesen wird, gibt es schon eine Artikelnummer.
	// Die muss dann übernommen werden.
	public Buch (int artikelnummer, double preis, String titel, String autoren, String verlag) throws UngueltigerPreisException, UngueltigeBezeichnungException {
		super (artikelnummer, Kategorie.Buch, titel, preis);
		this.titel = titel;
		this.autoren = autoren;
		this.verlag = verlag;
	}

	public String getTitel() {
		return titel;
	}

	public String getAutoren() {
		return autoren;
	}

	public String getVerlag() {
		return verlag;
	}
	
	public String toString() {
		String st = super.toString() + ", "; // Titel identisch mit Bezeichnung
		st += "Autoren : " + autoren + ", ";
		st += "Verlag : " + verlag;
		return st;
	}

}
