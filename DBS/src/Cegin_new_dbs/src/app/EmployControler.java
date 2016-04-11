package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmployControler implements Initializable {

	@FXML
	private TextField txtFirstName;

	@FXML
	private TextField txtIDemploy;

	@FXML
	private TextField txtLastName;

	@FXML
	private TextField txtIDplace;

	@FXML
	private TextField txtPayment;

	@FXML
	private TextArea txtAreaLog;

	@FXML
	private ComboBox<String> comboBoxPosition;

	@FXML
	private ComboBox<String> comboBoxWhere;

	ObservableList<String> list = FXCollections.observableArrayList("Brigadnik", "Pracovnik", "Veduci");
	ObservableList<String> listActual = FXCollections.observableArrayList("Chliev", "Pole");

	@FXML
	private void btnAddAction(ActionEvent event) throws ClassNotFoundException {

		Connection c = null;
		Statement stmt = null;
		if (!checkIfCorrect(txtPayment)) {
			txtAreaLog.appendText("Plat musÌ byù ËÌslo.\n");
			return;
		}
		if (comboBoxPosition.getSelectionModel().getSelectedItem() == null) {
			txtAreaLog.appendText("Vyberte poziciu.\n");
			return;
		}
		if (txtPayment.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte plat.\n");
			return;
		}
		if (txtFirstName.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte meno.\n");
			return;
		}
		if (txtLastName.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte priezvisko.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "INSERT INTO zamestnanec (meno,priezvisko,plat,pozicia,nastup) " + "VALUES ('"
				+ txtFirstName.getText() + "','" + txtLastName.getText() + "', " + txtPayment.getText() + " , '"
				+ comboBoxPosition.getSelectionModel().getSelectedItem() + "', CURRENT_DATE );";
			stmt.executeUpdate(sql);
			stmt.close();
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
		System.out.println("Records created successfully");
		txtAreaLog.appendText("⁄speöne vytvoren˝ z·znam: Meno: " + txtFirstName.getText() + ", Priezvisko: "
			+ txtLastName.getText() + ", Pozicia: " + comboBoxPosition.getSelectionModel().getSelectedItem()
			+ ", Plat: " + txtPayment.getText() + ".\n");

	}
	@FXML
	private void btnActualAction(ActionEvent event) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		if (txtIDemploy.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		if (txtIDplace.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte ID.\n");
			return;
		}
		if (comboBoxWhere.getSelectionModel().getSelectedItem() == null) {
			txtAreaLog.appendText("Vyberte moznost.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			String query = "SELECT COUNT(did) AS suma FROM zamestnanec WHERE did = " + txtIDemploy.getText() + ";";
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int sum = 0;
			while (rs.next()) {
				sum = rs.getInt("suma");
			}
			if (sum == 0) {
				txtAreaLog.appendText("Zamestnanec neexisuje.\n");
				c.close();
				return;
			}

			query = getTestQuery();
			rs = stmt.executeQuery(query);
			sum = 0;
			while (rs.next()) {
				sum = rs.getInt("suma");
			}
			if (sum == 0) {
				txtAreaLog.appendText("Miesto neexisuje.\n");
				c.close();
				return;
			}

			stmt = c.createStatement();
			String sql = getQuery();
			stmt.executeUpdate(sql);
			stmt.close();
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
		System.out.println("Records created successfully");
		txtAreaLog.appendText("⁄speöne priradeny z·mestnanec: ID: " + txtIDemploy.getText() + ", Miesto priradenia: "
			+ comboBoxWhere.getSelectionModel().getSelectedItem() + ", ID miesta: " + txtIDplace.getText() + ".\n");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboBoxPosition.setItems(list);
		comboBoxWhere.setItems(listActual);

	}

	private Boolean checkIfCorrect(TextField txtField) {
		for (char c : txtField.getText().toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private String getTestQuery() {
		String obj = comboBoxWhere.getSelectionModel().getSelectedItem();
		String query = null;
		if (obj.equals("Chliev")) {
			query = "SELECT COUNT(did) AS suma FROM chliev WHERE did = " + txtIDplace.getText() + ";";
		} else {
			query = "SELECT COUNT(did) AS suma FROM pole WHERE did = " + txtIDplace.getText() + ";";
		}
		return query;
	}

	private String getQuery() {
		String obj = comboBoxWhere.getSelectionModel().getSelectedItem();
		String query = null;
		if (obj.equals("Chliev")) {
			query = "INSERT INTO chliev_zamestnanec (chliev_id,zamestnanec_id) VALUES(" + txtIDplace.getText() + ", "
				+ txtIDemploy.getText() + ");";
		} else {
			query = "INSERT INTO pole_zamestnanec (pole_id,zamestnanec_id) VALUES(" + txtIDplace.getText() + ", "
				+ txtIDemploy.getText() + ");";
		}
		return query;
	}
}
