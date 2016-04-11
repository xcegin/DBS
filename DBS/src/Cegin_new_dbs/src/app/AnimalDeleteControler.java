package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import entity.Zviera;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AnimalDeleteControler implements Initializable {

	@FXML
	private TextField txtChlievID;

	@FXML
	private TextField txtWeight;
	@FXML
	private TextField txtAnimalSearchName;
	@FXML
	private TextField txtZvieraID;

	@FXML
	private TextArea txtAreaLog;

	@FXML
	private ComboBox<String> comboBoxType;

	@FXML
	private void btnDeleteAction(ActionEvent event) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		if (!checkIfCorrect(txtWeight)) {
			txtAreaLog.appendText("Vaha musÌ byù ËÌslo.\n");
			return;
		}
		if (comboBoxType.getSelectionModel().getSelectedItem() == null) {
			txtAreaLog.appendText("Vyberte typ.\n");
			return;
		}
		String text = null;
		text = txtWeight.getText();
		if (text.hashCode() == 0) {
			txtAreaLog.appendText("Zadajte cislo.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "DELETE from zviera WHERE weight > " + txtWeight.getText() + " AND type_id LIKE '"
				+ getIdFromTypeofAnimal() + "';";
			stmt.executeUpdate(sql);
			c.commit();

			stmt.close();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
					txtAreaLog.appendText("SQL ERROR.\n");
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		txtAreaLog.appendText("⁄speöne odstranenÈ zvierata typu: " + comboBoxType.getSelectionModel().getSelectedItem()
			+ " s vahou nad " + txtWeight.getText() + "kg.\n");

	}
	private boolean idExists(int i, Connection c) throws SQLException {
		Statement stmt = c.createStatement();
		String sql = null;
		int nieco = 0;
		if (i == 1) {
			sql = "SELECT COUNT(did) AS suma FROM chliev WHERE did = " + txtChlievID.getText() + ";";
		} else {
			sql = "SELECT COUNT(did) AS suma FROM zviera WHERE did = " + txtZvieraID.getText() + ";";
		}
		stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			nieco = (rs.getInt("suma"));
		}
		if (nieco == 0) {
			return false;
		} else {
			return true;
		}
	}
	@FXML
	private void btnActualAction(ActionEvent event) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		Zviera zviera = new Zviera();
		if (!checkIfCorrect(txtChlievID)) {
			txtAreaLog.appendText("Chliev ID musÌ byù ËÌslo.\n");
			return;
		}
		if (!checkIfCorrect(txtZvieraID)) {
			txtAreaLog.appendText("Zviera ID musÌ byù ËÌslo.\n");
			return;
		}
		if (txtZvieraID.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte cislo.\n");
			return;
		}
		if (txtChlievID.getText().hashCode() == 0) {
			txtAreaLog.appendText("Zadajte cislo.\n");
			return;
		}
		String chlievID = txtChlievID.getText();
		String ZvieraID = txtZvieraID.getText();
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			if (!idExists(1, c)) {
				txtAreaLog.appendText("Chliev ID neexistuje.\n");
				c.close();
				return;
			}

			if (!idExists(2, c)) {
				txtAreaLog.appendText("Zviera ID neexistuje.\n");
				c.close();
				return;
			}

			stmt = c.createStatement();
			String sql = "UPDATE zviera set chliev_id = " + chlievID + " where did=" + ZvieraID + ";";
			stmt.executeUpdate(sql);
			c.commit();
			stmt.close();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
					txtAreaLog.appendText("SQL ERROR.\n");
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		zviera.makeItHappen(Integer.parseInt(ZvieraID));
		txtAreaLog
			.appendText("⁄speöne pridanie zvierata " + zviera.information() + " Do chlievu s id: " + chlievID + ".\n");

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Connection c = null;
		Statement stmt = null;
		ObservableList<String> listType = FXCollections.observableArrayList();
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT typ FROM zviera_typ;");
			while (rs.next()) {
				listType.add(rs.getString("typ"));
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (SQLException | ClassNotFoundException ex) {

			if (c != null) {
				try {
					c.rollback();
					txtAreaLog.appendText("SQL ERROR.\n");
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		comboBoxType.setItems(listType);
	}

	private String getIdFromTypeofAnimal() throws ClassNotFoundException {
		String type = null;
		type = comboBoxType.getSelectionModel().getSelectedItem();

		Connection c = null;
		Statement stmt = null;
		String code = null;
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT code FROM zviera_typ WHERE typ LIKE '" + type + "';");
			while (rs.next()) {
				code = rs.getString("code");
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (SQLException ex) {

			if (c != null) {
				try {
					c.rollback();
					txtAreaLog.appendText("SQL ERROR.\n");
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		return code;
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
