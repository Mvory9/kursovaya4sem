package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.ProrabDao;
import ru.kochedykovdev.laba7.model.Prorab;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProrabDaoImpl implements ProrabDao {

    private static final String TABLE = DBHelper.SCHEMA + ".prorab";

    @Override
    public void insert(Prorab prorab) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_insert_prorab",
                prorab.getName(),
                prorab.getPhone()
        );
        prorab.setId(id);
    }

    @Override
    public Optional<Prorab> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Prorab> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<Prorab> list = new ArrayList<>();
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Prorab prorab) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, phone=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prorab.getName());
            ps.setString(2, prorab.getPhone());
            ps.setLong(3, prorab.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Prorab mapRow(ResultSet rs) throws SQLException {
        Prorab prorab = new Prorab();
        prorab.setId(rs.getLong("id"));
        prorab.setName(rs.getString("name"));
        prorab.setPhone(rs.getString("phone"));
        return prorab;
    }
}
