package ru.kochedykovdev.laba7.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {

    private static final String CONFIG_PATH = "/ru/kochedykovdev/laba7/config.properties";

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);

    private static final String dbUrlBase;
    private static final String dbName;
    public static final String SCHEMA;

    private static Connection connection;
    private static String storedUser;
    private static String storedPassword;

    static {
        Properties prop = new Properties();
        try (InputStream is = DBHelper.class.getResourceAsStream(CONFIG_PATH)) {
            if (is == null) {
                throw new IOException("config.properties не найден");
            }
            prop.load(is);
            dbUrlBase = prop.getProperty("db.url");
            dbName = prop.getProperty("db.name");
            SCHEMA = prop.getProperty("db.schema");
            logger.debug("Загружены настройки подключения (url, name, schema)");
        } catch (IOException ex) {
            logger.error("Ошибка загрузки config.properties", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void initConnection(String user, String password) throws SQLException {
        storedUser = user;
        storedPassword = password;
        connect(true);
    }

    public static Connection getConnection() throws SQLException {
        if (storedUser == null) {
            throw new SQLException("Соединение не инициализировано. Вызовите initConnection()");
        }
        if (connection == null || connection.isClosed()) {
            connect(false);
        }
        return connection;
    }

    private static void connect(boolean logConnect) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            closeConnection();
        }
        String fullUrl = dbUrlBase + dbName;
        if (logConnect) {
            logger.info("Подключение к {} пользователем {}", fullUrl, storedUser);
        } else {
            logger.debug("Повторное подключение к {} пользователем {}", fullUrl, storedUser);
        }
        connection = DriverManager.getConnection(fullUrl, storedUser, storedPassword);
        connection.setSchema(SCHEMA);
        logger.info("Соединение установлено, схема: {}", SCHEMA);
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Соединение закрыто");
            } catch (SQLException ex) {
                logger.error("Ошибка закрытия соединения", ex);
            } finally {
                connection = null;
            }
        }
    }
}
