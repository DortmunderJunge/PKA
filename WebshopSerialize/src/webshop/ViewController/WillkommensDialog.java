package webshop.ViewController;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Für ueb06
 * Enthält Willkommensgrafik mit Einkaufswagen als Komponente
 * @author Scheben
 *
 */
public class WillkommensDialog extends JDialog {
	public WillkommensDialog (JFrame owner) {
		super(owner);
		setTitle("Willkommen");
		setModal(true);
		setSize(200, 150);
		setLocation(10, 10);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		add("Center", new WillkommensGrafik());
	}
}
