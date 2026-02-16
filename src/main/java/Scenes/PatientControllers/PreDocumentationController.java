package Scenes.PatientControllers;

import MyAPI.PatientService;
import Scenes.EntryMode;
import Scenes.OnShowAware;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PreDocumentationController implements OnShowAware {

    private int patientID;
    @FXML
    private TextArea preDocumentationField;

    @Override
    public void onShow(EntryMode mode, Integer id) {
        patientID = id;
        preDocumentationField.setText(PatientService.getPreDocumentation(patientID));
    }

    @FXML
    public void goBack(){
        SceneManager.switchTo("PATIENT_VIEW", EntryMode.READ_ONLY, patientID);
    }
}
