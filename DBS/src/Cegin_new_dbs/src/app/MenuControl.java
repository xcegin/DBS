package app;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Note that we load the panes with the FXMLLoader
 * on every use. This allows us to manipulate the
 * CSS between loads and have it take affect.
 *
 * Also, the panes should not save state internally.
 * Reloading the FXML forces better programming
 * design, because it is impossible to get lazy
 * and expect the panes to save their own state.
 */
public class MenuControl {

	@FXML
	private MenuItem menuFind; // Value injected by FXMLLoader

	@FXML
	private MenuItem menuStatistic; // Value injected by FXMLLoader

	@FXML
	private MenuItem menuDelete;

	@FXML
	private MenuItem menuEmploy;

	@FXML
	private MenuItem menuPlant;

	@FXML
	private MenuItem menuTraktor;

	@FXML
	private MenuItem menuSearch;

	@FXML
	private MenuItem menuKoniec;

	@FXML
	void menuAddAction(ActionEvent event) {

		try {

			URL paneAddURL = getClass().getResource("/gui/AnimalAdd.fxml");
			AnchorPane animalAddPane = FXMLLoader.load(paneAddURL);

			BorderPane border = Main.getRoot();
			border.setCenter(animalAddPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuStatisticsAction(ActionEvent event) {

		try {

			URL statisticsURL = getClass().getResource("/gui/Statistics.fxml");
			AnchorPane statisticsPane = FXMLLoader.load(statisticsURL);

			BorderPane border = Main.getRoot();
			border.setCenter(statisticsPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuDeleteAction(ActionEvent event) {

		try {

			URL paneDeleteURL = getClass().getResource("/gui/AnimalDelete.fxml");
			AnchorPane deletePane = FXMLLoader.load(paneDeleteURL);

			BorderPane border = Main.getRoot();
			border.setCenter(deletePane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuEmployAction(ActionEvent event) {

		try {

			URL employURL = getClass().getResource("/gui/Employ.fxml");
			AnchorPane employPane = FXMLLoader.load(employURL);

			BorderPane border = Main.getRoot();
			border.setCenter(employPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuTraktorAction(ActionEvent event) {

		try {

			URL traktorURL = getClass().getResource("/gui/Traktor.fxml");
			AnchorPane traktorPane = FXMLLoader.load(traktorURL);

			BorderPane border = Main.getRoot();
			border.setCenter(traktorPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuPlantAction(ActionEvent event) {

		try {

			URL plantURL = getClass().getResource("/gui/Plant.fxml");
			AnchorPane plantPane = FXMLLoader.load(plantURL);

			BorderPane border = Main.getRoot();
			border.setCenter(plantPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuFindAction(ActionEvent event) {

		try {

			URL findURL = getClass().getResource("/gui/Find.fxml");
			AnchorPane findPane = FXMLLoader.load(findURL);

			BorderPane border = Main.getRoot();
			border.setCenter(findPane);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void menuKoniecAction(ActionEvent event) {

		System.exit(0);

	}

}