package webshop.ViewController;

import javax.swing.table.DefaultTableModel;

import webshop.Artikel.Kategorie;

/**** ueb06: MyTableModel überschreibt Methode getColumnClass ****/
// Dadurch kann ein RowSorter auf den Typen der Spalten arbeiten.
// Implementieren diese Typen die Comparable-Schnittstelle, werden die
// Elemente in einer Spalte entsprechend der compareTo-Methode
// sortiert.
// Ohne Überschreiben gibt getColumnClass Object.class zurück.
// Dann wird zum Sortieren auf die toString-Methode der Objekte
// zurückgegriffen
// Änderungen für ueb06
public class MyTableModel extends DefaultTableModel {
	public MyTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	public Class<?> getColumnClass(int column) {
		if (this.getRowCount() > 0)
			return this.getValueAt(0, column).getClass();
		else
			return Object.class;
	}
}
