package webshop.IO;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Artikelindex {
	// Attribute
	public final int MAX = 1000;
	private String dateiname = null;
	private int indextabelle[]; // 0..MAX-1
	private RandomAccessFile eineIndexDatei;

	// Konstruktor
	public Artikelindex(String dateiname) {
		this.dateiname = dateiname;
		indextabelle = new int[MAX];
		// Initialisierung der Indextabelle
		for (int i = 0; i < MAX; i++)
			indextabelle[i] = -1; // Kein Datensatz zum Schlüssel vorhanden
	}

	// Speichert zu einem Schlüssel den in index übergebenen Wert in
	// der Indextabelle. Der Wert ist hier die Nummer des zugehörigen
	// Datensatzes (1-ter, 2-ter, ...)
	public void erzeugeEintrag(int schluessel, int index) throws IOException {
		if (schluessel < MAX)
			indextabelle[schluessel] = index;
		// Aktualisieren der Indexdatei, d.h. abspeichern der Datei
		aktualisiereIndexDatei(schluessel);
	}

	// Gibt zu dem Schlüssel den gefundenen Eintrag zurück
	// Hier -1 oder Nummer des zugehörigen Datensatzes
	public int gibIndexZuSchluessel(int schluessel) {
		if (schluessel < MAX)
			return indextabelle[schluessel];
		// oder -1, wenn Schlüssel zu groß ist
		else
			return -1;
	}

	// Für Testzwecke
	private void inhaltIndex () {
		for (int i = 0; i < indextabelle.length; i++)
			System.out.println("Indexeintrag an Position " + i + 
					           " :  " + indextabelle[i]);
	}
	
	// Liest die Indextabelle vollständig aus einer Datei
	// Dies geschieht nur beim Start des Programms
	public void ladeIndexDatei() throws IOException {
		eineIndexDatei = new RandomAccessFile(dateiname, "r");
		int index;
		for (int schluessel = 0; schluessel < MAX; schluessel++) {
			index = eineIndexDatei.readInt();
			indextabelle[schluessel] = index;
		}
		eineIndexDatei.close();
//		inhaltIndex();
	}

	// Speichert die Indextabelle vollständig in einer Datei
	// Dies geschieht beim Beenden des Programms
	public void speichereIndexDatei() throws IOException {
		eineIndexDatei = new RandomAccessFile(dateiname, "rw");
		for (int schluessel = 0; schluessel < MAX; schluessel++)
			eineIndexDatei.writeInt(indextabelle[schluessel]);
		eineIndexDatei.close();
	}

	// Aktualisiert die Indextabelle in der Indexdatei
	// Dies geschieht beim Hinzufügen eines neuen Indexes oder Ändern
	// eines alten Indexes
	private void aktualisiereIndexDatei(int schluessel) throws IOException {
		eineIndexDatei = new RandomAccessFile(dateiname, "rw");
		// Positionieren auf den entsprechenden Eintrag; 
		// eine int-Zahl belegt 4Bytes
		eineIndexDatei.seek((long) (schluessel * 4));
		eineIndexDatei.writeInt(indextabelle[schluessel]);
		eineIndexDatei.close();
	}
}
