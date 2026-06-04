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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochedykovdev.laba7.dao.ToolDao;
import ru.kochedykovdev.laba7.dao.ToolIssueDao;
import ru.kochedykovdev.laba7.model.Tool;
import ru.kochedykovdev.laba7.model.ToolIssue;
import ru.kochedykovdev.laba7.util.Messages;
import ru.kochedykovdev.laba7.util.FieldValidation;
import ru.kochedykovdev.laba7.util.UiAlerts;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReturnToolController {

    private static final Logger logger = LoggerFactory.getLogger(ReturnToolController.class);

    private final ToolIssueDao toolIssueDao;
    private final ToolDao toolDao;

    private final Map<Long, String> toolNames = new HashMap<>();

    @FXML
    private ComboBox<ToolIssue> toolIssueComboBox;
    @FXML
    private DatePicker actualReturnDatePicker;
    @FXML
    private TextField conditionField;
    @FXML
    private Button submitButton;

    public ReturnToolController(ToolIssueDao toolIssueDao, ToolDao toolDao) {
        this.toolIssueDao = toolIssueDao;
        this.toolDao = toolDao;
    }

    @FXML
    private void initialize() {
        try {
            for (Tool tool : toolDao.findAll()) {
                toolNames.put(tool.getId(), tool.getName());
            }

            toolIssueComboBox.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(ToolIssue item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : formatIssue(item));
                }
            });
            toolIssueComboBox.setButtonCell(toolIssueComboBox.getCellFactory().call(null));

            toolIssueComboBox.getItems().setAll(
                    toolIssueDao.findAll().stream()
                            .filter(i -> i.getActualReturnDate() == null)
                            .toList()
            );
            actualReturnDatePicker.setValue(java.time.LocalDate.now());

            FieldValidation.bindSubmit(submitButton, Bindings.createBooleanBinding(
                    () -> toolIssueComboBox.getValue() != null
                            && actualReturnDatePicker.getValue() != null
                            && !conditionField.getText().isBlank(),
                    toolIssueComboBox.valueProperty(),
                    actualReturnDatePicker.valueProperty(),
                    conditionField.textProperty()
            ));
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        }
    }

    @FXML
    private void onCloseClick(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    private void onSubmitClick(ActionEvent event) {
        try {
            ToolIssue issue = toolIssueComboBox.getValue();
            toolIssueDao.returnTool(
                    issue.getId(),
                    actualReturnDatePicker.getValue(),
                    conditionField.getText().trim()
            );
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private String formatIssue(ToolIssue issue) {
        String name = toolNames.getOrDefault(issue.getToolId(), "инструмент");
        return name + " — " + issue.getIssuedTo();
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
