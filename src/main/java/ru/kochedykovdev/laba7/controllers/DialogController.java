package ru.kochedykovdev.laba7.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Общий контроллер диалоговых форм - только закрытие окна
 */
public class DialogController {

    @FXML
    private void onCloseClick(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
