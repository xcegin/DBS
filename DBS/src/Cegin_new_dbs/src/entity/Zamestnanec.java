package entity;

import java.sql.Date;

public class Zamestnanec {

	private String meno;
	private String priezvisko;

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}

	public String getPriezvisko() {
		return priezvisko;
	}

	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}

	public Date getNastup() {
		return nastup;
	}

	public void setNastup(Date nastup) {
		this.nastup = nastup;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getPlat() {
		return plat;
	}

	public void setPlat(int plat) {
		this.plat = plat;
	}

	public String getPozicia() {
		return pozicia;
	}

	public void setPozicia(String pozicia) {
		this.pozicia = pozicia;
	}

	public String information() {
		return "Meno: " + this.getMeno() + ", ID: " + this.getDid() + ", Priezvisko: " + this.getPriezvisko()
			+ ", Pozicia: " + getPozicia() + ", Plat: " + this.getPlat() + ", Nastup dna: " + this.getNastup() + ".";
	}

	private Date nastup;
	private int did;
	private int plat;
	private String pozicia;
}
