package ru.kochedykovdev.laba7.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

/**
 * Вызов хранимых функций PostgreSQL через JDBC.
 */
public final class SqlProcedureHelper {

    private static final Logger logger = LoggerFactory.getLogger(SqlProcedureHelper.class);

    private SqlProcedureHelper() {
    }

    public static long callFunctionReturningLong(String functionName, Object... params) throws SQLException {
        try (Connection conn = DBHelper.getConnection();
             CallableStatement cs = prepareCall(conn, functionName, true, params)) {
            cs.execute();
            long result = cs.getLong(1);
            if (cs.wasNull()) {
                throw new SQLException("Функция " + functionName + " вернула NULL");
            }
            logger.info("Вызов {} -> id={}", functionName, result);
            return result;
        }
    }

    public static void callFunctionVoid(String functionName, Object... params) throws SQLException {
        try (Connection conn = DBHelper.getConnection();
             CallableStatement cs = prepareCall(conn, functionName, false, params)) {
            cs.execute();
            logger.info("Вызов {} выполнен", functionName);
        }
    }

    private static CallableStatement prepareCall(Connection conn, String functionName, boolean hasReturn,
                                                 Object... params) throws SQLException {
        StringBuilder ph = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) {
                ph.append(", ");
            }
            ph.append('?');
        }
        String sql;
        if (hasReturn) {
            sql = "{ ? = call " + DBHelper.SCHEMA + "." + functionName + "(" + ph + ") }";
        } else {
            sql = "{ call " + DBHelper.SCHEMA + "." + functionName + "(" + ph + ") }";
        }
        CallableStatement cs = conn.prepareCall(sql);
        int paramIndex = 1;
        if (hasReturn) {
            cs.registerOutParameter(paramIndex++, Types.BIGINT);
        }
        for (Object param : params) {
            setParameter(cs, paramIndex++, param);
        }
        return cs;
    }

    private static void setParameter(CallableStatement cs, int index, Object param) throws SQLException {
        if (param == null) {
            cs.setNull(index, Types.VARCHAR);
            return;
        }
        switch (param) {
            case Long l -> cs.setLong(index, l);
            case Integer i -> cs.setInt(index, i);
            case String s -> cs.setString(index, s);
            case BigDecimal bd -> cs.setBigDecimal(index, bd);
            case LocalDate ld -> cs.setObject(index, ld);
            default -> cs.setObject(index, param);
        }
    }
}
