package ru.kochedykovdev.laba7.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345678";

    public static final String SCHEMA = "kursovaya4sem";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.info("Подключение к БД: {}", URL);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setSchema(SCHEMA);
            logger.info("Соединение установлено, схема: {}", SCHEMA);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                logger.info("Соединение закрыто");
            } catch (SQLException e) {
                logger.error("Ошибка закрытия соединения", e);
            }
        }
    }
}
