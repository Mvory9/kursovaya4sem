package ru.kochedykovdev.laba7.util;

import javafx.scene.control.Alert;
import org.slf4j.Logger;

import java.sql.SQLException;

public class UiAlerts {

    private UiAlerts() {
    }

    public static void showSqlError(Logger logger, SQLException e) {
        logger.error("Ошибка базы данных: {}", e.getMessage(), e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(Messages.bundle.getString("error.header"));
        alert.setContentText(Messages.bundle.getString("error.database"));
        alert.showAndWait();
    }
}
