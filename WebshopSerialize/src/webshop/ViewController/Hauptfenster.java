package webshop.ViewController;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import javax.swing.table.TableRowSorter;

import webshop.Webshop;
import webshop.Artikel.Artikel;
import webshop.Artikel.UngueltigeBezeichnungException;
import webshop.Artikel.UngueltigerPreisException;
import webshop.Kunden.AccountException;
import webshop.Kunden.UngueltigerNameException;

/****** Container implementiert selbst die Beobachter-Methoden       ******/
/****** und registriert sich selbst als Beobachter an seinen JButtons ******/
// TODO ueb08: Beim Schließen des Fensters Accountdaten 
// per Serialisierung in Datei speichern 
public class Hauptfenster extends JFrame implements ActionListener {

	private Webshop webshop;

	private AnmeldeDialog anmeldeDialog;
	private EinkaufswagenDialog einkaufswagenDialog;
	private HinweisFenster hinweisFenster;

	// ueb06: Tabelle mit eigenem Tabellenmodell
	private MyTableModel tabellenModell;
	private JTable artikelTabelle;

	private JButton anmelden;
	private JButton abmelden;
	private JButton auswaehlen;
	private JButton zumEinkaufswagen;

	private JPanel buttonPanel; // Zur Aufnahme der JButtons

