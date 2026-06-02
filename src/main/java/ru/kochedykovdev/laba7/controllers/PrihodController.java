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
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.MaterialReceiptDao;
import ru.kochedykovdev.laba7.dao.MaterialStockDao;
import ru.kochedykovdev.laba7.dao.SupplierDao;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.MaterialReceipt;
import ru.kochedykovdev.laba7.model.MaterialStock;
import ru.kochedykovdev.laba7.model.Supplier;
import ru.kochedykovdev.laba7.util.FieldValidation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class PrihodController {

    private final MaterialCardDao materialCardDao;
    private final SupplierDao supplierDao;
    private final MaterialReceiptDao materialReceiptDao;
    private final MaterialStockDao materialStockDao;

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
                            MaterialReceiptDao materialReceiptDao,
                            MaterialStockDao materialStockDao) {
        this.materialCardDao = materialCardDao;
        this.supplierDao = supplierDao;
        this.materialReceiptDao = materialReceiptDao;
        this.materialStockDao = materialStockDao;
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

            updateStock(material.getId(), qty);
            closeWindow(event);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void updateStock(Long materialId, BigDecimal qty) throws SQLException {
        Optional<MaterialStock> existing = materialStockDao.findAll().stream()
                .filter(s -> materialId.equals(s.getMaterialId()))
                .findFirst();

        if (existing.isPresent()) {
            MaterialStock stock = existing.get();
            stock.setQuantity(stock.getQuantity().add(qty));
            stock.setLastUpdated(LocalDateTime.now());
            materialStockDao.update(stock);
        } else {
            MaterialStock stock = new MaterialStock(null, materialId, qty, LocalDateTime.now());
            materialStockDao.insert(stock);
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
        alert.setHeaderText("Ошибка");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
