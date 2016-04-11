package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class StatisticsControler implements Initializable {

	@FXML
	private TextArea txtAreaLog;

	@FXML
	private ComboBox<String> comboBoxStatistic;

	ObservableList<String> list = FXCollections.observableArrayList("Množtvo krmiva: ",
		"Najèastejšie meno zvierat je: ", "Priemerny plat je: ", "Pocet druhov zvierat, ktore su v chlievoch: ",
		"Priemerna obsadenost chlievov je: ");

	@FXML
	private void btnShowAction(ActionEvent event) throws ClassNotFoundException {
		if (comboBoxStatistic.getSelectionModel().getSelectedItem().equals("Množtvo krmiva: ")) {
			statistics(1);
		} else if (comboBoxStatistic.getSelectionModel().getSelectedItem().equals("Najèastejšie meno zvierat je: ")) {
			statistics(2);
		} else if (comboBoxStatistic.getSelectionModel().getSelectedItem().equals("Priemerny plat je: ")) {
			statistics(3);
		} else if (comboBoxStatistic.getSelectionModel().getSelectedItem()
			.equals("Pocet druhov zvierat, ktore su v chlievoch: ")) {
			statistics(4);
		} else if (comboBoxStatistic.getSelectionModel().getSelectedItem()
			.equals("Priemerna obsadenost chlievov je: ")) {
			statistics(5);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxStatistic.setItems(list);
	}

	private void statistics(int i) throws ClassNotFoundException {
		Statement stmt = null;
		Connection c = null;
		List<Rastlina> rastliny = new ArrayList<Rastlina>();
		if (comboBoxStatistic.getSelectionModel().getSelectedItem() == null) {
			txtAreaLog.appendText("Vyberte z ponuky.\n");
			return;
		}
		if (i == 1) {
			try {

				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres",
					"chidorinagashi");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(
					"SELECT type AS ras, SUM(velkost) FROM pole p JOIN rastlina r ON  r.code = p.rastlina_id GROUP BY ras ORDER BY ras DESC;");
				while (rs.next()) {
					Rastlina rastlina = new Rastlina();
					rastlina.setRas(rs.getString("ras"));
					rastlina.setSuma(rs.getInt("sum"));
					rastliny.add(rastlina);
				}
				for (Rastlina rastlina : rastliny) {
					rastlina.information();
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException ex) {

				if (c != null) {
					try {
						c.rollback();
						txtAreaLog.appendText("SQL Error.\n");
					} catch (SQLException ex1) {
						System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
						System.exit(0);
					}
				}
			}
		}

		if (i == 2) {
			String meno = "";
			try {

				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres",
					"chidorinagashi");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(
					"SELECT MAX(tab.meno) AS meno FROM (SELECT COUNT(name) AS suma, name AS meno FROM zviera GROUP BY meno ORDER BY suma DESC) AS tab;");
				while (rs.next()) {
					meno = rs.getString("meno");
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException ex) {

				if (c != null) {
					try {
						c.rollback();
						txtAreaLog.appendText("SQL Error.\n");
					} catch (SQLException ex1) {
						System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
						System.exit(0);
					}
				}
			}
			txtAreaLog.appendText("Najcastejsie meno zvierat je: " + meno + ".\n");
		}

		if (i == 3) {
			Float plat = null;
			try {

				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres",
					"chidorinagashi");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT ROUND(AVG(plat),1) AS plat from zamestnanec;");
				while (rs.next()) {
					plat = rs.getFloat("plat");
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException ex) {

				if (c != null) {
					try {
						c.rollback();
						txtAreaLog.appendText("SQL Error.\n");
					} catch (SQLException ex1) {
						System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
						System.exit(0);
					}
				}
			}
			txtAreaLog.appendText("Priemerny plat je je: " + plat.floatValue() + ".\n");
		}

		if (i == 4) {
			int poc = 0;
			try {

				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres",
					"chidorinagashi");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(
					"SELECT COUNT(*) FROM (SELECT COUNT(*) FROM zviera z JOIN chliev ch ON ch.did = z.chliev_id GROUP BY type_id) AS tab; ");
				while (rs.next()) {
					poc = rs.getInt("count");
				}
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException ex) {

				if (c != null) {
					try {
						c.rollback();
						txtAreaLog.appendText("SQL Error.\n");
					} catch (SQLException ex1) {
						System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
						System.exit(0);
					}
				}
			}
			txtAreaLog.appendText("Pocet roznych druhov zvierat, ktore su v chlievoch: " + poc + ".\n");
		}

		if (i == 5) {
			Float poc = (float) 0;
			try {
				List<Float> priemery = new ArrayList<Float>();
				Class.forName("org.postgresql.Driver");
				c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres",
					"chidorinagashi");
				c.setAutoCommit(false);
				System.out.println("Opened database successfully");

				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(
					"SELECT count, kapacita  FROM (SELECT tab.did, COUNT(tab.did) FROM(SELECT ch.kapacita, ch.did FROM chliev ch JOIN zviera z ON z.chliev_id = ch.did GROUP BY z.did, ch.kapacita, ch.did) AS tab GROUP BY tab.did) as tabb JOIN chliev chz ON chz.did=tabb.did;");
				while (rs.next()) {
					Float p = (float) rs.getInt("count");
					Float k = (float) rs.getInt("kapacita");
					Float f = p / k;
					priemery.add(f);
				}
				for (Float priemer : priemery) {
					poc += priemer;
				}
				poc = poc / priemery.size();
				poc = poc * 100;
				rs.close();
				stmt.close();
				c.close();
			} catch (SQLException ex) {

				if (c != null) {
					try {
						c.rollback();
						txtAreaLog.appendText("SQL Error.\n");
					} catch (SQLException ex1) {
						System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
						System.exit(0);
					}
				}
			}
			txtAreaLog.appendText("Priemerna obsadenost chlievov je: " + poc + "%.\n");
		}

	}

	private class Rastlina {

		private int suma;
		private String ras;

		public int getSuma() {
			return suma;
		}

		public void setSuma(int suma) {
			this.suma = suma;
		}

		public String getRas() {
			return ras;
		}

		public void setRas(String ras) {
			this.ras = ras;
		}

		public void information() {
			txtAreaLog.appendText("Typ krmiva: " + this.getRas() + ", Mnozstvo krmiva: " + this.getSuma() + ".\n");
		}

	}

}
