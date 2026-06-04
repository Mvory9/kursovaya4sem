package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.SupplierDao;
import ru.kochedykovdev.laba7.model.Supplier;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDaoImpl implements SupplierDao {

    private static final String TABLE = DBHelper.SCHEMA + ".supplier";

    @Override
    public void insert(Supplier supplier) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_insert_supplier",
                supplier.getName(),
                supplier.getContactPerson(),
                supplier.getPhone(),
                supplier.getEmail(),
                supplier.getAddress()
        );
        supplier.setId(id);
    }

    @Override
    public Optional<Supplier> findById(Long id) throws SQLException {
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
    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<Supplier> list = new ArrayList<>();
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
    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, contact_person=?, phone=?, email=?, address=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getContactPerson());
            ps.setString(3, supplier.getPhone());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());
            ps.setLong(6, supplier.getId());
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

    private Supplier mapRow(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("id"));
        supplier.setName(rs.getString("name"));
        supplier.setContactPerson(rs.getString("contact_person"));
        supplier.setPhone(rs.getString("phone"));
        supplier.setEmail(rs.getString("email"));
        supplier.setAddress(rs.getString("address"));
        return supplier;
    }
}
