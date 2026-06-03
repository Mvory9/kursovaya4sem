package ru.kochedykovdev.laba7.test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.WaitForAsyncUtils;
import ru.kochedykovdev.laba7.controllers.MainController;
import ru.kochedykovdev.laba7.dao.impl.BuildingObjectDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.InvoiceDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.InvoiceItemDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.MaterialCardDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.MaterialReceiptDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.MaterialReturnDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.MaterialStockDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.ProrabDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.SupplierDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.ToolDaoImpl;
import ru.kochedykovdev.laba7.dao.impl.ToolIssueDaoImpl;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.model.InvoiceItem;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.model.MaterialStock;
import ru.kochedykovdev.laba7.util.Messages;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@EnabledIf("ru.kochedykovdev.laba7.test.TestDbSupport#tryConnect")
class MainViewFxTest extends ApplicationTest {

    @BeforeEach
    void connectDb() throws Exception {
        TestDbSupport.connectOrIgnore();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ru/kochedykovdev/laba7/main-view.fxml"),
                Messages.bundle
        );
        loader.setControllerFactory(clazz -> {
            if (clazz == MainController.class) {
                return new MainController(
                        new MaterialCardDaoImpl(),
                        new SupplierDaoImpl(),
                        new ToolDaoImpl(),
                        new ProrabDaoImpl(),
                        new BuildingObjectDaoImpl(),
                        new MaterialStockDaoImpl(),
                        new MaterialReceiptDaoImpl(),
                        new InvoiceDaoImpl(),
                        new InvoiceItemDaoImpl(),
                        new MaterialReturnDaoImpl(),
                        new ToolIssueDaoImpl()
                );
            }
            throw new IllegalStateException(clazz.getName());
        });
        Parent root = loader.load();
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();
    }

    // --- Позитивные сценарии ---

    /** Позитивный 1: главное окно загружается, видна вкладка «Склад». */
    @Test
    void positive_mainWindowShowsWarehouseTab() {
        NodeQuery tab = lookup(Messages.bundle.getString("tab.warehouse"));
        verifyThat(tab, isVisible());
    }

    /** Позитивный 2: по кнопке «Приход» открывается форма с полем выбора материала. */
    @Test
    void positive_receiptDialogOpens() {
        clickOn(Messages.bundle.getString("button.receipt"));
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat(lookup(Messages.bundle.getString("label.material")), isVisible());
    }

    // --- Негативные сценарии ---

    /** Негативный 1: в форме выдачи материала кнопка «Провести» неактивна при пустых полях. */
    @Test
    void negative_giveMaterialSubmitDisabledWhenFormEmpty() {
        clickOn(Messages.bundle.getString("button.giveMaterial"));
        WaitForAsyncUtils.waitForFxEvents();
        verifyThat("#submitButton", isDisabled());
    }

    /** Негативный 2: выдача материала сверх остатка отклоняется функцией fn_issue_material. */
    @Test
    void negative_issueMaterialRejectsInsufficientStock() throws Exception {
        assumeTrue(TestDbSupport.proceduresInstalled(), "Нужен sql/stored_procedures.sql");

        MaterialCardDaoImpl materialCardDao = new MaterialCardDaoImpl();
        MaterialStockDaoImpl materialStockDao = new MaterialStockDaoImpl();
        BuildingObjectDaoImpl buildingObjectDao = new BuildingObjectDaoImpl();
        InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();

        MaterialCard material = materialCardDao.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет материалов в БД"));
        MaterialStock stock = materialStockDao.findAll().stream()
                .filter(s -> material.getId().equals(s.getMaterialId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет остатка для материала"));
        BuildingObject object = buildingObjectDao.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Нет объектов в БД"));

        BigDecimal excessive = stock.getQuantity().add(BigDecimal.valueOf(1_000_000));
        Invoice invoice = new Invoice(
                null,
                "NEG-" + System.nanoTime(),
                object.getId(),
                LocalDate.now()
        );
        InvoiceItem item = new InvoiceItem(
                null,
                null,
                material.getId(),
                excessive,
                BigDecimal.ONE
        );

        assertThrows(SQLException.class, () -> invoiceDao.issueMaterial(invoice, item));
    }
}
