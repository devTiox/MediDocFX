package Scenes.PatientControllers;

import Data.Models.Visit;
import MyAPI.VisitsService;
import Scenes.EntryMode;
import Scenes.OnShowAware;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VisitController implements OnShowAware {

    private EntryMode mode;
    private int patientID;
    private int visitID;

    @FXML private TextArea treatmentField;
    @FXML private TextArea notesField;
    @FXML private Button button;
    @FXML private Button deleteButton;

    @Override
    public void onShow(EntryMode mode, Integer id) {
        this.mode = mode;

        switch(mode){
            case EntryMode.NEW -> {
                patientID = id;
                clearFields();
                button.setText("Zapisz");
                button.setOnAction(_ -> save());
                deleteButton.setDisable(true);
                deleteButton.setVisible(false);
            }
            case EntryMode.READ_ONLY -> {
                this.visitID = id;
                Visit visit = VisitsService.getVisit(visitID);
                patientID = visit.patientID();
                fillFields(visit);
                treatmentField.setEditable(false);
                notesField.setEditable(false);
                button.setText("Edytuj");
                button.setOnAction(_ -> editVisit(visitID));
            }
            case EntryMode.EDIT -> {
                visitID = id;
                Visit visit = VisitsService.getVisit(visitID);
                patientID = visit.patientID();
                fillFields(visit);
                button.setText("Zapisz");
                button.setOnAction(_ -> save());
                deleteButton.setDisable(true);
                deleteButton.setVisible(false);
            }
        }
    }

    private void fillFields(Visit visit) {
        treatmentField.setText(visit.treatment());
        notesField.setText(visit.notes());
    }

    private void clearFields() {
        treatmentField.clear();
        notesField.clear();
    }

    public void editVisit(int visitID){
        SceneManager.switchTo("VISIT_VIEW", EntryMode.EDIT, visitID);
    }

    @FXML
    public void goBack(){
        switch(mode){
            case EntryMode.NEW, EntryMode.READ_ONLY -> SceneManager.switchTo("PATIENT_VIEW", EntryMode.READ_ONLY, patientID);
            case EntryMode.EDIT -> SceneManager.switchTo("VISIT_VIEW", EntryMode.READ_ONLY, visitID);

        }
    }

    @FXML
    private void save(){

        LocalDate date = LocalDate.from(LocalDateTime.now());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        switch(mode){
            case EntryMode.NEW -> {
                this.visitID = VisitsService.insertNewVisit(new Visit(0,formattedDate, treatmentField.getText(),notesField.getText(),patientID));
                SceneManager.switchTo("VISIT_VIEW",EntryMode.READ_ONLY, visitID);
            }
            case EntryMode.EDIT -> {
                VisitsService.updateVisit(new Visit(visitID,formattedDate,treatmentField.getText(),notesField.getText(),patientID));
                goBack();
            }
        }
        treatmentField.getText();
        notesField.getText();
    }

    @FXML
    public void delete() {
        if (SceneManager.confirmYesNo("Potwierdzenie", "Usuwanie Wizyty", "Na pewno chcesz usunąć wizyte?")) {
            VisitsService.deletePatient(visitID);
            goBack();
        }
    }
}
