package ru.kochedykovdev.laba7.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.kochedykovdev.laba7.dao.BuildingObjectDao;
import ru.kochedykovdev.laba7.dao.ToolDao;
import ru.kochedykovdev.laba7.dao.ToolIssueDao;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.model.Tool;
import ru.kochedykovdev.laba7.model.ToolIssue;
import ru.kochedykovdev.laba7.util.Messages;
import ru.kochedykovdev.laba7.util.FieldValidation;

import java.sql.SQLException;

public class GiveToolController {

    private final ToolDao toolDao;
    private final BuildingObjectDao buildingObjectDao;
    private final ToolIssueDao toolIssueDao;

    @FXML
    private ComboBox<Tool> toolComboBox;
    @FXML
    private ComboBox<BuildingObject> objectComboBox;
    @FXML
    private TextField issuedToField;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Button submitButton;

    public GiveToolController(ToolDao toolDao,
                              BuildingObjectDao buildingObjectDao,
                              ToolIssueDao toolIssueDao) {
        this.toolDao = toolDao;
        this.buildingObjectDao = buildingObjectDao;
        this.toolIssueDao = toolIssueDao;
    }

    @FXML
    private void initialize() {
        try {
            setupCombo(toolComboBox);
            setupCombo(objectComboBox);
            toolComboBox.getItems().setAll(toolDao.findAll());
            objectComboBox.getItems().setAll(buildingObjectDao.findAll());
            issueDatePicker.setValue(java.time.LocalDate.now());

            FieldValidation.bindSubmit(submitButton, Bindings.createBooleanBinding(
                    () -> toolComboBox.getValue() != null
                            && objectComboBox.getValue() != null
                            && !issuedToField.getText().isBlank()
                            && issueDatePicker.getValue() != null
                            && returnDatePicker.getValue() != null
                            && !returnDatePicker.getValue().isBefore(issueDatePicker.getValue()),
                    toolComboBox.valueProperty(),
                    objectComboBox.valueProperty(),
                    issuedToField.textProperty(),
                    issueDatePicker.valueProperty(),
                    returnDatePicker.valueProperty()
            ));
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onCloseClick(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        try {
            ToolIssue issue = new ToolIssue(
                    null,
                    toolComboBox.getValue().getId(),
                    objectComboBox.getValue().getId(),
                    issuedToField.getText().trim(),
                    issueDatePicker.getValue(),
                    returnDatePicker.getValue(),
                    null
            );
            toolIssueDao.insert(issue);
            closeWindow(event);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private static <T> void setupCombo(ComboBox<T> comboBox) {
        comboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(Messages.bundle.getString("error.header"));
        alert.setContentText(message);
        alert.showAndWait();
    }
}
