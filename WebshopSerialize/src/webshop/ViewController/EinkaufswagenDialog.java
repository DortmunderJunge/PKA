package webshop.ViewController;


import java.awt.Component;
import java.awt.Container;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.BevelBorder;

import webshop.Artikel.Artikel;

/***** Beobachter des jetztKaufen-Buttons als separate Klasse KaufenBeobachter *****/
public class EinkaufswagenDialog extends JDialog {

	private Hauptfenster owner;
	private HinweisFenster hinweisFenster;
	
	// ueb06: JList statt List: Listenmodell nötig
	private JList<Artikel> inhaltEinkaufswagen;
	private DefaultListModel<Artikel> listenModell = new DefaultListModel<Artikel>();
	

	public EinkaufswagenDialog(Hauptfenster owner, String titel) {
		super(owner, titel);
		this.owner = owner;
		setModal(true);

		setSize(450, 200);
		setLocation(100, 50);

		hinweisFenster = new HinweisFenster(this);
		hinweisFenster.setLocation(100, 210);

		JLabel labelInhaltEinkaufswagen = new JLabel(
				"Inhalt Ihres Einkaufswagens");
		// ueb06: Text auffälliger
		labelInhaltEinkaufswagen.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		// ueb06: JList erstellen mit Angabe des Datenmodells
		inhaltEinkaufswagen = new JList<Artikel>(listenModell);
		JButton jetztKaufen = new JButton("jetztKaufen");

		GridBagLayout lm = new GridBagLayout();
		setLayout(lm);

		addComponent(this, lm, labelInhaltEinkaufswagen, 0, 0, 1, 1, 0.0, 0.0);
		
		// Listenelement: Ausdehnung über 1 Spalte und 4 Zeilen
		// Überschüssiger Platz: Da weightx > 0 und alle Komponenten in derselben 
		// Spalte liegen, bekommt jede Komponente von horizontalem Zusatzplatz ab
		// Da weighty > 0 nur für Listenelement, bekommt nur dieses von extra
		// vertikalem Platz ab. Es gibt keine anderen Elemente in derselben Zeile,
		// die auch von vertikalen Zusatzplatz für die Liste profitieren könnten.		
		addComponent(this, lm, inhaltEinkaufswagen, 0, 1, 1, 4, 1.0, 1.0);
		addComponent(this, lm, jetztKaufen, 0, 5, 1, 1, 0.0, 0.0);

		// Beobachter registrieren
		jetztKaufen.addActionListener(new KaufenBeobachter(this));
		
		// ueb06: WindowListener kann entfallen wegen Default "HIDE_ON_CLOSE"

	}

	// Zur Vereinfachung des Umgangs mit dem GridBagLayout
	private void addComponent(Container container, GridBagLayout lm,
			Component comp, int x, int y, int width, int height,
			double weightx, double weighty) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.fill = GridBagConstraints.BOTH;
		lm.setConstraints(comp, constraints);
		container.add(comp);
	}

	// ueb06: Angepasst auf JList. Jetzt alle Operationen wie
	// Inhalt löschen, Element hinzufügen etc. auf Listenmodell
	// Zur Anzeige wird toString-Methode der Elemente aufgerufen
	public void setInhalt(java.util.List<Artikel> inhalt) {
		listenModell.clear();
		for (Artikel a : inhalt)
			listenModell.addElement(a);
	}

	/**** Für Kommunikation mit Controller (separate Klasse) ****/
	public Hauptfenster getOwner() {
		return owner;
	}

	public HinweisFenster getHinweisFenster() {
		return hinweisFenster;
	}
}
