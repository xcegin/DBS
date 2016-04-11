package entity;

public class Traktor {

	private int did;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getZnacka() {
		return znacka;
	}

	public void setZnacka(String znacka) {
		this.znacka = znacka;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public String information() {
		return "Traktor s ID: " + this.getDid() + ", znacka: " + this.getZnacka() + ", stav: " + this.getStav() + ".\n";
	}

	private String znacka;
	private String stav;
}
