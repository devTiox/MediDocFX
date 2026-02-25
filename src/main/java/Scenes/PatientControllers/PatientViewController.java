package Scenes.PatientControllers;

import Data.Models.Patient;
import Data.Models.PatientPreView;
import Data.Models.VisitView;
import MyAPI.PatientService;
import MyAPI.PatientsList;
import MyAPI.VisitsService;
import Scenes.EntryMode;
import Scenes.OnShowAware;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.List;

public class PatientViewController implements OnShowAware {

    @FXML
    public TextArea docArea;
    @FXML
    private VBox visitsBox;

    private Patient currentPatient;

    private void openPatientView(int patientID) {
        if(!PatientService.loadPatient(patientID)){
            System.out.println("Error");
        }
        currentPatient = PatientService.getPatient();
        docArea.setText(currentPatient.toString());
        loadVisits(patientID);
    }

    private void loadVisits(int id){
        visitsBox.getChildren().clear();
        List<VisitView> visitList = VisitsService.getPatientsVisits(id);
        for(int i = visitList.size()-1; i >= 0 ; i--){
            VisitView v = visitList.get(i);
            Button b = new Button("Wizyta:" + v.date());
            b.setMaxWidth(Double.MAX_VALUE);

            b.setOnAction(_ -> openVisit(v.visitID()));

            visitsBox.getChildren().add(b);
        }
    }

    public void openVisit(int visitID){
        SceneManager.switchTo("VISIT_VIEW", EntryMode.READ_ONLY, visitID);
    }

    public void newVisit(){
        SceneManager.switchTo("VISIT_VIEW", EntryMode.NEW, currentPatient.getID());
    }

    public void editPatientData(){
        SceneManager.updatePatient(currentPatient.getID());
    }

    @Override
    public void onShow(EntryMode mode, Integer id) {
        openPatientView(id);
    }

    @FXML
    public void goBack(){
        SceneManager.switchTo("PATIENTS_LIST", null, null);
    }

    @FXML
    public void delete(){
        if(SceneManager.confirmYesNo("Potwierdzenie", "Usuwanie Pacjenta","Na pewno chcesz usunąć pacjenta??")) {
            if(!PatientService.deletePatient(currentPatient)) return;
            PatientsList.deletePatient(new PatientPreView(currentPatient.getID(),
                    currentPatient.getLastName(), currentPatient.getName(), currentPatient.getPhoneNumber()));

            goBack();
        }
    }

    public void showPreDocumentation() {
        SceneManager.switchTo("PREDOCUMENTATION_SCENE", EntryMode.READ_ONLY, currentPatient.getID());
    }
}
