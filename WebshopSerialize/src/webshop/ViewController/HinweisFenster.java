
package webshop.ViewController;

import java.awt.BorderLayout;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/***** Verwendet anonyme Klasse für den Beobachter des ok-Buttons *****/
public class HinweisFenster extends JDialog {
	
	// Außerhalb des Konstruktors benötigtes Label --> setText()
	private JLabel hinweisLabel;

	public HinweisFenster(Window owner) {
		
		super(owner);
		setModal(true);
		
		hinweisLabel = new JLabel("", JLabel.CENTER);
		add(hinweisLabel, BorderLayout.CENTER);
		
		JButton ok = new JButton("ok");
		ok.addActionListener(new ActionListener()
		/****** Anonyme Klasse mit Controller-Code *****/
		{
			@Override
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		
		add(ok, BorderLayout.SOUTH);
		setSize(450, 100);
		setLocation(70, 100);
		
		// ueb06: Hier jetzt nötig wegen Default "HIDE_ON_CLOSE"
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}

	public void setText(String s) {
		hinweisLabel.setText(s);
	}
}
