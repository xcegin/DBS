package app;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

public class AnimalAddControler implements Initializable {

	@FXML
	private TextField txtAnimalName;

	@FXML
	private TextField txtWeight;

	@FXML
	private TextField txtAnimalSearchName;

	@FXML
	private TextField txtRaceSearch;

	@FXML
	private ComboBox<String> comboBoxType;

	@FXML
	private ComboBox<String> comboBoxHungry;

	@FXML
	private ComboBox<String> comboBoxHungrySearch;

	@FXML
	private ComboBox<String> comboBoxTypeSearch;

	@FXML
	private TextArea txtAreaLog;

	ObservableList<String> listHungry = FXCollections.observableArrayList("Ano", "Nie");

	@FXML
	private void btnAddAction(ActionEvent event) throws ClassNotFoundException {

		Connection c = null;
		Statement stmt = null;
		if (!checkIfCorrect()) {
			txtAreaLog.appendText("Vaha musÌ byù ËÌslo.\n");
			return;
		}
		if (comboBoxType.getSelectionModel().getSelectedItem() == null) {
			txtAreaLog.appendText("Vyberte typ.\n");
			return;
		}
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "INSERT INTO zviera (name,hungry,weight,type_id,date_added) " + "VALUES ('"
				+ txtAnimalName.getText() + "'," + hungryChecked() + ", " + txtWeight.getText() + " , '"
				+ getIdFromTypeofAnimal(1) + "', CURRENT_DATE );";
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
		txtAreaLog.appendText("⁄speöne vytvoren˝ z·znam: Meno: " + txtAnimalName.getText() + ", Vaha: "
			+ txtWeight.getText() + ", Typ zvierata:" + comboBoxType.getSelectionModel().getSelectedItem()
			+ ", Bolo hladnÈ: " + comboBoxHungry.getSelectionModel().getSelectedItem() + ".\n");
	}
	@FXML
	private void btnSearchAction(ActionEvent event) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		String hungry = null;
		String name = null;
		ArrayList<Zviera> animals = new ArrayList<Zviera>();
		if (comboBoxHungrySearch.getSelectionModel().getSelectedItem() != null) {
			hungry = hungrySearchChecked();
		}
		name = txtAnimalSearchName.getText();
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(queryParser(name, hungry));
			while (rs.next()) {
				System.out.println();
				Zviera zviera = new Zviera();
				zviera.setDid(rs.getInt("did"));
				zviera.setName(rs.getString("name"));
				zviera.setWeight(rs.getInt("weight"));
				zviera.setDate_added(rs.getDate("date_added"));
				zviera.setChliev_id(rs.getInt("chliev_id"));
				zviera.setHungry(rs.getBoolean("hungry"));
				zviera.setType_id(rs.getString("type_id"));
				animals.add(zviera);
			}
			rs.close();
			stmt.close();
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
		txtAreaLog.appendText("N·jdenÈ z·znamy: \n");
		for (Zviera zviera : animals) {
			txtAreaLog.appendText(zviera.information());
			txtAreaLog.appendText("\n");
		}
		System.out.println("Sucessful filter.");

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
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		comboBoxHungry.setItems(listHungry);
		comboBoxHungrySearch.setItems(listHungry);
		comboBoxType.setItems(listType);
		comboBoxTypeSearch.setItems(listType);
	}

	private Boolean checkIfCorrect() {
		for (char c : txtWeight.getText().toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private String hungryChecked() {
		String hungry = comboBoxHungry.getSelectionModel().getSelectedItem();
		if (hungry.equals("Ano")) {
			return "TRUE";
		} else {
			return "FALSE";
		}
	}

	private String hungrySearchChecked() {
		String hungry = comboBoxHungrySearch.getSelectionModel().getSelectedItem();
		if (hungry.equals("Ano")) {
			return "TRUE";
		} else {
			return "FALSE";
		}
	}

	private String getIdFromTypeofAnimal(int i) throws ClassNotFoundException {
		String type = null;
		if (i == 1) {
			type = comboBoxType.getSelectionModel().getSelectedItem();
		} else {
			type = comboBoxTypeSearch.getSelectionModel().getSelectedItem();
		}
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
				} catch (SQLException ex1) {
					System.err.println(ex1.getClass().getName() + ": " + ex1.getMessage());
					System.exit(0);
				}
			}
		}
		return code;
	}

	private String queryParser(String name, String hungry) throws ClassNotFoundException {
		String id = null;
		String query = null;
		String type = comboBoxTypeSearch.getSelectionModel().getSelectedItem();
		if (name == null && hungry == null && type == null) {
			return "SELECT * FROM zviera ";
		} else {
			query = "SELECT * FROM zviera WHERE ";
			if (type != null) {
				id = getIdFromTypeofAnimal(2);
				query = query + "type_id LIKE '%" + id + "%' ";
			}
			if (type != null && name != null) {
				query = query + "AND ";
			}
			if (name != null) {
				query = query + "name LIKE '%" + name + "%' ";
			}
			if (hungry != null && (type != null || name != null)) {
				query = query + "AND ";
			}
			if (hungry != null) {
				query = query + "hungry=" + hungry + "";
			}
		}
		query = query + ";";
		return query;
	}

}
