package webshop.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import webshop.Webshop;

/*******
 * Controller-Code für Button jetztKaufen im Dialog Einkaufswagen; 
 * separate Klasse
 *******/

public class KaufenBeobachter implements ActionListener {

	private EinkaufswagenDialog eDialog;

	public KaufenBeobachter(EinkaufswagenDialog eDialog) {
		this.eDialog = eDialog;
	}

	private void anzeigenVersandnachricht(String nachricht) {
		HinweisFenster hinweisFenster = eDialog.getHinweisFenster();
		hinweisFenster.setText(nachricht);
		hinweisFenster.setVisible(true);
	}

	private void jetztKaufen() {
		Webshop webshop = eDialog.getOwner().getWebshop();
		if (!webshop.einkaufswagenIstLeer()) {
			anzeigenVersandnachricht("Die Artikel werden umgehend versandt an "
					+ webshop.getKundenName());
			// Einkaufswagen leeren
			webshop.leereEinkaufswagen();
			// Später ggf. Versandbenachrichtigung erweitern um bestellte
			// Artikel inkl. Lieferdauer
		}
	}

	public void actionPerformed(ActionEvent evt) {
		jetztKaufen();
		eDialog.setVisible(false);
	}
}
