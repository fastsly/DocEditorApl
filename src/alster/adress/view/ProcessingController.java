package alster.adress.view;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import alster.adress.MainApp;
import alster.adress.model.Person;
import alster.adress.util.DateUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFileChooser;

import alster.adress.util.FileEditor;

public class ProcessingController {

	@FXML
    private Label inputField;
    @FXML
    private Label outputField;
    @FXML
    private Label excelField;
    @FXML
    private TextField week1OutField;
    @FXML
    private TextField week2OutField;
    @FXML
    private TextField week3OutField;
    @FXML
    private TextField week4OutField;

    private MainApp mainApp;

	private Stage dialogStage;
	private boolean okClicked = false;

	private FileEditor fileEditor= new FileEditor();

	@FXML
    private void initialize() {
    }

	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	public boolean isOkClicked() {
        return okClicked;
    }

	@FXML
    private void handleInput() {

		DirectoryChooser fileChooser = new DirectoryChooser();

        // Show open file dialog
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());
        fileEditor.setInPath(file.getPath());
        System.out.println(inputField.getText());
        if (file.getPath() != null) {
        inputField.setText(file.getPath());
        }
        else
        {
        	inputField.setText("");
        }
    }

	@FXML
    private void handleExcel() {

		FileChooser fileChooser = new FileChooser();

        // Show open file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        fileEditor.setExcelPath(file.getPath());
        System.out.println(inputField.getText());
        if (file.getPath() != null) {
        excelField.setText(file.getPath());
        }
        else
        {
        	excelField.setText("");
        }
    }
	
	@FXML
    private void handleOutput() {

		DirectoryChooser fileChooser = new DirectoryChooser();

        // Show open file dialog
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());
        fileEditor.setOutPath(file.getPath());
        //System.out.println(ouputField.getText());
        if (file.getPath() != null) {
        outputField.setText(file.getPath());
        }
        else
        {
        	outputField.setText("");
        }
    }

	@FXML
    private void handleOk() {
        //if (isInputValid()) {
            fileEditor.setInPath(inputField.getText());
            fileEditor.setOutPath(outputField.getText());
            fileEditor.setWeek1Out(week1OutField.getText());
            fileEditor.setWeek2Out(week2OutField.getText());
            fileEditor.setWeek3Out(week3OutField.getText());
            fileEditor.setWeek4Out(week4OutField.getText());
            fileEditor.setExcelPath(excelField.getText());
            fileEditor.setMainApp(mainApp);
            try{
            fileEditor.edit(true);
            System.out.println("Finished fileEditor.edit");
            }
            catch (Exception e){
            	// insert check for docx
            	System.out.println("You've got an error bitch at processing controller fileEditor.edit: "+e);
            }
            okClicked = true;
            dialogStage.close();
       // }
    }

	@FXML
	private void handleProcessDosarSocial(){
		//fileEditor.setInPath(inputField.getText());
		fileEditor.setMainApp(mainApp);
		try{
            fileEditor.edit(false);
            }
            catch (Exception e){
            	// insert check for docx
            	System.out.println("You've got an error bitch"+e);
            }
		dialogStage.close();
	}

	@FXML
	private void handleSelectFolder(){
		DirectoryChooser fileChooser = new DirectoryChooser();

        // Show open file dialog
        File file = fileChooser.showDialog(mainApp.getPrimaryStage());
        fileEditor.setInPath(file.getPath());
        //System.out.println(inputField.getText());
        if (file.getPath() != null) {
        fileEditor.setInPath(file.getPath());
        }
        else
        {
        	fileEditor.setInPath("");
        }
    }


	@FXML
    private void handleCancel() {
        dialogStage.close();
    }

	public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
