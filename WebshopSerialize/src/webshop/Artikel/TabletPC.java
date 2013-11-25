package webshop.Artikel;

public class TabletPC extends Artikel {

	public TabletPC (double preis, String bezeichnung) throws UngueltigeBezeichnungException, UngueltigerPreisException {
		super (Kategorie.TabletPC, bezeichnung, preis);
	}	
	
	// Wenn Objekt aus Datei ausgelesen wird, gibt es schon eine Artikelnummer.
	// Die muss dann übernommen werden.
	public TabletPC (int artikelnummer, double preis, String bezeichnung) throws UngueltigerPreisException, UngueltigeBezeichnungException {
		super (artikelnummer, Kategorie.TabletPC, bezeichnung, preis);
	}

}

