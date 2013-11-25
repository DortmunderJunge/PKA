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
	private final int SATZNRMAX = 1000; // Index hat nur Platz f�r 1000 Eintr�ge
	// Attribute f�r die Datenhaltung
	private Artikelindex derIndex; // Verwendung der Indexverwaltung
	private Artikeldatei dieDatei; // Verwendung der Dateiverwaltung
	
	// ueb07: Pfad- und Dateinamen setzen
	private String pfadname = "c:/Webshop/Daten/";
	private String dateinameArtikel = "Artikelbestand.dat"; // Dateiname
	private String dateinameIndex = "ArtikelIndex.idx";

	// ueb07: Vor dem �ffnen zun�chst Pfad 
	// f�r die Dateien anlegen
	// Kontrollieren Sie, ob die Dateien im
	// richtigen Verzeichnis angelegt werden
	public Direktzugriffsspeicher() {
		File pfad = new File (pfadname);
		pfad.mkdirs();
		oeffneDateien();
	}

	// Operationen
	
	// Gibt maximal erlaubten Schl�sselwert zur�ck.
	// Entspricht dem Maximum der m�glichen Eintr�ge
	// im Index (Schl�sselwert wird als Index im Array benutzt)
	public int getMaxSchluessel() {
		return derIndex.MAX;
	}
	
	// �berschreibt den durch schluessel identifizierten Datensatz mit
	// den �bergebenen Artikeldaten. Gibt es zum Schl�ssel keinen
	// Eintrag in der Indexdatei, so wird neuer Datensatz am 
	// Ende der Datei angef�gt.
	public void satzSchreiben(int schluessel, Artikel einSatz) {
		schreibeSatz(einSatz, schluessel);
	}

	// Liest durch schluessel identifizierte Artikeldaten und gibt diese zur�ck.
	// Gibt es zum �bergebenen Schl�ssel keinen Eintrag in der Indexdatei, 
	// so wird null zur�ckgegeben.
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

	
	// Private Operationen f�r die Datenhaltung
	
	// ueb07 Artikel und Indexdatei �ffnen und ggf. anlegen
	//
	// Verwenden die Operationen der Klassen Datei und Index
	private void oeffneDateien() {
		derIndex = new Artikelindex(pfadname + dateinameIndex);
		try {
			// Indexdatei �ffnen und Indextabelle laden
			derIndex.ladeIndexDatei();
		} catch (IOException e) {
			try {
				derIndex.speichereIndexDatei();
			} catch (IOException e1) {
			}
			System.out.println("Indexdatei nicht vorhanden \n");
		}
		// Datei �ffnen
		dieDatei = new Artikeldatei(pfadname + dateinameArtikel);
	}

	private void schliesseDateien() {
		if (derIndex != null) {
			try {
				// Indexdatei speichern
				derIndex.speichereIndexDatei();
			} catch (IOException e) {
				System.out.println("Probleme beim Schlie�en der Indexdatei "
						+ e + "\n");
			}
			// Schlie�en der Datei
			if (dieDatei != null)
				dieDatei.schliesseDatei();
		}
	}

	// Gibt es in der Indexdatei bereits einen Wert zu dem Schl�ssel,
	// wird der zugeh�rige Datensatz �berschrieben. Ansonsten wird
	// der Datensatz ans Ende der Datei angef�gt und ein passender
	// Indexeintrag vorgenommen.
	private void schreibeSatz(Artikel einSatz, int schluessel) {

		// Gibt es in Indexdatei bereits einen Eintrag zum Schl�ssel 
		// (index >=0)?
		int index = derIndex.gibIndexZuSchluessel(schluessel);
		System.out.println("Indextabelle: Index " + index + " zu Schluessel "
				+ schluessel);
		if (index < 0) {
			// Anzahl Datens�tze holen, daraus neuen Index berechnen
			index = dieDatei.gibAnzahlDatensaetze();
			System.out
					.println("schreibeSatz: (Neuer) Index (=AnzahlDatens�tze) "
							+ index + " f�r Datensatz mit Schl�ssel "
							+ schluessel + "\n");
			// Schreibt einen Datensatz f�r den Schl�ssel in die Indexdatei
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
	// Hier�ber Artikeldatei mit Daten f�llen:
	// Artikel vom Typ TabletPC und Buch erzeugen und 
	// unter der Artikelnummer als Schl�ssel
	// mithilfe der Methoden der Klasse Direktzugriffsdatei 
	// abspeichern
	//
	public static void main(String args[]) {
		Direktzugriffsspeicher dzs = new Direktzugriffsspeicher();
		try {
			// Hier gibt der Schl�ssel die Artikelnummer an
			dzs.satzSchreiben(8, new TabletPC(8, 118.56, "Acer Iconia B1"));
			dzs.satzSchreiben(2, new TabletPC(2, 159.99, "Kindle Fire"));
			dzs.satzSchreiben(5, new TabletPC(5, 265.58, "Leonovo"));
			dzs.satzSchreiben(1, new Buch(1, 39.25, "Bl�tenzauber", "Berta Kruse",
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
