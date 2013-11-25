package webshop.ViewController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import webshop.Webshop;
import webshop.Kunden.Account;
import webshop.Kunden.AccountException;

/***** Controller-Code des weiter-Buttons in innerer Klasse 'AnmeldeController' *****/
public class AnmeldeDialog extends JDialog {

	private int maxAnmeldeVersuche = 1;
	private int anzVersuche = 0;    // Zum Zählen der bereits erfolgten
									// Anmeldeversuche
	private boolean anmeldeFehler = false; // Für Haupfenster: Anmeldung
										   // erfolgreich?

	// Außerhalb des Konstruktors benoetigte Textfelder als Attribute
	private JTextField textfeldEmailAdresse;

	// ueb06: statt TextField JPasswordField verwendet
	private JPasswordField textfeldPasswort;
	
	private HinweisFenster hinweisFenster;

	public AnmeldeDialog(Hauptfenster owner, String titel) {
		super(owner, titel);
		setModal(true);

		setSize(300, 150);
		setLocation(100, 50);
		LayoutManager lm = new GridLayout(5, 1);
		setLayout(lm);

		JLabel labelEmailAdresse = new JLabel("Geben Sie Ihre Emailadresse ein");
		JLabel labelPasswort = new JLabel("Geben Sie Ihr Passwort ein");
		textfeldEmailAdresse = new JTextField();
		
		// ueb06: statt TextField JPasswordField verwendet
		textfeldPasswort = new JPasswordField();
		
		JButton weiter = new JButton("weiter");
		hinweisFenster = new HinweisFenster(this);
		hinweisFenster.setLocation(60, 200);

		add(labelEmailAdresse);
		add(textfeldEmailAdresse);
		add(labelPasswort);
		add(textfeldPasswort);
		add(weiter);

		/***** Controller-Code in innerer Klasse 'AnmeldeController' *****/
		AnmeldeController ac = new AnmeldeController();

		// Beobachter registrieren
		weiter.addActionListener(ac);
		
		// ueb06: Hier jetzt nötig wegen Default "HIDE_ON_CLOSE"
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

	}

	public void setMaxAnmeldeVersuche(int maxVersuche) {
		maxAnmeldeVersuche = maxVersuche;
	}

	public void setAnmeldeFehler(boolean fehler) {
		anmeldeFehler = fehler;
	}

	public boolean istAnmeldeFehler() {
		return anmeldeFehler;
	}

	public void iniAnmeldeDialog() {
		textfeldEmailAdresse.setText("");
		textfeldPasswort.setText("");
		textfeldEmailAdresse.requestFocus();
		this.setVisible(true);
	}

	/***** Controller als innere Klasse *****/
	private class AnmeldeController implements ActionListener {

		private void aktualisiereAnmeldeVersuche() {
			anzVersuche++;
			if (anzVersuche >= maxAnmeldeVersuche) {
				hinweisFenster.setText("Anmeldung nicht erfolgreich: "
						+ "Anzahl der maximalen Versuche überschritten.");
				hinweisFenster.setVisible(true);
				anzVersuche = 0;
				anmeldeFehler = true;
				setVisible(false);
			}
		}

		public void actionPerformed(ActionEvent evt) {
			String email = textfeldEmailAdresse.getText();
			
			// ueb06: getText() ist depricated; getPassword liefert char[]
			String pwd = String.valueOf(textfeldPasswort.getPassword());

			try {
				Account account = new Account(email, pwd);
				Webshop webshop = ((Hauptfenster) getOwner()).getWebshop();
				if (webshop.istGueltig(account)) {
					webshop.anmelden(account);
					anzVersuche = 0;
					// AnmeldeDialog wird erst verlassen, wenn Sichtbarkeit
					// auf false gesetzt wird, oder dispose aufgerufen wird.
					setVisible(false);
				} else {
					hinweisFenster.setText("Ungültige Anmeldedaten");
					hinweisFenster.setVisible(true);
					aktualisiereAnmeldeVersuche();
				}
			} catch (AccountException e) {
				hinweisFenster.setText(e.toString());
				hinweisFenster.setVisible(true);
				aktualisiereAnmeldeVersuche();
			}
		}
	}

}
