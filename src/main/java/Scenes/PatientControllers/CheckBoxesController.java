package Scenes.PatientControllers;

import Data.Lists;
import Data.Models.Patient;
import Data.Models.PatientPreView;
import MyAPI.PatientService;
import MyAPI.PatientsList;
import Scenes.EntryMode;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckBoxesController {

    @FXML
    private Accordion accordion;

    private boolean submitted = false;
    private final Map<String, List<CheckBox>> categoryMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        addCategory("Objawy trawienne", Lists.DIGESTIVE);
        addCategory("Objawy moczowe", Lists.URINARY);
        addCategory("Objawy psychiczne", Lists.MENTAL);
        addCategory("Objawy mięśniowo-szkieletowe", Lists.MUSCULO);
        addCategory("Objawy fizyczne", Lists.PHYSICAL);
        addCategory("Inne objawy", Lists.OTHER);
    }

    private void addCategory(String name, List<String> items) {

        VBox box = new VBox(6);
        box.setPadding(new Insets(8));

        List<CheckBox> checkBoxes = new ArrayList<>();

        for (String item : items) {
            CheckBox cb = new CheckBox(item);
            checkBoxes.add(cb);
            box.getChildren().add(cb);
        }

        categoryMap.put(name, checkBoxes);

        TitledPane pane = new TitledPane(name, box);
        pane.setExpanded(false);

        accordion.getPanes().add(pane);
    }

    public void submitCheckBoxes(){
        if (submitted) return;
        submitted = true;

        Patient patient = Patient.newPatient;

        patient.setPreDocumentation(
                makePreDocumentation(getSelectedByCategory())
        );

        int id = PatientService.addPatient(patient);
        boolean check = PatientService.loadPatient(id) &&
                PatientsList.addPatient(new PatientPreView(id, patient.getLastName(), patient.getName(), patient.getPhoneNumber()));
        if(!check){
            return;
        }

        Patient.newPatient = null; // 🔥 zamykamy sesję

        SceneManager.openPatientView(id);
    }

    // 🔥 ODCZYT ZAZNACZEŃ Z PODZIAŁEM NA KATEGORIE
    private Map<String, List<String>> getSelectedByCategory() {

        Map<String, List<String>> result = new LinkedHashMap<>();

        for (var entry : categoryMap.entrySet()) {
            List<String> selected = new ArrayList<>();

            for (CheckBox cb : entry.getValue()) {
                if (cb.isSelected()) {
                    selected.add(cb.getText());
                }
            }

            if (!selected.isEmpty()) {
                result.put(entry.getKey(), selected);
            }
        }

        return result;
    }

    private String makePreDocumentation(Map<String,List<String>> result){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, List<String>> entry : result.entrySet()){
            sb.append(entry.getKey()).append(":\n");
            for(String symptom : entry.getValue()){
                sb.append("-").append(symptom).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @FXML
    public void goBack(){
        SceneManager.switchTo("BASIC_DATA", EntryMode.RE_EDIT, null);
    }
}