	public Hauptfenster(String webshopBetreiber) throws NullPointerException,
			UngueltigerNameException, AccountException,
			UngueltigeBezeichnungException, UngueltigerPreisException {
		super("Webshop-Anwendung");
		this.webshop = new Webshop(webshopBetreiber);

		setSize(600, 300);
		setLayout(new BorderLayout());
		
		hinweisFenster = new HinweisFenster(this);
		anmeldeDialog = new AnmeldeDialog(this, "Anmelden");
		einkaufswagenDialog = new EinkaufswagenDialog(this, "Einkaufswagen");

		fuelleButtonPanel();
		add("North", buttonPanel);

		// ueb06: Mit sortierbarer Tabelle arbeiten statt mit List
		/*******************************************************************************/
		String[] spaltenUeberschriften = { "Kategorie", "Artikelnummer", "Bezeichnung", "Preis" };
		// zunächst 0 Zeilen
		tabellenModell = new MyTableModel(spaltenUeberschriften, 0);
		artikelTabelle = new JTable(tabellenModell);
		artikelTabelle
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		RowSorter<MyTableModel> sorter = new TableRowSorter<MyTableModel>(tabellenModell);
		artikelTabelle.setRowSorter(sorter);
		artikelTabelle.setEnabled(false);
		// Tabelle --> JScrollPane, da zweiteilig (Header, Inhalt)
		add("Center", new JScrollPane(artikelTabelle));
		/*******************************************************************************/		
		
		fuelleArtikelliste();

		// Beobachter registrieren
		registriereListener();
		
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
		
		// ueb06: Statt 'anzeigenString' Willkommensdialog anzeigen 
		anzeigenWillkommensDialog();
		addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				webshop.speichereAccounts();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public Hauptfenster(String webshopBetreiber, int maxVersuche)
			throws NullPointerException, UngueltigerNameException,
			AccountException, UngueltigeBezeichnungException,
			UngueltigerPreisException {
		this(webshopBetreiber);
		anmeldeDialog.setMaxAnmeldeVersuche(maxVersuche);
	}

	private void fuelleButtonPanel() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4));

		anmelden = new JButton("anmelden");
		buttonPanel.add(anmelden);

		abmelden = new JButton("abmelden");
		buttonPanel.add(abmelden);

		auswaehlen = new JButton("auswaehlen");
		buttonPanel.add(auswaehlen);

		zumEinkaufswagen = new JButton("zumEinkaufswagen");
		buttonPanel.add(zumEinkaufswagen);

		disabelButtons();
	}

	private void disabelButtons() {
		abmelden.setEnabled(false);
		auswaehlen.setEnabled(false);
		zumEinkaufswagen.setEnabled(false);
	}

	// ueb06: Daten jetzt in Tabellenmodell einfügen
	public void fuelleArtikelliste() {
		java.util.List<Artikel> aliste = webshop.getArtikelliste();
		Object[] zeile = new Object[4];
		for (Artikel artikel : aliste) {
			zeile[0] = artikel.getKategorie();
			zeile[1] = artikel.getArtikelnummer();
			zeile[2] = artikel.getBezeichnung();
			zeile[3] = artikel.getPreis();
			// Änderung für ueb06
			tabellenModell.addRow(zeile);
		}
	}

	public void anzeigenString(String st) {
		hinweisFenster.setText(st);
		hinweisFenster.setVisible(true);
	}
	
	// ueb06 Aufgabe 2
	public void anzeigenWillkommensDialog() {
		WillkommensDialog wd = new WillkommensDialog(this);
		wd.setVisible(true);
	}

	/*********** Verbindung zwischen View und Controller ***********/
	// Beobachter an den JButtons registrieren
	private void registriereListener() {
		anmelden.addActionListener(this);
		abmelden.addActionListener(this);
		auswaehlen.addActionListener(this);
		zumEinkaufswagen.addActionListener(this);
	}

	/********** Controller-Teil **************/
	// U.a. Implementierung der actionPerformed-Methode aus der
	// Schnittstelle ActionListener
	// EIN Listener für ALLE JButtons
	public void actionPerformed(ActionEvent e) {
		String befehl = e.getActionCommand();

		if (befehl.equals("anmelden")) {
			anmeldeDialog.iniAnmeldeDialog();
			// Programmcode wird ausgeführt, sobald Anmeldedialog
			// nicht mehr sichtbar ist
			if (anmeldeDialog.istAnmeldeFehler()) {
				anmeldeDialog.setAnmeldeFehler(false);
			} else {
				abmelden.setEnabled(true);
				auswaehlen.setEnabled(true);
				anmelden.setEnabled(false);
			}
		}
		if (befehl.equals("abmelden")) {
			webshop.abmelden();
			resetListSelections();
			disabelButtons();
			artikelTabelle.setEnabled(false);
			anmelden.setEnabled(true);
		}
		if (befehl.equals("auswaehlen")) {
			artikelTabelle.setEnabled(true);
			auswaehlen.setEnabled(false);
			zumEinkaufswagen.setEnabled(true);
		}
		if (befehl.equals("zumEinkaufswagen")) {
			artikelTabelle.setEnabled(false);
			zumEinkaufswagen.setEnabled(false);
			zumEinkaufswagen();
			// Einkaufswagendialog fertig; weitere Auswahl möglich
			auswaehlen.setEnabled(true);

		}
	}
	
	// ueb06: Wegen Einsatz eines RowSorters Indizes der Anzeige
	//        --> Indizes im Datenmodell
	/**
	 * Hilfsmethode zum Zurücksetzen der Selektionen in der Artikelliste
	 * 
	 * @return Gibt Indizes der Einträge zurück, die ausgewählt waren und jetzt
	 *         zurückgesetzt sind
	 */
	private int[] resetListSelections() {
		int[] selected = artikelTabelle.getSelectedRows();
		// Indizes nach eventueller Sortierung andere als Indizes
		// der Daten im Modell. Daher auf Indizes im Modell abbilden über
		// convertRowIndexToModel.
		for (int i = 0; i < selected.length; i++) {
			selected[i] = artikelTabelle.convertRowIndexToModel(selected[i]);
		}
		artikelTabelle.clearSelection();
		return selected;
	}

	/**
	 * Hilfsmethode für zumEinkaufswagen()
	 * 
	 * @param ausgewaehlteArtikel
	 *            Indizes der ausgewaehlten Artikel aus der Artikelliste
	 * @return Liste der zugehörige Artikel
	 */
	private java.util.List<Artikel> ausgewaehlteArtikel(
			int[] ausgewaehlteArtikel) {
		java.util.List<Artikel> aliste = webshop.getArtikelliste();
		ArrayList<Artikel> auswahl = new ArrayList<Artikel>();
		for (int i = 0; i < ausgewaehlteArtikel.length; i++)
			auswahl.add(aliste.get(ausgewaehlteArtikel[i]));
		return auswahl;
	}

	private void zumEinkaufswagen() {
		int[] selected = resetListSelections();
		webshop.inEinkaufswagen(ausgewaehlteArtikel(selected));
		einkaufswagenDialog.setInhalt(webshop.getInhaltEinkaufswagen());
		einkaufswagenDialog.setVisible(true);
	}

	/*********** Fuer Subfenster ************/

	public Webshop getWebshop() {
		return webshop;
	}

}
