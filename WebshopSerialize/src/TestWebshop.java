import webshop.Artikel.UngueltigeBezeichnungException;
import webshop.Artikel.UngueltigerPreisException;
import webshop.Kunden.AccountException;
import webshop.Kunden.UngueltigerNameException;
import webshop.ViewController.Hauptfenster;

public class TestWebshop {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			new Hauptfenster("Mattes Warenhaus", 3);
		} catch (NullPointerException | UngueltigerNameException
				| AccountException | UngueltigeBezeichnungException
				| UngueltigerPreisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}