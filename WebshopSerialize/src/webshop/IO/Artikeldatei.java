package webshop.IO;

import java.io.*;

import webshop.Artikel.Artikel;
import webshop.Artikel.Buch;
import webshop.Artikel.Kategorie;
import webshop.Artikel.TabletPC;
import webshop.Artikel.UngueltigeBezeichnungException;
import webshop.Artikel.UngueltigerPreisException;

public class Artikeldatei {
	private RandomAccessFile dieArtikeldatei;
	private int aktuellePosition; // aktuelle Position des Dateizeigers
	
	// Alle vorkommenden Strings (Bezeichnung, Titel, Autoren, Verlag) 
	// sind maximal 30 Zeichen lang
	private final int STRINGLAENGE = 30;
	
	// artikelnummer: 4 Byte, Ordnungszahl kategorie: 4 Byte, preis: 8 Byte
	// maximal 4 Strings f�r Bezeichnung, Titel, Autoren, Verlag
	private final int SATZLAENGE = 4 + 4 + (4 * STRINGLAENGE) * 2 + 8;

	// Konstruktor
	public Artikeldatei(String dateiname) {
		oeffneDatei(dateiname);
	}

	/******* Operationen *******/

	// ueb07: Alle Daten eine Artikels hintereinander abspeichern
	// Artikelnummer, Ordnungszahl der Kategorie (kategorie.ordinal()), 
	// Bezeichnung (30 Zeichen lang), Preis
	// Falls es sich um ein Buch handelt, auch noch Titel, Autoren 
	// und Verlag abspeichern
	// 
	// Speichert einen Datensatz satz an der Position index in der Datei
	public void speichereSatz(Artikel satz, int index) throws IOException {
		if (dieArtikeldatei != null) {
			positioniereAufSatz(index); // interne Hilfsoperation
			//System.out.println("FilePointer vor Schreiben: " + 
			//                                dieArtikeldatei.getFilePointer());
			dieArtikeldatei.writeInt(satz.getArtikelnummer());
			dieArtikeldatei.writeInt(satz.getKategorie().ordinal());
			speichereFesteAnzahlZeichen(satz.getBezeichnung(), STRINGLAENGE);
			dieArtikeldatei.writeDouble(satz.getPreis());
			if (satz.getKategorie() == Kategorie.Buch){
				Buch buch = (Buch)satz;	
				speichereFesteAnzahlZeichen(buch.getTitel(), STRINGLAENGE);
				speichereFesteAnzahlZeichen(buch.getAutoren(), STRINGLAENGE);
				speichereFesteAnzahlZeichen(buch.getVerlag(), STRINGLAENGE);
			}
			else {
				for (int i = 0; i < 3; i++)
				    speichereFesteAnzahlZeichen("", STRINGLAENGE);
			}			
			//System.out.println("FilePointer NACH Schreiben: " + 
			//                                    dieArtikeldatei.getFilePointer());
		}
	}

	// ueb07: Alle Daten eine Artikels nacheinander lesen
	// Artikelnummer, Ordnungszahl der Kategorie, 
	// Bezeichnung (30 Zeichen lang), Preis
	// Falls Ordnungszahl der Kategorie gleich der Ordnungszahl von 
	// Kategorie.Buch ist, auch noch Titel, Autoren und Verlag auslesen
	// Aus diesen Daten passendes Artikelobjekt erzeugen und zur�ckgeben
	//
	// Liest den �ber index identifizierten Datensatz aus der Datei
	// und gibt ihn als Artikel zur�ck
	public Artikel leseSatz(int index) throws IOException, 
	                            UngueltigerPreisException, 
	                            UngueltigeBezeichnungException {
		Artikel a = null;
		if (dieArtikeldatei != null) {
			positioniereAufSatz(index); // interne Hilfsoperation
			int artikelnummer = dieArtikeldatei.readInt();
			int ordinal = dieArtikeldatei.readInt();
			String bezeichnung = leseFesteAnzahlZeichen(STRINGLAENGE);// interne Hilfsoperation
			double preis = dieArtikeldatei.readDouble();
			
			// Aus Ordinalzahl m�sste jetzt Kategorie ermittelt werden,
			// um zu wissen, zu welcher Subklasse der zu erzeugende Artikel geh�rt.
			// Da wir aber nur TabletPCs unterst�tzen, ist das hier �berfl�ssig
			if (ordinal == Kategorie.TabletPC.ordinal())
				a = new TabletPC(artikelnummer, preis, bezeichnung);
			else {
				//Zus�tzliche Daten f�r Buch lesen:
				String titel = leseFesteAnzahlZeichen(STRINGLAENGE);
				String autoren = leseFesteAnzahlZeichen(STRINGLAENGE);
				String verlag = leseFesteAnzahlZeichen(STRINGLAENGE);
				a = new Buch(artikelnummer, preis, titel, autoren, verlag);
			}
		} 
		return a;
	}

