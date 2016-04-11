package app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class RegistrationControler implements Initializable {

	@FXML
	private Label labelStat;
	@FXML
	private TextField txtUserName;
	@FXML
	private PasswordField txtNewPasswd;
	@FXML
	private PasswordField txtOldPasswd;

	private String username;
	private String oldPassword;

	@FXML
	private void btnRegistrationAction(ActionEvent event) throws ClassNotFoundException {
		String oldPassword = txtOldPasswd.getText();
		String newPassword = txtNewPasswd.getText();
		String user = txtUserName.getText();

		if (newPassword == null) {
			labelStat.setText("Nove heslo nesmie byt NULL");
			return;
		}

		Connection c = null;
		Statement stmt = null;
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Login;");
			while (rs.next()) {
				username = rs.getString("name");
				this.oldPassword = rs.getString("password");
				System.out.println("NAME = " + username);
				System.out.println("PASSWORD = " + this.oldPassword);
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
			if (oldPassword.equals(this.oldPassword) && user.equals(username)) {
				System.out.println("Operation done successfully");
				updatePassword(newPassword);
			} else {
				labelStat.setText("Nepsravne meno alebo heslo.");
			}
		}
	}

	@FXML
	private void btnLoginAction(ActionEvent event) {
		try {

			URL loginURL = getClass().getResource("/gui/Login.fxml");
			AnchorPane loginPane = FXMLLoader.load(loginURL);

			BorderPane border = Main.getRoot();
			border.setCenter(loginPane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	private void updatePassword(String newPassword) throws ClassNotFoundException {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "UPDATE Login set password = '" + newPassword + "' where name='" + username + "';";
			stmt.executeUpdate(sql);
			c.commit();

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Login;");
			while (rs.next()) {
				username = rs.getString("name");
				this.oldPassword = rs.getString("password");
				System.out.println("NAME = " + username);
				System.out.println("PASSWORD = " + this.oldPassword);
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
		System.out.println("Operation done successfully");
		labelStat.setText("Úspešne ste zmenili heslo.");
	}
}
