package app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class LoginControler implements Initializable {

	@FXML
	private Label lb1Message;
	@FXML
	private Label statusLabel;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField pswdField;

	private String password;

	private String username;

	@FXML
	private void btnLoginAction(ActionEvent event) throws IOException {

		String psswd = pswdField.getText();
		String user = usernameField.getText();
		Connection c = null;
		Statement stmt = null;
		try {

			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5121/polnohosp", "postgres", "chidorinagashi");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(did) AS suma FROM Login;");
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("suma");
			}
			if (id == 0) {
				statusLabel.setText("Pouzivatel neexistuje.");
				return;
			}

			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Login;");
			while (rs.next()) {
				id = rs.getInt("did");
				username = rs.getString("name");
				password = rs.getString("password");
				System.out.println("ID = " + id);
				System.out.println("NAME = " + username);
				System.out.println("PASSWORD = " + password);
				System.out.println();
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		if (psswd.equals(password) && user.equals(username)) {
			System.out.println("Login success");
			URL menuBarUrl = getClass().getResource("/gui/Menu.fxml");
			MenuBar bar = FXMLLoader.load(menuBarUrl);

			BorderPane border = Main.getRoot();
			border.setTop(bar);

			statusLabel.setText("Uspesne prihlasenie. Vitajte!");
		} else {
			statusLabel.setText("Nepsravne meno alebo heslo.");
		}
	}

	@FXML
	void btnRegistrationAction(ActionEvent event) {

		try {

			URL registrationURL = getClass().getResource("/gui/Registration.fxml");
			AnchorPane registrationPane = FXMLLoader.load(registrationURL);

			BorderPane border = Main.getRoot();
			border.setCenter(registrationPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

}
