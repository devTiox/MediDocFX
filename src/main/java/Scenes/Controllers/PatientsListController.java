package Scenes.Controllers;

import Data.Models.PatientPreView;
import MyAPI.PatientsList;
import Scenes.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class PatientsListController {

    @FXML
    private Pagination pagination;

    private static final int PAGE_SIZE = 20;

    @FXML
    public void initialize() {

        List<PatientPreView> patients = PatientsList.getPatients();

        System.out.println("Patients loaded: " + patients.size());

        int pageCount = (int) Math.ceil(
                (double) patients.size() / PAGE_SIZE
        );

        pagination.setPageCount(Math.max(pageCount, 1));
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(
                pageIndex -> createPage(pageIndex, patients)
        );
    }

    private Node createPage(int pageIndex, List<PatientPreView> patients) {

        VBox box = new VBox(5);
        box.setPadding(new Insets(10));

        int from = pageIndex * PAGE_SIZE;
        int to = Math.min(from + PAGE_SIZE, patients.size());

        for (int i = from; i < to; i++) {
            PatientPreView p = patients.get(i);
            box.getChildren().add(createPatientButton(p));
        }

        ScrollPane scrollPane = new ScrollPane(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private Button createPatientButton(PatientPreView p) {

        Button btn = new Button(
                p.id() + " | " +
                        p.lastName() + " " + p.name() +
                        " | " + p.phoneNumber()
        );

        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setOnAction(_ -> onPatientClicked(p));

        return btn;
    }

    private void onPatientClicked(PatientPreView p) {
        SceneManager.openPatientView(p.id());
    }

    @FXML
    public void goBack(){
        SceneManager.switchTo("MENU", null, null);
    }
}
