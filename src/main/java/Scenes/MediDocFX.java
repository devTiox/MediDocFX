package Scenes;

import Data.DataBase.DataBase;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.SQLException;

public class MediDocFX extends Application {

    @Override
    public void start(Stage stage){
        try {
            DataBase.setUp();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SceneManager.setStage(stage);
        SceneManager.switchTo("MENU",null, null);
        stage.setTitle("MediDoc-0.1");

        stage.show();
    }
}
