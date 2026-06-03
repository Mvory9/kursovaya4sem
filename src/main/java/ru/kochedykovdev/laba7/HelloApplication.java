package ru.kochedykovdev.laba7;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kochedykovdev.laba7.controllers.MainController;
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
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.LoginDialog;
import ru.kochedykovdev.laba7.util.Messages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;

public class HelloApplication extends Application {

    private static final Logger logger = LoggerFactory.getLogger(HelloApplication.class);

    @Override
    public void start(Stage stage) {
        logger.info("Приложение запущено");
        logger.info("Локаль: {}, ресурсы: {}", Locale.getDefault(), Messages.bundle.getLocale());

        LoginDialog loginDialog = new LoginDialog();
        while (true) {
            Optional<LoginDialog.LoginResult> result = loginDialog.showAndWait();
            if (result.isEmpty()) {
                logger.info("Пользователь отменил вход, выход");
                Platform.exit();
                return;
            }

            String username = result.get().getUsername();
            String password = result.get().getPassword();
            try {
                DBHelper.initConnection(username, password);
                logger.info("Успешное подключение для {}", username);
                break;
            } catch (SQLException ex) {
                logger.error("Ошибка подключения для {}: {}", username, ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(Messages.bundle.getString("login.error.connectionTitle"));
                alert.setHeaderText(Messages.bundle.getString("login.error.connectionHeader"));
                alert.setContentText(Messages.bundle.getString("login.error.connectionMessage")
                        + "\n\n" + ex.getMessage());
                alert.showAndWait();
            }
        }

        try {
            MaterialCardDao materialCardDao = new MaterialCardDaoImpl();
            SupplierDao supplierDao = new SupplierDaoImpl();
            ToolDao toolDao = new ToolDaoImpl();
            ProrabDao prorabDao = new ProrabDaoImpl();
            BuildingObjectDao buildingObjectDao = new BuildingObjectDaoImpl();
            MaterialStockDao materialStockDao = new MaterialStockDaoImpl();
            MaterialReceiptDao materialReceiptDao = new MaterialReceiptDaoImpl();
            InvoiceDao invoiceDao = new InvoiceDaoImpl();
            InvoiceItemDao invoiceItemDao = new InvoiceItemDaoImpl();
            MaterialReturnDao materialReturnDao = new MaterialReturnDaoImpl();
            ToolIssueDao toolIssueDao = new ToolIssueDaoImpl();

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"), Messages.bundle);
            loader.setControllerFactory(clazz -> {
                if (clazz == MainController.class) {
                    return new MainController(
                            materialCardDao, supplierDao, toolDao, prorabDao, buildingObjectDao,
                            materialStockDao, materialReceiptDao, invoiceDao, invoiceItemDao,
                            materialReturnDao, toolIssueDao
                    );
                }
                throw new IllegalStateException("Неизвестный контроллер: " + clazz);
            });

            Scene scene = new Scene(loader.load(), 1000, 600);
            stage.setTitle(Messages.bundle.getString("app.title"));
            stage.setScene(scene);
            stage.show();
            logger.info("Главное окно открыто");
        } catch (IOException e) {
            logger.error("Ошибка загрузки FXML", e);
            Platform.exit();
        }
    }

    @Override
    public void stop() {
        DBHelper.closeConnection();
        logger.info("Приложение завершено");
    }
}
