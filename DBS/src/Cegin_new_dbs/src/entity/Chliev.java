package entity;

import java.sql.Date;

public class Chliev {

	private Date date_added;

	public Date getDate_added() {
		return date_added;
	}

	public void setDate_added(Date date_added) {
		this.date_added = date_added;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getKapacita() {
		return kapacita;
	}

	public void setKapacita(int kapacita) {
		this.kapacita = kapacita;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	private int did;
	private int kapacita;
	private String stav;

	public String information() throws ClassNotFoundException {
		return "ID: " + this.getDid() + ", Kapacita: " + this.getKapacita() + ", Stav: " + getStav()
			+ ", Datum pridania: " + this.getDate_added() + ".";
	}
}
