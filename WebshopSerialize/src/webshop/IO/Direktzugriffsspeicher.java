package webshop.IO;

import java.io.File;
import java.io.IOException;

import webshop.Artikel.Artikel;
import webshop.Artikel.Buch;
import webshop.Artikel.TabletPC;
import webshop.Artikel.UngueltigeBezeichnungException;
import webshop.Artikel.UngueltigerPreisException;

public class Direktzugriffsspeicher {
	// Attribut
	private final int SATZNRMAX = 1000; // Index hat nur Platz für 1000 Einträge
	// Attribute für die Datenhaltung
	private Artikelindex derIndex; // Verwendung der Indexverwaltung
	private Artikeldatei dieDatei; // Verwendung der Dateiverwaltung
	
	// ueb07: Pfad- und Dateinamen setzen
	private String pfadname = "c:/Webshop/Daten/";
	private String dateinameArtikel = "Artikelbestand.dat"; // Dateiname
	private String dateinameIndex = "ArtikelIndex.idx";

	// ueb07: Vor dem Öffnen zunächst Pfad 
	// für die Dateien anlegen
	// Kontrollieren Sie, ob die Dateien im
	// richtigen Verzeichnis angelegt werden
	public Direktzugriffsspeicher() {
		File pfad = new File (pfadname);
		pfad.mkdirs();
		oeffneDateien();
	}

	// Operationen
	
	// Gibt maximal erlaubten Schlüsselwert zurück.
	// Entspricht dem Maximum der möglichen Einträge
	// im Index (Schlüsselwert wird als Index im Array benutzt)
	public int getMaxSchluessel() {
		return derIndex.MAX;
	}
	
	// Überschreibt den durch schluessel identifizierten Datensatz mit
	// den übergebenen Artikeldaten. Gibt es zum Schlüssel keinen
	// Eintrag in der Indexdatei, so wird neuer Datensatz am 
	// Ende der Datei angefügt.
	public void satzSchreiben(int schluessel, Artikel einSatz) {
		schreibeSatz(einSatz, schluessel);
	}

	// Liest durch schluessel identifizierte Artikeldaten und gibt diese zurück.
	// Gibt es zum übergebenen Schlüssel keinen Eintrag in der Indexdatei, 
	// so wird null zurückgegeben.
	public Artikel satzLesen(int schluessel) throws UngueltigerPreisException, 
	                                             UngueltigeBezeichnungException {
		Artikel einSatz = null;
		int index = derIndex.gibIndexZuSchluessel(schluessel);
		if (index != -1)// Ein Datensatz-Index ist vorhanden
		{
			try {
				einSatz = dieDatei.leseSatz(index);
			} catch (IOException e) {
				System.out.println("Direktzugriffsspeicher:satzLesen: "
						+ "Kein Satz vorhanden: Index = " + index);
			}
		}
		return einSatz;
	}

	public void endeAnwendung() {
		schliesseDateien();
	}

	
	// Private Operationen für die Datenhaltung
	
	// ueb07 Artikel und Indexdatei öffnen und ggf. anlegen
	//
	// Verwenden die Operationen der Klassen Datei und Index
	private void oeffneDateien() {
		derIndex = new Artikelindex(pfadname + dateinameIndex);
		try {
			// Indexdatei öffnen und Indextabelle laden
			derIndex.ladeIndexDatei();
		} catch (IOException e) {
			try {
				derIndex.speichereIndexDatei();
			} catch (IOException e1) {
			}
			System.out.println("Indexdatei nicht vorhanden \n");
		}
		// Datei öffnen
		dieDatei = new Artikeldatei(pfadname + dateinameArtikel);
	}

	private void schliesseDateien() {
		if (derIndex != null) {
			try {
				// Indexdatei speichern
				derIndex.speichereIndexDatei();
			} catch (IOException e) {
				System.out.println("Probleme beim Schließen der Indexdatei "
						+ e + "\n");
			}
			// Schließen der Datei
			if (dieDatei != null)
				dieDatei.schliesseDatei();
		}
	}

	// Gibt es in der Indexdatei bereits einen Wert zu dem Schlüssel,
	// wird der zugehörige Datensatz überschrieben. Ansonsten wird
	// der Datensatz ans Ende der Datei angefügt und ein passender
	// Indexeintrag vorgenommen.
	private void schreibeSatz(Artikel einSatz, int schluessel) {

		// Gibt es in Indexdatei bereits einen Eintrag zum Schlüssel 
		// (index >=0)?
		int index = derIndex.gibIndexZuSchluessel(schluessel);
		System.out.println("Indextabelle: Index " + index + " zu Schluessel "
				+ schluessel);
		if (index < 0) {
			// Anzahl Datensätze holen, daraus neuen Index berechnen
			index = dieDatei.gibAnzahlDatensaetze();
			System.out
					.println("schreibeSatz: (Neuer) Index (=AnzahlDatensätze) "
							+ index + " für Datensatz mit Schlüssel "
							+ schluessel + "\n");
			// Schreibt einen Datensatz für den Schlüssel in die Indexdatei
			try {
				derIndex.erzeugeEintrag(schluessel, index);
			} catch (IOException e) {
				System.out
						.println("Direktzugriffsspeicher: Fehler bei erzeugeEintrag: "
								+ e + "\n");
			}
		}
		// Speichert den Satz in der Datei
		try {
			dieDatei.speichereSatz(einSatz, index);
		} catch (IOException e) {
		}
	}

	/******** Hauptprogramm ***********/
/*
	// ueb07  
	// Hierüber Artikeldatei mit Daten füllen:
	// Artikel vom Typ TabletPC und Buch erzeugen und 
	// unter der Artikelnummer als Schlüssel
	// mithilfe der Methoden der Klasse Direktzugriffsdatei 
	// abspeichern
	//
	public static void main(String args[]) {
		Direktzugriffsspeicher dzs = new Direktzugriffsspeicher();
		try {
			// Hier gibt der Schlüssel die Artikelnummer an
			dzs.satzSchreiben(8, new TabletPC(8, 118.56, "Acer Iconia B1"));
			dzs.satzSchreiben(2, new TabletPC(2, 159.99, "Kindle Fire"));
			dzs.satzSchreiben(5, new TabletPC(5, 265.58, "Leonovo"));
			dzs.satzSchreiben(1, new Buch(1, 39.25, "Blütenzauber", "Berta Kruse",
					"Addison Wesley"));
			dzs.satzSchreiben(7, new Buch(7, 22.45, "Der goldene Drache", "Berta Kruse",
					"Addison Wesley"));

			System.out.println();
			System.out.println(dzs.satzLesen(1));
			System.out.println(dzs.satzLesen(2));
			System.out.println(dzs.satzLesen(5));
			System.out.println(dzs.satzLesen(7));
			System.out.println(dzs.satzLesen(8));

			System.out.println();
			dzs.satzSchreiben(4, new TabletPC(4, 300.15, "Leonovo 2"));
	
			System.out.println(dzs.satzLesen(1));
			System.out.println(dzs.satzLesen(2));
			System.out.println(dzs.satzLesen(4));
			System.out.println(dzs.satzLesen(5));
			System.out.println(dzs.satzLesen(7));
			System.out.println(dzs.satzLesen(8));
		} catch (UngueltigerPreisException e) {
			// TODO
			e.printStackTrace();
		} catch (UngueltigeBezeichnungException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dzs.endeAnwendung();
	}
*/
}
