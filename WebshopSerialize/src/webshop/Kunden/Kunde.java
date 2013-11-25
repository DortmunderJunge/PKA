package webshop.Kunden;

import java.io.Serializable;

// ueb08: Serializable implementieren
public class Kunde implements Serializable {
	
	private static int anzahlKunden = 0;
	private int kundenID;
	private String name;
	private int plz;
	private String ort;
	private String strasse;
	
	public Kunde(String name) throws UngueltigerNameException
	{
		kundenID = ++anzahlKunden;
		if ((name == null) || name.equals("")) throw new UngueltigerNameException();
		this.name = name;
	}	
	
	public int hashCode() {
		return kundenID;
	}
	
	public boolean equals (Object k) {
		if (k == null) return false;
		if (k instanceof Kunde)
		    return kundenID == ((Kunde)k).kundenID;
		else
			return false;
	}
	
	public long getKundenID() {
		return kundenID;
	}

	public String getName() {
		return name;
	}

	public String getAdresse() {
		return plz + " " + ort + "\n" + strasse;
	}

	public void setAdresse(String ort, int plz, String strasse) {
		this.ort = ort;
		this.plz = plz;
		this.strasse = strasse;
	}
	
	public String toString() {
		return 	"KundenID: " + kundenID + "\nName: " + name  
		        + "\nAdresse:\n" + getAdresse();
	}

}
