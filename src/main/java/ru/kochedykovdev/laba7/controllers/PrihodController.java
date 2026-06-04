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
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.MaterialReceiptDao;
import ru.kochedykovdev.laba7.dao.SupplierDao;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.MaterialReceipt;
import ru.kochedykovdev.laba7.model.Supplier;
import ru.kochedykovdev.laba7.util.FieldValidation;
import ru.kochedykovdev.laba7.util.Messages;
import ru.kochedykovdev.laba7.util.UiAlerts;

import java.math.BigDecimal;
import java.sql.SQLException;

public class PrihodController {

    private static final Logger logger = LoggerFactory.getLogger(PrihodController.class);

    private final MaterialCardDao materialCardDao;
    private final SupplierDao supplierDao;
    private final MaterialReceiptDao materialReceiptDao;

    @FXML
    private ComboBox<MaterialCard> materialComboBox;
    @FXML
    private ComboBox<Supplier> supplierComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private DatePicker receiptDatePicker;
    @FXML
    private Button submitButton;

    public PrihodController(MaterialCardDao materialCardDao,
                            SupplierDao supplierDao,
                            MaterialReceiptDao materialReceiptDao) {
        this.materialCardDao = materialCardDao;
        this.supplierDao = supplierDao;
        this.materialReceiptDao = materialReceiptDao;
    }

    @FXML
    private void initialize() {
        try {
            setupCombo(materialComboBox);
            setupCombo(supplierComboBox);
            materialComboBox.getItems().setAll(materialCardDao.findAll());
            supplierComboBox.getItems().setAll(supplierDao.findAll());
            receiptDatePicker.setValue(java.time.LocalDate.now());

            FieldValidation.onlyDecimal(quantityField);
            FieldValidation.onlyDecimal(priceField);

            FieldValidation.bindSubmit(submitButton, Bindings.createBooleanBinding(
                    () -> materialComboBox.getValue() != null
                            && supplierComboBox.getValue() != null
                            && !quantityField.getText().isBlank()
                            && !priceField.getText().isBlank()
                            && receiptDatePicker.getValue() != null
                            && isPositive(quantityField.getText())
                            && isPositive(priceField.getText()),
                    materialComboBox.valueProperty(),
                    supplierComboBox.valueProperty(),
                    quantityField.textProperty(),
                    priceField.textProperty(),
                    receiptDatePicker.valueProperty()
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
            MaterialCard material = materialComboBox.getValue();
            BigDecimal qty = new BigDecimal(quantityField.getText().trim());

            MaterialReceipt receipt = new MaterialReceipt(
                    null,
                    material.getId(),
                    supplierComboBox.getValue().getId(),
                    qty,
                    new BigDecimal(priceField.getText().trim()),
                    receiptDatePicker.getValue()
            );
            materialReceiptDao.insert(receipt);
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
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