	// �ffnen der Datei zum Lesen und Schreiben
	public void oeffneDatei(String name) {
		try {
			dieArtikeldatei = new RandomAccessFile(name, "rw");
		} catch (IOException e) {
			// Testausgabe
			System.out.println("Datei:oeffneDatei: " + e);
		}
	}

	// Schlie�en der Datei
	public void schliesseDatei() {
		try {
			dieArtikeldatei.close();
		} catch (java.io.IOException e) {
			// Testausgabe
			System.out.println("Datei:schliesseDatei: " + e);
		}
	}

	// ueb07 Anzahl Datens�tze korrekt berechnen
	//
	// R�ckgabe der Dateigr��e in Anzahl Datens�tzen
	public int gibAnzahlDatensaetze() {
		long anzahlBytes = 0;
		try {
			anzahlBytes = dieArtikeldatei.length();
		} catch (IOException e) {
			System.out.println("Datei: gibAnzahlDatensaetze: " + e);
		}
		// Umrechnung der Bytezahl auf die Anzahl der Datens�tze
		return (int) (anzahlBytes / (long) (SATZLAENGE));
	}

	/********** Hilfsoperationen ***********/

	// ueb07: �ber index korrekte Byteposition in Datei berechnen
	// und mit seek den FilePointer darauf positionieren
	//
	// Positioniert in der Datei auf den durch index identifizierten Datensatz
	private void positioniereAufSatz(int index) throws IOException {
		if (dieArtikeldatei != null) {
			try {
				dieArtikeldatei.seek(index * SATZLAENGE);
			} catch (IOException e) {
				// Testausgabe
				System.out.println("Datei:positioniereAufSatz: " + e);
			}
		}
	}

	// Liest einen String der festen L�nge laenge ein
	// (1 Unicode-Zeichen = 2 Byte)
	private String leseFesteAnzahlZeichen(int laenge) throws IOException {
		StringBuffer einPuffer = new StringBuffer(laenge);
		int i = 0;
		while (i < laenge) {
			char zeichen = dieArtikeldatei.readChar();
			i++;
			if (zeichen == 0) // Ende der Nutzdaten
			{
				dieArtikeldatei.skipBytes(2 * (laenge - i));// Rest mit 0 �berlesen
				// Wird nur ben�tigt, da hinter Bezeichnung noch weitere Daten
				// gelesen werden.
				return einPuffer.toString();
			} else
				einPuffer.append(zeichen);// Anh�ngen an den Puffer
		}
		return einPuffer.toString();
	}

	// Schreibt den String einString mit fester L�nge laenge weg
	private void speichereFesteAnzahlZeichen(String einString, int laenge)
			throws IOException {
		for (int i = 0; i < laenge; i++) {
			char zeichen = 0;
			if (i < einString.length())
				zeichen = einString.charAt(i); // liefert das Zeichen an der
												// i-ten Stelle
			// Der Rest wird mit 0 aufgef�llt
			dieArtikeldatei.writeChar(zeichen); // zeichenweises Schreiben in die
												// Stammdatei
		}
	}
}
