package ru.kochedykovdev.laba7.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void onPrihodClick(ActionEvent event) {
        openWindow("prihod-view.fxml", "Приход материала", event);
    }

    @FXML
    private void onGiveMaterialClick(ActionEvent event) {
        openWindow("give-material-view.fxml", "Выдача материала", event);
    }

    @FXML
    private void onGiveToolClick(ActionEvent event) {
        openWindow("give-tool-view.fxml", "Выдача инструмента", event);
    }

    @FXML
    private void onReturnToolClick(ActionEvent event) {
        openWindow("return-tool-view.fxml", "Возврат инструмента", event);
    }

    private void openWindow(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kochedykovdev/laba7/" + fxmlFile));
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Stage) ((Node) event.getSource()).getScene().getWindow()));

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}