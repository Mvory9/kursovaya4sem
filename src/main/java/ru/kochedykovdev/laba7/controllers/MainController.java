package ru.kochedykovdev.laba7.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochedykovdev.laba7.dao.BuildingObjectDao;
import ru.kochedykovdev.laba7.dao.InvoiceDao;
import ru.kochedykovdev.laba7.dao.InvoiceItemDao;
import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.dao.MaterialReceiptDao;
import ru.kochedykovdev.laba7.dao.MaterialReturnDao;
import ru.kochedykovdev.laba7.dao.MaterialStockDao;
import ru.kochedykovdev.laba7.dao.ProrabDao;
import ru.kochedykovdev.laba7.dao.SupplierDao;
import ru.kochedykovdev.laba7.dao.ToolDao;
import ru.kochedykovdev.laba7.dao.ToolIssueDao;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.model.InvoiceItem;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.MaterialReceipt;
import ru.kochedykovdev.laba7.model.MaterialReturn;
import ru.kochedykovdev.laba7.model.MaterialStock;
import ru.kochedykovdev.laba7.model.Prorab;
import ru.kochedykovdev.laba7.model.Supplier;
import ru.kochedykovdev.laba7.model.Tool;
import ru.kochedykovdev.laba7.model.ToolIssue;
import ru.kochedykovdev.laba7.util.Messages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final MaterialCardDao materialCardDao;
    private final SupplierDao supplierDao;
    private final ToolDao toolDao;
    private final ProrabDao prorabDao;
    private final BuildingObjectDao buildingObjectDao;
    private final MaterialStockDao materialStockDao;
    private final MaterialReceiptDao materialReceiptDao;
    private final InvoiceDao invoiceDao;
    private final InvoiceItemDao invoiceItemDao;
    private final MaterialReturnDao materialReturnDao;
    private final ToolIssueDao toolIssueDao;

    private List<Map<String, String>> referenceRows = List.of();
    private List<Map<String, String>> warehouseRows = List.of();
    private List<Map<String, String>> reportRows = List.of();

    @FXML
    private TabPane referenceSubTabs;
    @FXML
    private TableView<Map<String, String>> referenceTableView;
    @FXML
    private TextField referenceSearchField;

    @FXML
    private TabPane warehouseSubTabs;
    @FXML
    private TableView<Map<String, String>> warehouseTableView;
    @FXML
    private TextField warehouseSearchField;

    @FXML
    private TabPane reportSubTabs;
    @FXML
    private TableView<Map<String, String>> reportTableView;
    @FXML
    private TextField reportSearchField;

    public MainController(MaterialCardDao materialCardDao,
                          SupplierDao supplierDao,
                          ToolDao toolDao,
                          ProrabDao prorabDao,
                          BuildingObjectDao buildingObjectDao,
                          MaterialStockDao materialStockDao,
                          MaterialReceiptDao materialReceiptDao,
                          InvoiceDao invoiceDao,
                          InvoiceItemDao invoiceItemDao,
                          MaterialReturnDao materialReturnDao,
                          ToolIssueDao toolIssueDao) {
        this.materialCardDao = materialCardDao;
        this.supplierDao = supplierDao;
        this.toolDao = toolDao;
        this.prorabDao = prorabDao;
        this.buildingObjectDao = buildingObjectDao;
        this.materialStockDao = materialStockDao;
        this.materialReceiptDao = materialReceiptDao;
        this.invoiceDao = invoiceDao;
        this.invoiceItemDao = invoiceItemDao;
        this.materialReturnDao = materialReturnDao;
        this.toolIssueDao = toolIssueDao;
    }

    @FXML
    private void initialize() {
        referenceSubTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> reloadReferenceView());
        warehouseSubTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> reloadWarehouseView());
        reportSubTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> reloadReportView());
        refreshAll();
    }

    @FXML
    private void onRefreshReferenceClick() {
        reloadReferenceView();
    }

    @FXML
    private void onSearchReferenceClick() {
        applyFilter(referenceTableView, referenceRows, referenceSearchField);
    }

    @FXML
    private void onRefreshWarehouseClick() {
        reloadWarehouseView();
    }

    @FXML
    private void onSearchWarehouseClick() {
        applyFilter(warehouseTableView, warehouseRows, warehouseSearchField);
    }

    @FXML
    private void onRefreshReportClick() {
        reloadReportView();
    }

    @FXML
    private void onSearchReportClick() {
        applyFilter(reportTableView, reportRows, reportSearchField);
    }

    @FXML
    private void onPrihodClick(ActionEvent event) {
        openDialog("prihod-view.fxml", "dialog.prihod", event, PrihodController.class);
    }

    @FXML
    private void onGiveMaterialClick(ActionEvent event) {
        openDialog("give-material-view.fxml", "dialog.giveMaterial", event, GiveMaterialController.class);
    }

    @FXML
    private void onGiveToolClick(ActionEvent event) {
        openDialog("give-tool-view.fxml", "dialog.giveTool", event, GiveToolController.class);
    }

    @FXML
    private void onReturnToolClick(ActionEvent event) {
        openDialog("return-tool-view.fxml", "dialog.returnTool", event, ReturnToolController.class);
    }

    @FXML
    private void onManageReferencesClick(ActionEvent event) {
        openDialog("reference-editor-view.fxml", "dialog.references", event, ReferenceEditorController.class);
    }

    private void refreshAll() {
        reloadReferenceView();
        reloadWarehouseView();
        reloadReportView();
    }

    private void reloadReferenceView() {
        try {
            switch (Math.max(referenceSubTabs.getSelectionModel().getSelectedIndex(), 0)) {
                case 1 -> loadSuppliersTable();
                case 2 -> loadToolsTable();
                case 3 -> loadProrabsTable();
                case 4 -> loadObjectsTable();
                default -> loadMaterialsTable();
            }
            applyFilter(referenceTableView, referenceRows, referenceSearchField);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void reloadWarehouseView() {
        try {
            switch (Math.max(warehouseSubTabs.getSelectionModel().getSelectedIndex(), 0)) {
                case 1 -> loadReceiptsTable();
                case 2 -> loadInvoicesTable();
                case 3 -> loadInvoiceItemsTable();
                case 4 -> loadReturnsTable();
                default -> loadStockTable();
            }
            applyFilter(warehouseTableView, warehouseRows, warehouseSearchField);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void reloadReportView() {
        try {
            switch (Math.max(reportSubTabs.getSelectionModel().getSelectedIndex(), 0)) {
                case 1 -> loadReportIssuesTable();
                case 2 -> loadReportDebtorsTable();
                case 3 -> loadReportToolIssuesTable();
                default -> loadReportReceiptsTable();
            }
            applyFilter(reportTableView, reportRows, reportSearchField);
        } catch (SQLException e) {
            showError(e.getMessage());
        }
    }

    private void loadMaterialsTable() throws SQLException {
        List<String> keys = List.of("id", "name", "article", "unit", "writeOffRate");
        List<String> headers = List.of("col.id", "label.name", "label.article", "label.unit", "label.writeOffRate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (MaterialCard card : materialCardDao.findAll()) {
            rows.add(dataRow(keys,
                    str(card.getId()), str(card.getName()), str(card.getArticle()),
                    str(card.getUnit()), str(card.getWriteOffRate())));
        }
        showTable(referenceTableView, keys, headers, rows);
        referenceRows = rows;
    }

    private void loadSuppliersTable() throws SQLException {
        List<String> keys = List.of("id", "name", "contactPerson", "phone", "email", "address");
        List<String> headers = List.of("col.id", "label.name", "label.contactPerson", "label.phone", "label.email", "label.address");
        List<Map<String, String>> rows = new ArrayList<>();
        for (Supplier supplier : supplierDao.findAll()) {
            rows.add(dataRow(keys,
                    str(supplier.getId()), str(supplier.getName()), str(supplier.getContactPerson()),
                    str(supplier.getPhone()), str(supplier.getEmail()), str(supplier.getAddress())));
        }
        showTable(referenceTableView, keys, headers, rows);
        referenceRows = rows;
    }

    private void loadToolsTable() throws SQLException {
        List<String> keys = List.of("id", "name", "inventoryNumber", "condition");
        List<String> headers = List.of("col.id", "label.name", "label.inventoryNumber", "label.condition");
        List<Map<String, String>> rows = new ArrayList<>();
        for (Tool tool : toolDao.findAll()) {
            rows.add(dataRow(keys,
                    str(tool.getId()), str(tool.getName()), str(tool.getInventoryNumber()), str(tool.getCondition())));
        }
        showTable(referenceTableView, keys, headers, rows);
        referenceRows = rows;
    }

    private void loadProrabsTable() throws SQLException {
        List<String> keys = List.of("id", "name", "phone");
        List<String> headers = List.of("col.id", "label.name", "label.phone");
        List<Map<String, String>> rows = new ArrayList<>();
        for (Prorab prorab : prorabDao.findAll()) {
            rows.add(dataRow(keys, str(prorab.getId()), str(prorab.getName()), str(prorab.getPhone())));
        }
        showTable(referenceTableView, keys, headers, rows);
        referenceRows = rows;
    }

    private void loadObjectsTable() throws SQLException {
        Map<Long, String> prorabNames = nameMap(prorabDao.findAll(), Prorab::getId, Prorab::getName);
        List<String> keys = List.of("id", "name", "address", "prorab");
        List<String> headers = List.of("col.id", "label.name", "label.address", "label.prorab");
        List<Map<String, String>> rows = new ArrayList<>();
        for (BuildingObject object : buildingObjectDao.findAll()) {
            String prorab = object.getProrabId() == null ? "" : prorabNames.getOrDefault(object.getProrabId(), "?");
            rows.add(dataRow(keys, str(object.getId()), str(object.getName()), str(object.getAddress()), prorab));
        }
        showTable(referenceTableView, keys, headers, rows);
        referenceRows = rows;
    }

    private void loadStockTable() throws SQLException {
        Map<Long, String> materialNames = nameMap(materialCardDao.findAll(), MaterialCard::getId, MaterialCard::getName);
        List<String> keys = List.of("id", "material", "quantity", "lastUpdated");
        List<String> headers = List.of("col.id", "label.material", "label.quantity", "col.lastUpdated");
        List<Map<String, String>> rows = new ArrayList<>();
        for (MaterialStock stock : materialStockDao.findAll()) {
            rows.add(dataRow(keys,
                    str(stock.getId()),
                    materialNames.getOrDefault(stock.getMaterialId(), "?"),
                    str(stock.getQuantity()),
                    str(stock.getLastUpdated())));
        }
        showTable(warehouseTableView, keys, headers, rows);
        warehouseRows = rows;
    }

    private void loadReceiptsTable() throws SQLException {
        Map<Long, String> materialNames = nameMap(materialCardDao.findAll(), MaterialCard::getId, MaterialCard::getName);
        Map<Long, String> supplierNames = nameMap(supplierDao.findAll(), Supplier::getId, Supplier::getName);
        List<String> keys = List.of("id", "material", "supplier", "quantity", "price", "receiptDate");
        List<String> headers = List.of("col.id", "label.material", "label.supplier", "label.quantity", "label.price", "label.receiptDate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (MaterialReceipt receipt : materialReceiptDao.findAll()) {
            rows.add(dataRow(keys,
                    str(receipt.getId()),
                    materialNames.getOrDefault(receipt.getMaterialId(), "?"),
                    supplierNames.getOrDefault(receipt.getSupplierId(), "?"),
                    str(receipt.getQuantity()),
                    str(receipt.getPrice()),
                    str(receipt.getReceiptDate())));
        }
        showTable(warehouseTableView, keys, headers, rows);
        warehouseRows = rows;
    }

    private void loadInvoicesTable() throws SQLException {
        Map<Long, String> objectNames = nameMap(buildingObjectDao.findAll(), BuildingObject::getId, BuildingObject::getName);
        List<String> keys = List.of("id", "invoiceNumber", "object", "issueDate");
        List<String> headers = List.of("col.id", "col.invoiceNumber", "label.object", "label.issueDate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (Invoice invoice : invoiceDao.findAll()) {
            rows.add(dataRow(keys,
                    str(invoice.getId()),
                    str(invoice.getInvoiceNumber()),
                    objectNames.getOrDefault(invoice.getObjectId(), "?"),
                    str(invoice.getIssueDate())));
        }
        showTable(warehouseTableView, keys, headers, rows);
        warehouseRows = rows;
    }

    private void loadInvoiceItemsTable() throws SQLException {
        Map<Long, String> materialNames = nameMap(materialCardDao.findAll(), MaterialCard::getId, MaterialCard::getName);
        Map<Long, String> invoiceNumbers = invoiceNumberMap();
        List<String> keys = List.of("id", "invoiceNumber", "material", "quantity", "priceAtMoment");
        List<String> headers = List.of("col.id", "col.invoiceNumber", "label.material", "label.quantity", "label.priceAtIssue");
        List<Map<String, String>> rows = new ArrayList<>();
        for (InvoiceItem item : invoiceItemDao.findAll()) {
            rows.add(dataRow(keys,
                    str(item.getId()),
                    invoiceNumbers.getOrDefault(item.getInvoiceId(), "?"),
                    materialNames.getOrDefault(item.getMaterialId(), "?"),
                    str(item.getQuantity()),
                    str(item.getPriceAtMoment())));
        }
        showTable(warehouseTableView, keys, headers, rows);
        warehouseRows = rows;
    }

    private void loadReturnsTable() throws SQLException {
        Map<Long, String> materialNames = nameMap(materialCardDao.findAll(), MaterialCard::getId, MaterialCard::getName);
        Map<Long, String> invoiceNumbers = invoiceNumberMap();
        List<String> keys = List.of("id", "invoiceNumber", "material", "quantity", "returnDate");
        List<String> headers = List.of("col.id", "col.invoiceNumber", "label.material", "label.quantity", "label.materialReturnDate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (MaterialReturn materialReturn : materialReturnDao.findAll()) {
            rows.add(dataRow(keys,
                    str(materialReturn.getId()),
                    invoiceNumbers.getOrDefault(materialReturn.getInvoiceId(), "?"),
                    materialNames.getOrDefault(materialReturn.getMaterialId(), "?"),
                    str(materialReturn.getQuantity()),
                    str(materialReturn.getReturnDate())));
        }
        showTable(warehouseTableView, keys, headers, rows);
        warehouseRows = rows;
    }

    private void loadReportReceiptsTable() throws SQLException {
        loadReceiptsTable();
        reportRows = new ArrayList<>(warehouseRows);
        showTable(reportTableView,
                List.of("id", "material", "supplier", "quantity", "price", "receiptDate"),
                List.of("col.id", "label.material", "label.supplier", "label.quantity", "label.price", "label.receiptDate"),
                reportRows);
    }

    private void loadReportIssuesTable() throws SQLException {
        loadInvoiceItemsTable();
        reportRows = new ArrayList<>(warehouseRows);
        showTable(reportTableView,
                List.of("id", "invoiceNumber", "material", "quantity", "priceAtMoment"),
                List.of("col.id", "col.invoiceNumber", "label.material", "label.quantity", "label.priceAtIssue"),
                reportRows);
    }

    private void loadReportDebtorsTable() throws SQLException {
        Map<Long, String> toolNames = nameMap(toolDao.findAll(), Tool::getId, Tool::getName);
        Map<Long, String> objectNames = nameMap(buildingObjectDao.findAll(), BuildingObject::getId, BuildingObject::getName);
        List<String> keys = List.of("id", "tool", "object", "issuedTo", "issueDate", "returnDate");
        List<String> headers = List.of("col.id", "label.tool", "label.object", "label.issuedTo", "label.issueDate", "label.returnDate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (ToolIssue issue : toolIssueDao.findAll()) {
            if (issue.getActualReturnDate() != null) {
                continue;
            }
            rows.add(dataRow(keys,
                    str(issue.getId()),
                    toolNames.getOrDefault(issue.getToolId(), "?"),
                    objectNames.getOrDefault(issue.getObjectId(), "?"),
                    str(issue.getIssuedTo()),
                    str(issue.getIssueDate()),
                    str(issue.getReturnDate())));
        }
        showTable(reportTableView, keys, headers, rows);
        reportRows = rows;
    }

    private void loadReportToolIssuesTable() throws SQLException {
        Map<Long, String> toolNames = nameMap(toolDao.findAll(), Tool::getId, Tool::getName);
        Map<Long, String> objectNames = nameMap(buildingObjectDao.findAll(), BuildingObject::getId, BuildingObject::getName);
        List<String> keys = List.of("id", "tool", "object", "issuedTo", "issueDate", "returnDate", "actualReturnDate");
        List<String> headers = List.of("col.id", "label.tool", "label.object", "label.issuedTo", "label.issueDate", "label.returnDate", "label.actualReturnDate");
        List<Map<String, String>> rows = new ArrayList<>();
        for (ToolIssue issue : toolIssueDao.findAll()) {
            rows.add(dataRow(keys,
                    str(issue.getId()),
                    toolNames.getOrDefault(issue.getToolId(), "?"),
                    objectNames.getOrDefault(issue.getObjectId(), "?"),
                    str(issue.getIssuedTo()),
                    str(issue.getIssueDate()),
                    str(issue.getReturnDate()),
                    str(issue.getActualReturnDate())));
        }
        showTable(reportTableView, keys, headers, rows);
        reportRows = rows;
    }

    private Map<Long, String> invoiceNumberMap() throws SQLException {
        Map<Long, String> invoiceNumbers = new HashMap<>();
        for (Invoice invoice : invoiceDao.findAll()) {
            invoiceNumbers.put(invoice.getId(), invoice.getInvoiceNumber());
        }
        return invoiceNumbers;
    }

    private void showTable(TableView<Map<String, String>> table,
                           List<String> keys,
                           List<String> headerKeys,
                           List<Map<String, String>> rows) {
        table.getColumns().clear();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            TableColumn<Map<String, String>, String> column =
                    new TableColumn<>(Messages.bundle.getString(headerKeys.get(i)));
            column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrDefault(key, "")));
            table.getColumns().add(column);
        }
        table.setItems(FXCollections.observableArrayList(rows));
    }

    private void applyFilter(TableView<Map<String, String>> table,
                             List<Map<String, String>> allRows,
                             TextField searchField) {
        String filter = searchField.getText().trim().toLowerCase();
        if (filter.isEmpty()) {
            table.setItems(FXCollections.observableArrayList(allRows));
            return;
        }
        table.setItems(FXCollections.observableArrayList(
                allRows.stream()
                        .filter(row -> rowMatches(row, filter))
                        .collect(Collectors.toList())
        ));
    }

    private static boolean rowMatches(Map<String, String> row, String filter) {
        return row.values().stream()
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .anyMatch(value -> value.contains(filter));
    }

    private static Map<String, String> dataRow(List<String> keys, String... values) {
        Map<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            row.put(keys.get(i), i < values.length && values[i] != null ? values[i] : "");
        }
        return row;
    }

    private static <T> Map<Long, String> nameMap(List<T> items, Function<T, Long> idFn, Function<T, String> nameFn) {
        Map<Long, String> map = new HashMap<>();
        for (T item : items) {
            map.put(idFn.apply(item), nameFn.apply(item));
        }
        return map;
    }

    private static String str(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private void openDialog(String fxml, String titleKey, ActionEvent event, Class<?> controllerClass) {
        logger.info("Открытие окна: {}", titleKey);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kochedykovdev/laba7/" + fxml), Messages.bundle);
            loader.setControllerFactory(clazz -> createDialogController(clazz, controllerClass));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(Messages.bundle.getString(titleKey));
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Stage) ((Node) event.getSource()).getScene().getWindow()));
            stage.showAndWait();

            refreshAll();
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    private Object createDialogController(Class<?> clazz, Class<?> expected) {
        if (clazz != expected) {
            throw new IllegalStateException("Неизвестный контроллер: " + clazz);
        }
        if (clazz == PrihodController.class) {
            return new PrihodController(materialCardDao, supplierDao, materialReceiptDao, materialStockDao);
        }
        if (clazz == GiveMaterialController.class) {
            return new GiveMaterialController(invoiceDao, invoiceItemDao, buildingObjectDao, materialCardDao, materialStockDao);
        }
        if (clazz == GiveToolController.class) {
            return new GiveToolController(toolDao, buildingObjectDao, toolIssueDao);
        }
        if (clazz == ReturnToolController.class) {
            return new ReturnToolController(toolIssueDao, toolDao);
        }
        if (clazz == ReferenceEditorController.class) {
            return new ReferenceEditorController(
                    materialCardDao, supplierDao, prorabDao, buildingObjectDao, toolDao
            );
        }
        throw new IllegalStateException("Неизвестный контроллер: " + clazz);
    }

    private void showError(String message) {
        logger.error("{}", message);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(Messages.bundle.getString("error.header"));
        alert.setContentText(message);
        alert.showAndWait();
    }
}
