package alster.adress;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.List;
import java.io.File;
import alster.adress.model.Person;
import alster.adress.view.PersonOverwiewController;
import alster.adress.view.PersonEditDialogController;
import alster.adress.model.PersonListWrapper;
import alster.adress.view.RootLayoutController;
import alster.adress.view.ProcessingController;


public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonOverview();
	}



	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
         // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

         // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }

     // Try to load last opened person file.
        File file = getPersonFilePath();
        if (file != null) {
        loadPersonDataFromFile(file);
        }
    }


	public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/Person.overview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

         // Give the controller access to the main app.
            PersonOverwiewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public Stage getPrimaryStage() {
        return primaryStage;
    }

	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 *
	 * @param person the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showPersonEditDialog(Person person) {
	    try {
	        // Load the fxml file and create a new stage for the popup dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/alster.adress.view.PersonEditDialog.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edit Person");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        PersonEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPerson(person);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public boolean showProcessDialog (){
		try{

			FXMLLoader loader1 = new FXMLLoader();
	        loader1.setLocation(MainApp.class.getResource("view/ProcessingDialog1.fxml"));
	        AnchorPane page1 = (AnchorPane) loader1.load();

	        // Create the dialog Stage.
	        Stage dialogStage1 = new Stage();
	        dialogStage1.setTitle("Process Files");
	        dialogStage1.initModality(Modality.WINDOW_MODAL);
	        dialogStage1.initOwner(primaryStage);
	        Scene scene1 = new Scene(page1);
	        dialogStage1.setScene(scene1);

	        // Set the person into the controller.
	        ProcessingController controller1 = loader1.getController();
	        controller1.setDialogStage(dialogStage1);
	        controller1.setMainApp(this);

	        // Show the dialog and wait until the user closes it
	        dialogStage1.showAndWait();

	        return controller1.isOkClicked();
		} catch(IOException e) {
	        e.printStackTrace();
	        return false;
		}

		}

	public boolean showDosarSocProcessDialog (){
		try{

			FXMLLoader loader2 = new FXMLLoader();
	        loader2.setLocation(MainApp.class.getResource("view/DoasrSocialView.fxml"));
	        AnchorPane page2 = (AnchorPane) loader2.load();

	        // Create the dialog Stage.
	        Stage dialogStage2 = new Stage();
	        dialogStage2.setTitle("Process Files");
	        dialogStage2.initModality(Modality.WINDOW_MODAL);
	        dialogStage2.initOwner(primaryStage);
	        Scene scene2 = new Scene(page2);
	        dialogStage2.setScene(scene2);

	        // Set the person into the controller.
	        ProcessingController controller2 = loader2.getController();
	        controller2.setDialogStage(dialogStage2);
	        controller2.setMainApp(this);

	        // Show the dialog and wait until the user closes it
	        dialogStage2.showAndWait();

	        return controller2.isOkClicked();
		} catch(IOException e) {
	        e.printStackTrace();
	        return false;
		}

		}

	/**
	 * Returns the person file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 *
	 * @return
	 */
	public File getPersonFilePath() {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 *
	 * @param file the file or null to remove the path
	 */
	public void setPersonFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // Update the stage title.
	        primaryStage.setTitle("App pt Centru - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // Update the stage title.
	        primaryStage.setTitle("App pt Centru");
	    }
	}

	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 *
	 * @param file
	 */
	public void loadPersonDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

	        personData.clear();
	        personData.addAll(wrapper.getPersons());

	        // Save the file path to the registry.
	        setPersonFilePath(file);

	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	/**
	 * Saves the current person data to the specified file.
	 *
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // Wrapping our person data.
	        PersonListWrapper wrapper = new PersonListWrapper();
	        wrapper.setPersons(personData);

	        // Marshalling and saving XML to the file.
	        m.marshal(wrapper, file);

	        // Save the file path to the registry.
	        setPersonFilePath(file);
	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	public static void main(String[] args) {
		launch(args);


	}

	public List<Person> getPersonList(){
		return personData;
	}

	public void setPersonList (List<Person> list){
		personData.clear();
        personData.addAll(list);
	}
}
