package webshop.Artikel;

public abstract class Artikel implements Comparable<Artikel> {

	private static int anzArtikel = 0;
	private int artikelnummer;
	private Kategorie kategorie;
	private String bezeichnung;
	private double preis;

	public Artikel(Kategorie kategorie, String bezeichnung, double preis)
			throws UngueltigeBezeichnungException, UngueltigerPreisException {
		artikelnummer = ++anzArtikel;
		this.kategorie = kategorie;
		if (bezeichnung == null || bezeichnung.equals(""))
			throw new UngueltigeBezeichnungException(
					"Keine Bezeichnung für Artikel Nr. " + artikelnummer + " angegeben");
		this.bezeichnung = bezeichnung;
		setPreis(preis);
	}
	
	// Wenn Objekt aus Datei ausgelesen wird, gibt es schon eine Artikelnummer.
	// Die muss dann übernommen werden.
	public Artikel(int artikelnummer, Kategorie kategorie, String bezeichnung, double preis)
			throws UngueltigerPreisException, UngueltigeBezeichnungException {
		this(kategorie, bezeichnung, preis);
		this.artikelnummer = artikelnummer;
	}

	public int getArtikelnummer() {
		return artikelnummer;
	}

	public Kategorie getKategorie() {
		return kategorie;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) throws UngueltigerPreisException {
		if (preis <= 0.0)
			throw new UngueltigerPreisException("Für Artikel Nr. " + artikelnummer + 
					" mit der Bezeichnung '"+ bezeichnung + "' wurde ein Preis <= 0 angegeben");
		this.preis = preis;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) throws UngueltigeBezeichnungException {
		if (bezeichnung == null || bezeichnung.equals(""))
			throw new UngueltigeBezeichnungException("Für Artikel Nr. " + artikelnummer + " wurde keine Bezeichnung angegeben");
		else
			this.bezeichnung = bezeichnung;
	}

	// Um Artikel nach ihrer Kategorie sortieren zu können,
	// muss die compareTo-Methode implementiert werden
	// Als zweites Sortierkriterium gilt die Artikelnummer
	/**
	 * Implementierung der Schnittstelle Comparable, um Artikel nach ihrer
	 * Kategorie sortieren zu können
	 * 
	 * @param a
	 * @return erg erg = 0 entspricht gleich, erg > 0 entspricht this > a, erg <
	 *         0 entspricht this < a
	 */
	public int compareTo(Artikel a) {
		if (a == null)
			throw new NullPointerException("Zu vergleichender Artikel ist null");
		int erg = this.kategorie.compareTo(a.getKategorie());
		if (erg == 0) // Sortierung nach Artikelnummer vornehmen
			erg = this.artikelnummer - a.getArtikelnummer();
		return erg;
	}

	public String toString() {
		String st = "";
		st += "Kategorie : " + kategorie + ", ";
		st += "ArtikelNr.: " + artikelnummer + ", ";
		st += "Bezeichnung : " + bezeichnung + ", ";
		st += "Preis : " + preis;
		return st;	
	}

}
