package Scenes.Controllers;

import Scenes.SceneManager;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    public void exit(){
        System.exit(0);
    }

    @FXML
    public void showPatientsList(){
        SceneManager.switchTo("PATIENTS_LIST", null, null);
    }

    @FXML
    public void addPatient(){
        SceneManager.addPatient();
    }
}
