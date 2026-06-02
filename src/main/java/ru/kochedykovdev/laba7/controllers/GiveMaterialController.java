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
import ru.kochedykovdev.laba7.dao.InvoiceDao;
import ru.kochedykovdev.laba7.dao.InvoiceItemDao;
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.MaterialStockDao;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.model.InvoiceItem;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.util.Messages;
import ru.kochedykovdev.laba7.model.MaterialStock;
import ru.kochedykovdev.laba7.util.FieldValidation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class GiveMaterialController {

    private final InvoiceDao invoiceDao;
    private final InvoiceItemDao invoiceItemDao;
    private final BuildingObjectDao buildingObjectDao;
    private final MaterialCardDao materialCardDao;
    private final MaterialStockDao materialStockDao;

    @FXML
    private TextField invoiceNumberField;
    @FXML
    private ComboBox<BuildingObject> objectComboBox;
    @FXML
    private DatePicker issueDatePicker;
    @FXML
    private ComboBox<MaterialCard> materialComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private Button submitButton;

    public GiveMaterialController(InvoiceDao invoiceDao,
                                  InvoiceItemDao invoiceItemDao,
                                  BuildingObjectDao buildingObjectDao,
                                  MaterialCardDao materialCardDao,
                                  MaterialStockDao materialStockDao) {
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
        this.buildingObjectDao = buildingObjectDao;
        this.materialCardDao = materialCardDao;
        this.materialStockDao = materialStockDao;
    }

    @FXML
    private void initialize() {
        try {
            setupCombo(objectComboBox);
            setupCombo(materialComboBox);
            objectComboBox.getItems().setAll(buildingObjectDao.findAll());
            materialComboBox.getItems().setAll(materialCardDao.findAll());
            issueDatePicker.setValue(java.time.LocalDate.now());

            FieldValidation.onlyDecimal(quantityField);
            FieldValidation.onlyDecimal(priceField);

            FieldValidation.bindSubmit(submitButton, Bindings.createBooleanBinding(
                    () -> !invoiceNumberField.getText().isBlank()
                            && objectComboBox.getValue() != null
                            && materialComboBox.getValue() != null
                            && issueDatePicker.getValue() != null
                            && isPositive(quantityField.getText())
                            && isPositive(priceField.getText()),
                    invoiceNumberField.textProperty(),
                    objectComboBox.valueProperty(),
                    materialComboBox.valueProperty(),
                    issueDatePicker.valueProperty(),
                    quantityField.textProperty(),
                    priceField.textProperty()
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

            MaterialStock stock = findStock(material.getId())
                    .orElseThrow(() -> new IllegalStateException("Материала нет на складе"));

            if (stock.getQuantity().compareTo(qty) < 0) {
                throw new IllegalStateException("Недостаточно материала на складе");
            }

            Invoice invoice = new Invoice(
                    null,
                    invoiceNumberField.getText().trim(),
                    objectComboBox.getValue().getId(),
                    issueDatePicker.getValue()
            );
            invoiceDao.insert(invoice);

            InvoiceItem item = new InvoiceItem(
                    null,
                    invoice.getId(),
                    material.getId(),
                    qty,
                    new BigDecimal(priceField.getText().trim())
            );
            invoiceItemDao.insert(item);

            stock.setQuantity(stock.getQuantity().subtract(qty));
            stock.setLastUpdated(LocalDateTime.now());
            materialStockDao.update(stock);

            closeWindow(event);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private Optional<MaterialStock> findStock(Long materialId) throws SQLException {
        return materialStockDao.findAll().stream()
                .filter(s -> materialId.equals(s.getMaterialId()))
                .findFirst();
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
