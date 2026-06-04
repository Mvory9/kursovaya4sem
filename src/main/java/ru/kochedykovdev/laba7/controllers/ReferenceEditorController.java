package ru.kochedykovdev.laba7.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochedykovdev.laba7.dao.BuildingObjectDao;
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.ProrabDao;
import ru.kochedykovdev.laba7.dao.SupplierDao;
import ru.kochedykovdev.laba7.dao.ToolDao;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.Prorab;
import ru.kochedykovdev.laba7.model.Supplier;
import ru.kochedykovdev.laba7.model.Tool;
import ru.kochedykovdev.laba7.util.FieldValidation;
import ru.kochedykovdev.laba7.util.Messages;
import ru.kochedykovdev.laba7.util.UiAlerts;

import java.sql.SQLException;

public class ReferenceEditorController {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceEditorController.class);

    private final MaterialCardDao materialCardDao;
    private final SupplierDao supplierDao;
    private final ProrabDao prorabDao;
    private final BuildingObjectDao buildingObjectDao;
    private final ToolDao toolDao;

    @FXML
    private TabPane referenceTabPane;
    @FXML
    private Tab objectTab;

    @FXML
    private TextField materialNameField;
    @FXML
    private TextField materialArticleField;
    @FXML
    private TextField materialUnitField;
    @FXML
    private TextField materialWriteOffRateField;
    @FXML
    private Button materialSubmitButton;

    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField supplierContactField;
    @FXML
    private TextField supplierPhoneField;
    @FXML
    private TextField supplierEmailField;
    @FXML
    private TextField supplierAddressField;
    @FXML
    private Button supplierSubmitButton;

    @FXML
    private TextField prorabNameField;
    @FXML
    private TextField prorabPhoneField;
    @FXML
    private Button prorabSubmitButton;

    @FXML
    private TextField objectNameField;
    @FXML
    private TextField objectAddressField;
    @FXML
    private ComboBox<Prorab> objectProrabComboBox;
    @FXML
    private Button objectSubmitButton;

    @FXML
    private TextField toolNameField;
    @FXML
    private TextField toolInventoryField;
    @FXML
    private TextField toolConditionField;
    @FXML
    private Button toolSubmitButton;

    public ReferenceEditorController(MaterialCardDao materialCardDao,
                                     SupplierDao supplierDao,
                                     ProrabDao prorabDao,
                                     BuildingObjectDao buildingObjectDao,
                                     ToolDao toolDao) {
        this.materialCardDao = materialCardDao;
        this.supplierDao = supplierDao;
        this.prorabDao = prorabDao;
        this.buildingObjectDao = buildingObjectDao;
        this.toolDao = toolDao;
    }

    @FXML
    private void initialize() {
        FieldValidation.onlyInteger(materialArticleField);
        FieldValidation.onlyInteger(materialWriteOffRateField);
        setupCombo(objectProrabComboBox);

        FieldValidation.bindSubmit(materialSubmitButton, Bindings.createBooleanBinding(
                () -> !materialNameField.getText().isBlank(),
                materialNameField.textProperty()
        ));
        FieldValidation.bindSubmit(supplierSubmitButton, Bindings.createBooleanBinding(
                () -> !supplierNameField.getText().isBlank(),
                supplierNameField.textProperty()
        ));
        FieldValidation.bindSubmit(prorabSubmitButton, Bindings.createBooleanBinding(
                () -> !prorabNameField.getText().isBlank(),
                prorabNameField.textProperty()
        ));
        FieldValidation.bindSubmit(objectSubmitButton, Bindings.createBooleanBinding(
                () -> !objectNameField.getText().isBlank(),
                objectNameField.textProperty()
        ));
        FieldValidation.bindSubmit(toolSubmitButton, Bindings.createBooleanBinding(
                () -> !toolNameField.getText().isBlank(),
                toolNameField.textProperty()
        ));

        referenceTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == objectTab) {
                reloadProrabs();
            }
        });
        reloadProrabs();
    }

    @FXML
    private void onCloseClick(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    private void onMaterialSubmitClick(ActionEvent event) {
        try {
            MaterialCard card = new MaterialCard(
                    null,
                    materialNameField.getText().trim(),
                    parseOptionalInteger(materialArticleField),
                    blankToNull(materialUnitField),
                    parseOptionalInteger(materialWriteOffRateField)
            );
            materialCardDao.insert(card);
            clearMaterialForm();
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onSupplierSubmitClick(ActionEvent event) {
        try {
            Supplier supplier = new Supplier(
                    null,
                    supplierNameField.getText().trim(),
                    blankToNull(supplierContactField),
                    blankToNull(supplierPhoneField),
                    blankToNull(supplierEmailField),
                    blankToNull(supplierAddressField)
            );
            supplierDao.insert(supplier);
            clearSupplierForm();
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onProrabSubmitClick(ActionEvent event) {
        try {
            Prorab prorab = new Prorab(
                    null,
                    prorabNameField.getText().trim(),
                    blankToNull(prorabPhoneField)
            );
            prorabDao.insert(prorab);
            clearProrabForm();
            reloadProrabs();
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onObjectSubmitClick(ActionEvent event) {
        try {
            Prorab prorab = objectProrabComboBox.getValue();
            BuildingObject object = new BuildingObject(
                    null,
                    objectNameField.getText().trim(),
                    blankToNull(objectAddressField),
                    prorab != null ? prorab.getId() : null
            );
            buildingObjectDao.insert(object);
            clearObjectForm();
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onToolSubmitClick(ActionEvent event) {
        try {
            Tool tool = new Tool(
                    null,
                    toolNameField.getText().trim(),
                    blankToNull(toolInventoryField),
                    blankToNull(toolConditionField)
            );
            toolDao.insert(tool);
            clearToolForm();
            closeWindow(event);
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void reloadProrabs() {
        try {
            objectProrabComboBox.getItems().setAll(prorabDao.findAll());
        } catch (SQLException e) {
            UiAlerts.showSqlError(logger, e);
        }
    }

    private void clearMaterialForm() {
        materialNameField.clear();
        materialArticleField.clear();
        materialUnitField.clear();
        materialWriteOffRateField.clear();
    }

    private void clearSupplierForm() {
        supplierNameField.clear();
        supplierContactField.clear();
        supplierPhoneField.clear();
        supplierEmailField.clear();
        supplierAddressField.clear();
    }

    private void clearProrabForm() {
        prorabNameField.clear();
        prorabPhoneField.clear();
    }

    private void clearObjectForm() {
        objectNameField.clear();
        objectAddressField.clear();
        objectProrabComboBox.getSelectionModel().clearSelection();
    }

    private void clearToolForm() {
        toolNameField.clear();
        toolInventoryField.clear();
        toolConditionField.clear();
    }

    private static Integer parseOptionalInteger(TextField field) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        return Integer.valueOf(text);
    }

    private static String blankToNull(TextField field) {
        String text = field.getText().trim();
        return text.isEmpty() ? null : text;
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
