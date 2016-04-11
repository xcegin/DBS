package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Zamestnanec;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FindControler implements Initializable {

	@FXML
	private TextField txtIDemploy;

	@FXML
	private TextArea txtAreaLog;

	@FXML
	public void btnFindAction() throws ClassNotFoundException {

		Connection c = null;
		Statement stmt = null;
		ArrayList<Zamestnanec> people = new ArrayList<Zamestnanec>();
		if (!checkIfCorrect(txtIDemploy)) {
			txtAreaLog.appendText("ID musi byt cislo");
			return;
		}
		if (txtIDemploy.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String query = null;
			ResultSet rs = null;
			query = "SELECT * FROM zamestnanec " + getID() + ";";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				Zamestnanec zamestnanec = new Zamestnanec();
				zamestnanec.setDid(rs.getInt("did"));
				zamestnanec.setPlat(rs.getInt("plat"));
				zamestnanec.setNastup(rs.getDate("nastup"));
				zamestnanec.setMeno(rs.getString("meno"));
				zamestnanec.setPriezvisko(rs.getString("priezvisko"));
				zamestnanec.setPozicia(rs.getString("pozicia"));
				people.add(zamestnanec);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
					txtAreaLog.appendText("SQL ERROR " + ex.getMessage() + ". ID je prazdne.\n");
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}

		txtAreaLog.appendText("Nájdené záznamy: \n");
		for (Zamestnanec zamestnanec : people) {
			txtAreaLog.appendText(zamestnanec.information());
			txtAreaLog.appendText("\n");
		}
		System.out.println("Sucessful filter.");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	private String getID() {
		String id = txtIDemploy.getText();
		if (id == null) {
			return null;
		} else {
			return "WHERE did = " + id + " ";
		}

	}

	private Boolean checkIfCorrect(TextField txtField) {
		for (char c : txtField.getText().toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
}
