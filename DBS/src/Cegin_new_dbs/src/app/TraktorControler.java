package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TraktorControler implements Initializable {

	@FXML
	private TextField txtIDtractor;

	@FXML
	private TextField txtIDemploy;

	@FXML
	private TextArea txtAreaLog;

	@FXML
	private void btnAddAction(ActionEvent event) throws ClassNotFoundException {

		Connection c = null;
		Statement stmt = null;
		int sum = 0;
		if (txtIDemploy.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		if (txtIDtractor.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		if (!checkIfCorrect(txtIDtractor)) {
			txtAreaLog.appendText("ID traktor musÌ byù ËÌslo.\n");
			return;
		}
		if (!checkIfCorrect(txtIDemploy)) {
			txtAreaLog.appendText("ID zamestnanec musÌ byù ËÌslo.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt
				.executeQuery("SELECT COUNT(did) FROM traktor WHERE did = " + txtIDtractor.getText() + ";");
			while (rs.next()) {
				sum = (rs.getInt("count"));
				System.out.println();
			}
			stmt.close();
			if (sum == 0) {
				c.close();
				txtAreaLog.appendText("Traktor s ID: " + txtIDtractor.getText() + " neexistuje.\n");
				return;
			}

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(did) FROM traktor WHERE did = " + txtIDtractor.getText() + ";");
			while (rs.next()) {
				sum = (rs.getInt("count"));
				System.out.println();
			}
			stmt.close();
			if (sum == 0) {
				c.close();
				txtAreaLog.appendText("Zamestnanec s ID: " + txtIDemploy.getText() + " neexistuje.\n");
				return;
			}

			sum = 0;
			stmt = c.createStatement();
			rs = stmt.executeQuery(
				"SELECT COUNT(did) FROM pole_zamestnanec WHERE zamestnanec_id = " + txtIDemploy.getText() + ";");
			while (rs.next()) {
				sum = (rs.getInt("count"));
				System.out.println();
			}
			stmt.close();

			if (sum == 0) {
				txtAreaLog.appendText("Zamestnanec nepracuje na poli, teda nie je mozne priradit traktor.\n");
				c.close();
				return;
			}

			else {

				stmt = c.createStatement();
				String sql = "UPDATE pole_zamestnanec SET traktor_id = null WHERE traktor_id = "
					+ txtIDtractor.getText() + ";";
				stmt.executeUpdate(sql);
				stmt.close();

				stmt = c.createStatement();
				sql = "UPDATE pole_zamestnanec SET traktor_id = " + txtIDtractor.getText() + " WHERE zamestnanec_id = "
					+ txtIDemploy.getText() + ";";
				stmt.executeUpdate(sql);
				stmt.close();
			}

			c.commit();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		txtAreaLog.appendText("Traktor ID: " + txtIDtractor.getText() + " priradeny zamestnancovi s ID: "
			+ txtIDemploy.getText() + ".\n");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
