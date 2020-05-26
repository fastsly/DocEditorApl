package alster.adress.view;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;

import alster.adress.MainApp;
import alster.adress.model.Person;
import alster.adress.util.DateUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class PersonOverwiewController {

	    @FXML
	    private TableView<Person> personTable;
	    @FXML
	    private TableColumn<Person, String> firstNameColumn;
	    @FXML
	    private TableColumn<Person, String> lastNameColumn;

	    @FXML
	    private Label firstNameLabel;
	    @FXML
	    private Label lastNameLabel;
	    @FXML
	    private Label streetLabel;
	    @FXML
	    private Label postalCodeLabel;
	    @FXML
	    private Label cityLabel;
	    @FXML
	    private Label birthdayLabel;
	    @FXML
	    private Label dataNastereLabel;
	    @FXML
	    private Label evalFileLabel;
	    @FXML
	    private Label formatAdaptatFileLabel;
	    @FXML
	    private Label planDeInterventieLabel;
	    @FXML
	    private Label reabSocFileLabel;
	    @FXML
	    private Label reabFunctFileLabel;
	    @FXML
	    private Label absentLabel;
	    @FXML
	 // Reference to the main application.
	    private MainApp mainApp;


	    /**
	     * The constructor.
	     * The constructor is called before the initialize() method.
	     */
	    public PersonOverwiewController() {
	    	mainApp=null;
	    }

	    @FXML
	    private void initialize() {
	        // Initialize the person table with the two columns.
	        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
	        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());


	        // Clear person details.
	        showPersonDetails(null);

	        // Listen for selection changes and show the person details when changed.
	        personTable.getSelectionModel().selectedItemProperty().addListener(
	                (observable, oldValue, newValue) -> showPersonDetails(newValue));

	        PseudoClass highValuePseudoClass = PseudoClass.getPseudoClass("high-value");
	        personTable.setRowFactory(cellData -> new TableRow<Person>() {
	            @Override
	            public void updateItem(Person item, boolean empty) {
	                super.updateItem(item, empty);
	             if (item != null)  {
	            	LocalDate date = item.getBirthday();
	            	date = date.plusMonths(3);
	 		        int comp  = LocalDate.now().compareTo(date);
	            	pseudoClassStateChanged(highValuePseudoClass, (! empty) && comp >= 0);
	             }
	            }
	        });

	    }

	    private void showPersonDetails(Person person) {
	    	int comp;
	        if (person != null) {
	            // Fill the labels with info from the person object.
	            firstNameLabel.setText(person.getFirstName());
	            lastNameLabel.setText(person.getLastName());
	            streetLabel.setText(DateUtil.format(person.getStreet()));
	            postalCodeLabel.setText(DateUtil.format(person.getPostalCode()));
	            cityLabel.setText(DateUtil.format(person.getCity()));
	            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
	            dataNastereLabel.setText(DateUtil.format(person.getDataNastere()));
	            absentLabel.setText(String.valueOf(person.getAbsent()));
	            //evalFileLabel.setText(person.getEvalFileName());
	            //formatAdaptatFileLabel.setText(person.getFormatAdaptatFileName());
	            //planDeInterventieLabel.setText(person.getPlanDeInterventieFileName());
	            //reabSocFileLabel.setText(person.getReabSocFileName());
	            //reabFunctFileLabel.setText(person.getReabFunctFileName());

	        } else {
	            // Person is null, remove all the text.
	            firstNameLabel.setText("");
	            lastNameLabel.setText("");
	            streetLabel.setText("");
	            postalCodeLabel.setText("");
	            cityLabel.setText("");
	            birthdayLabel.setText("");
	            dataNastereLabel.setText("");
	            absentLabel.setText("");
	            //evalFileLabel.setText("");
	            //formatAdaptatFileLabel.setText("");
	            //planDeInterventieLabel.setText("");
	            //reabSocFileLabel.setText("");
	            //reabFunctFileLabel.setText("");
	            comp=0;
	        }


	    }



	    @FXML
	    private void handleDeletePerson() {
	        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

	        if (selectedIndex >= 0) {
	            personTable.getItems().remove(selectedIndex);
	        } else {
	            // Nothing selected.
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("No Selection");
	            alert.setHeaderText("No Person Selected");
	            alert.setContentText("Please select a person in the table.");

	            alert.showAndWait();
	        }
	    }

	    /**
	     * Called when the user clicks the new button. Opens a dialog to edit
	     * details for a new person.
	     */
	    @FXML
	    private void handleNewPerson() {
	        Person tempPerson = new Person();
	        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
	        if (okClicked) {
	            mainApp.getPersonData().add(tempPerson);
	        }
	    }

	    /**
	     * Called when the user clicks the edit button. Opens a dialog to edit
	     * details for the selected person.
	     */
	    @FXML
	    private void handleEditPerson() {
	        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
	        if (selectedPerson != null) {
	            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
	            if (okClicked) {
	                showPersonDetails(selectedPerson);
	            }


	        } else {
	            // Nothing selected.
	            Alert alert = new Alert(Alert.AlertType.WARNING);
	            alert.initOwner(mainApp.getPrimaryStage());
	            alert.setTitle("No Selection");
	            alert.setHeaderText("No Person Selected");
	            alert.setContentText("Please select a person in the table.");

	            alert.showAndWait();
	        }
	        initialize();
	    }



	    /**
	     * Is called by the main application to give a reference back to itself.
	     *
	     * @param mainApp
	     */
	    public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;

	        // Add observable list data to the table
	        personTable.setItems(mainApp.getPersonData());
	    }
}
