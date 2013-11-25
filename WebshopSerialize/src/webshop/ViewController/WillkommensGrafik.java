package webshop.ViewController;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

/**
 * Für ueb06
 * JPanel, in das der Einkaufswagen gezeichnet wird
 * @author Scheben
 *
 */
public class WillkommensGrafik extends JPanel {
	public WillkommensGrafik() {
		this.setPreferredSize(new Dimension(200, 100));
	}

	// GraphicsPanel : paintComponent
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.blue);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(5));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	            RenderingHints.VALUE_ANTIALIAS_ON);
		double x = 20;
		double y = 20;
		Point2D p1 = new Point2D.Double(x, y);
		Point2D p2 = new Point2D.Double(x + 20, y);
		g2.draw(new Line2D.Double(p1, p2));
		p1 = p2;
		x = p2.getX() + 30;
		y = y + 50;
		p2 = new Point2D.Double(x, y);
		g2.draw(new Line2D.Double(p1, p2));
		
		// Erstes Rad
		// Parameter: Linke obere Ecke des umschließenden Dreiecks, Breite, Höhe, Winkel
		g2.fill(new Arc2D.Double(p2.getX(), p2.getY() + 10, 10, 10, 0, 360, Arc2D.OPEN));
		
		p1 = p2;
		p2 = new Point2D.Double (p2.getX()+50, y);
		g2.draw(new Line2D.Double(p1, p2));
		
		// Zweites Rad
		// Parameter: Linke obere Ecke des umschließenden Dreiecks, Breite, Höhe, Winkel
		g2.fill(new Arc2D.Double(p2.getX() - 10, p2.getY() + 10, 10, 10, 0, 360, Arc2D.OPEN));
		
		p1 = p2;
		p2 = new Point2D.Double (p2.getX() + 20, y - 25);
		g2.draw(new Line2D.Double(p1, p2));
	}
}
