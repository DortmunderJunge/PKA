package webshop.IO;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import webshop.Kunden.Account;

public class AccountdateiSerialisiert {

	// TODO ueb08
	// Attribute deklarieren und Konstruktor implementieren

	ObjectOutputStream objectout;
	ObjectInputStream objectin;

	public AccountdateiSerialisiert() {

	}

	// TODO ueb08
	public void serialisiereAccounts(Set<Account> accountliste) {
		// Soll die in der übergebenen Accountliste enthaltenen Objekte per
		// Serialisierung in der Datei 'Accounts.ser' im Verzeichnis
		// 'laufwerk:/Webshop/Daten/' abspeichern. 'laufwerk' steht dabei
		// für eins der auf Ihrem Rechner zur Verfügung stehenden Laufwerke.
		try {
			objectout = new ObjectOutputStream(new FileOutputStream(
					"C:/Users/Lars/workspace/Webshop/Daten/Accounts.ser"));
			for (Account a : accountliste) {
				objectout.writeObject(a);
			}
		} catch (IOException e) {
			System.out.println("Error writing account: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				objectout.close();
			} catch (IOException e) {
				System.out.println("Error closing Accountdatei-output: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// TODO ueb08
	public Set<Account> deserialisiereAccounts() {
		// Die in der Datei 'Accounts.ser' gespeicherten Accounts sollen
		// per Deserialisierung ausgelesen und der zurückzugebenden
		// Liste der Accounts hinzugefügt werden.
		// Geben Sie für jedes der ausgelesenen Accounts die zugehörigen
		// Daten (Emailadresse, Passwort, Kundendaten) zur Kontrolle aus
		// (siehe Methode 'anzeigenAccountdaten').
		Set<Account> accounts = new HashSet<Account>();
		try {
			objectin = new ObjectInputStream(new FileInputStream(
					"C:/Users/Lars/workspace/Webshop/Daten/Accounts.ser"));
			while (true) {
				Account a = (Account) objectin.readObject();
				accounts.add(a);
				anzeigenAccountdaten(a);
			}
		} catch (EOFException e) {

		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error reading Account: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				objectin.close();
			} catch (IOException e) {
				System.out.println("Error closing Accountdatei-input: "
						+ e.getMessage());
				e.printStackTrace();
			}
		}
		return accounts;
	}

	// TODO ueb08
	private void anzeigenAccountdaten(Account account) {
		// Hier sollen für Kontrollzwecke alle zu 'account' gehörenden
		// Daten (Emailadresse, Passwort, Kundendaten) auf der
		// Standardausgabe ausgegeben werden.
		System.out.printf("Email:\t%s\nPasswort:\t%s\nKundendaten:\t%s\n\n",
				account.getEmailAdresse(), account.getPasswort(), account
						.getKunde().toString());
	}
}
