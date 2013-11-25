package webshop;

import java.util.*;

import webshop.Artikel.Artikel;

/**
 * Vereinfachung: Ein Artikel kann nicht mehrfach im Einkaufswagen sein
 *                (also nicht mehrmals dasselbe Buch etc.)
 *                Ansonsten braucht man zu jedem Artikel im Einkaufswagen 
 *                eine Anzahl oder muss diese über Suche ermitteln             
 * @author Scheben
 *
 */
public class Einkaufswagen {
	private LinkedList<Artikel> artikelliste = new LinkedList<Artikel>();
	
	public void einfuegen(Artikel artikel) {
		artikelliste.add(artikel);
	}
	
	public boolean loeschen(Artikel artikel) {
		return artikelliste.remove(artikel);
	}
	
	public void allesLoeschen() {
		artikelliste = new LinkedList<Artikel>();
	}	
	
	public List<Artikel> getInhalt() {
		return artikelliste;
	}
	
	public boolean istLeer() {
		return artikelliste.size() <= 0;
	}	

}


