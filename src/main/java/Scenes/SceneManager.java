package Scenes;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SceneManager {

    private static Stage stage;

    private static final Map<String, String> scenes = new HashMap<>();

    static {
        scenes.put("MENU", "menu-view.fxml");
        scenes.put("BASIC_DATA", "basic-data.fxml");
        scenes.put("PATIENT_VIEW", "patient-view.fxml");
        scenes.put("PATIENTS_LIST", "patients-list.fxml");
        scenes.put("VISIT_VIEW", "visit-view.fxml");
        scenes.put("CHECK_BOXES", "check-boxes.fxml");
        scenes.put("PREDOCUMENTATION_SCENE", "predocumentation-view.fxml");
    }

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchTo(String sceneKey, EntryMode mode, Integer id) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(SceneManager.class.getResource(scenes.get(sceneKey)));

            Parent view = loader.load();
            Object controller = loader.getController();

            if (controller instanceof OnShowAware aware) {
                aware.onShow(mode, id);
            }
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

            Scene scene = switch(sceneKey){
                case "MENU", "BASIC_DATA", "PATIENTS_LIST"
                        -> new Scene(view, bounds.getWidth()*0.3, bounds.getHeight() *0.8);
                default -> new Scene(view, bounds.getWidth()*0.8, bounds.getHeight() *0.8);
            };

            scene.getStylesheets().add(
                    Objects.requireNonNull(SceneManager.class.getResource("app.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean confirmYesNo(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yes = new ButtonType("Tak");
        ButtonType no  = new ButtonType("Nie");

        alert.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yes;
    }

    public static void openPatientView(int id) {
        switchTo("PATIENT_VIEW", EntryMode.READ_ONLY, id);
    }

    public static void updatePatient(int patientID){
        switchTo("BASIC_DATA", EntryMode.EDIT, patientID);
    }

    public static void addPatient() {
        switchTo("BASIC_DATA", EntryMode.NEW, null);
    }
}
