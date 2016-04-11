package app;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	// Creating a static root to pass to the controller
	private static BorderPane root = new BorderPane();

	/**
	 * Just a root getter for the controller to use
	 */
	public static BorderPane getRoot() {
		return root;
	}

	@Override
	public void start(Stage primaryStage) throws IOException {

		// loading FXML resources
		// note that we don't need PaneTwo in this class

		URL loginPaneURL = getClass().getResource("/gui/Login.fxml");
		AnchorPane loginPane = FXMLLoader.load(loginPaneURL);

		// constructing our scene using the static root

		root.setCenter(loginPane);

		Scene scene = new Scene(root, 650, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.setTitle("Hospodársky systém");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}