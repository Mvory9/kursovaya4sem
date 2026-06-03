package ru.kochedykovdev.laba7.test;

import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.SQLException;

/**
 * Подключение к тестовой БД (localhost, пользователь egor)
 */
public final class TestDbSupport {

    public static final String TEST_USER = "egor";
    public static final String TEST_PASSWORD = "123456";

    private TestDbSupport() {
    }

    public static boolean tryConnect() {
        try {
            DBHelper.initConnection(TEST_USER, TEST_PASSWORD);
            DBHelper.getConnection().close();
            DBHelper.initConnection(TEST_USER, TEST_PASSWORD);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static void connectOrIgnore() throws SQLException {
        DBHelper.initConnection(TEST_USER, TEST_PASSWORD);
    }

    /** true, если в БД установлены хранимые функции */
    public static boolean proceduresInstalled() {
        if (!tryConnect()) {
            return false;
        }
        try {
            var conn = DBHelper.getConnection();
            var ps = conn.prepareStatement(
                    "SELECT 1 FROM pg_proc p JOIN pg_namespace n ON p.pronamespace = n.oid "
                            + "WHERE n.nspname = ? AND p.proname = 'fn_register_material_receipt'"
            );
            ps.setString(1, DBHelper.SCHEMA);
            var rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}
