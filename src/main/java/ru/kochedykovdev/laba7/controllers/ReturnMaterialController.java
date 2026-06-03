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
import ru.kochedykovdev.laba7.dao.InvoiceDao;
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.MaterialReturnDao;
import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.MaterialReturn;
import ru.kochedykovdev.laba7.util.FieldValidation;
import ru.kochedykovdev.laba7.util.Messages;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ReturnMaterialController {

    private final InvoiceDao invoiceDao;
    private final MaterialCardDao materialCardDao;
    private final MaterialReturnDao materialReturnDao;

    @FXML
    private ComboBox<Invoice> invoiceComboBox;
    @FXML
    private ComboBox<MaterialCard> materialComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private Button submitButton;

    public ReturnMaterialController(InvoiceDao invoiceDao,
                                    MaterialCardDao materialCardDao,
                                    MaterialReturnDao materialReturnDao) {
        this.invoiceDao = invoiceDao;
        this.materialCardDao = materialCardDao;
        this.materialReturnDao = materialReturnDao;
    }

    @FXML
    private void initialize() {
        try {
            setupCombo(invoiceComboBox);
            setupCombo(materialComboBox);
            invoiceComboBox.getItems().setAll(invoiceDao.findAll());
            materialComboBox.getItems().setAll(materialCardDao.findAll());
            returnDatePicker.setValue(java.time.LocalDate.now());

            invoiceComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Invoice item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : formatInvoice(item));
                }
            });
            invoiceComboBox.setCellFactory(list -> new ListCell<>() {
                @Override
                protected void updateItem(Invoice item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : formatInvoice(item));
                }
            });

            FieldValidation.onlyDecimal(quantityField);
            FieldValidation.bindSubmit(submitButton, Bindings.createBooleanBinding(
                    () -> invoiceComboBox.getValue() != null
                            && materialComboBox.getValue() != null
                            && returnDatePicker.getValue() != null
                            && isPositive(quantityField.getText()),
                    invoiceComboBox.valueProperty(),
                    materialComboBox.valueProperty(),
                    returnDatePicker.valueProperty(),
                    quantityField.textProperty()
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
            MaterialReturn materialReturn = new MaterialReturn(
                    null,
                    invoiceComboBox.getValue().getId(),
                    materialComboBox.getValue().getId(),
                    new BigDecimal(quantityField.getText().trim()),
                    returnDatePicker.getValue()
            );
            materialReturnDao.insert(materialReturn);
            closeWindow(event);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private static String formatInvoice(Invoice invoice) {
        return invoice.getInvoiceNumber() + " (" + invoice.getIssueDate() + ")";
    }

    private static boolean isPositive(String text) {
        if (text.isBlank()) {
            return false;
        }
        return new BigDecimal(text.trim()).compareTo(BigDecimal.ZERO) > 0;
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
