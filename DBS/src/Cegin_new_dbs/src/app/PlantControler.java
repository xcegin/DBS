package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Chliev;
import entity.Pole;
import entity.Traktor;
import entity.Zamestnanec;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PlantControler implements Initializable {

	private Zamestnanec zamestnanec;
	private ArrayList<Pole> polia = new ArrayList<Pole>();
	private ArrayList<Chliev> chlievy = new ArrayList<Chliev>();

	@FXML
	private TextField txtFind;

	@FXML
	private TextArea txtAreaLog;

	Traktor traktor = null;

	@FXML
	private void btnFindAction(ActionEvent event) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		int sum1 = 0, sum2 = 0, sum3 = 0;
		if (txtFind.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		if (!checkIfCorrect()) {
			txtAreaLog.appendText("ID musi byt cislo.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(counts(1));
			while (rs.next()) {
				sum1 = rs.getInt("suma");
			}

			stmt = c.createStatement();
			rs = stmt.executeQuery(counts(2));
			while (rs.next()) {
				sum2 = rs.getInt("suma");
			}

			stmt = c.createStatement();
			rs = stmt.executeQuery(counts(2));
			while (rs.next()) {
				sum3 = rs.getInt("suma");
			}

			if (sum1 != 0) {
				stmt = c.createStatement();
				rs = stmt.executeQuery(createQuery(sum1, 0, 0));
				rsParser(rs, sum1, 0, 0);
			}

			if (sum3 != 0) {
				stmt = c.createStatement();
				rs = stmt.executeQuery(createQuery(0, 0, sum3));
				rsParser(rs, 0, 0, sum3);
			}

			if (sum2 != 0) {
				stmt = c.createStatement();
				rs = stmt.executeQuery(createQuery(0, sum2, 0));
				rsParser(rs, 0, sum2, 0);
			}

			if (sum1 + sum2 == 0) {
				txtAreaLog.appendText("Neexistuju zaznamy s danym pouzivatelom");
				c.close();
				return;
			} else {
				txtAreaLog.appendText("Pocet najdenych zaznamov je " + (sum1 + sum2) + ".\n");
			}

			rs.close();
			stmt.close();
			txtAreaLog.appendText("Zamestnanec: " + zamestnanec.information() + "\n");
			for (Pole pole : polia) {
				txtAreaLog.appendText(" Pole, na ktorom pracuje: " + pole.information());
				txtAreaLog.appendText("\n");
			}
			for (Chliev chliev : chlievy) {
				txtAreaLog.appendText(" Chliev, v ktorom pracuje: " + chliev.information());
				txtAreaLog.appendText("\n");
			}
			if (traktor != null) {
				txtAreaLog.appendText("Traktor, s ktorym zamestnanec pracuje: " + traktor.information() + "\n");
			}
			c.close();

		} catch (SQLException ex) {

			if (c != null) {
				try {
					txtAreaLog.appendText("SQL Error.\n");
					c.rollback();
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		zamestnanec = new Zamestnanec();
		polia = new ArrayList<Pole>();
		chlievy = new ArrayList<Chliev>();

		System.out.println("Sucessful filter.");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	private Boolean checkIfCorrect() {
		for (char c : txtFind.getText().toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private String counts(int i) {
		if (i == 1) {
			return "Select COUNT(z.did) AS suma FROM zamestnanec z JOIN chliev_zamestnanec chz ON z.did = chz.zamestnanec_id WHERE z.did = "
				+ txtFind.getText() + ";";
		} else {
			return "Select COUNT(z.did) AS suma FROM zamestnanec z JOIN pole_zamestnanec p ON z.did = p.zamestnanec_id WHERE z.did = "
				+ txtFind.getText() + ";";
		}
	}

	private String tractors() {
		return "Select COUNT(z.did) AS suma FROM zamestnanec z JOIN pole_zamestnanec p ON z.did = p.zamestnanec_id JOIN traktor t ON p.traktor_id=t.did WHERE z.did=1;";
	}

	private String createQuery(int s1, int s2, int s3) {
		if (s1 != 0) {
			return "Select * FROM zamestnanec z JOIN chliev_zamestnanec chz ON z.did = chz.zamestnanec_id JOIN chliev ch ON ch.did = chz.chliev_id WHERE z.did = "
				+ txtFind.getText() + ";";
		}
		if (s2 != 0) {
			return "Select * FROM zamestnanec z JOIN pole_zamestnanec pz ON z.did = pz.zamestnanec_id JOIN pole p ON p.did = pz.pole_id WHERE z.did = "
				+ txtFind.getText() + ";";
		}
		if (s3 != 0) {
			return "Select * FROM zamestnanec z JOIN pole_zamestnanec pz ON z.did = pz.zamestnanec_id JOIN pole p ON p.did = pz.pole_id JOIN traktor t ON pz.traktor_id=t.did WHERE z.did = "
				+ txtFind.getText() + " ;";
		}
		return null;
	}

	private void rsParser(ResultSet rs, int s1, int s2, int s3) throws SQLException {
		while (rs.next()) {
			this.zamestnanec = new Zamestnanec();
			zamestnanec.setDid(rs.getInt("did"));
			zamestnanec.setMeno(rs.getString("meno"));
			zamestnanec.setPriezvisko(rs.getString("priezvisko"));
			zamestnanec.setPozicia(rs.getString("pozicia"));
			zamestnanec.setNastup(rs.getDate("nastup"));
			zamestnanec.setPlat(rs.getInt("plat"));
			if (s2 != 0) {
				Pole pole = new Pole();
				pole.setDid(rs.getInt("pole_id"));
				pole.setVelkost(rs.getInt("velkost"));
				pole.setRastlina_id(rs.getString("rastlina_id"));
				polia.add(pole);
			}
			if (s1 != 0) {
				Chliev chliev = new Chliev();
				chliev.setDid(rs.getInt("chliev_id"));
				chliev.setKapacita(rs.getInt("kapacita"));
				chliev.setDate_added(rs.getDate("date_added"));
				chliev.setStav(rs.getString("stav"));
				chlievy.add(chliev);
			}
			if (s3 != 0) {
				this.traktor = new Traktor();
				traktor.setDid(rs.getInt("traktor_id"));
				traktor.setStav(rs.getString("stav"));
				traktor.setZnacka(rs.getString("znacka"));
			}
		}
	}

}
