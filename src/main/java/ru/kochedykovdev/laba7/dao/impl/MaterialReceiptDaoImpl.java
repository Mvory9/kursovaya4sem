package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.MaterialReceiptDao;
import ru.kochedykovdev.laba7.model.MaterialReceipt;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialReceiptDaoImpl implements MaterialReceiptDao {

    private static final String TABLE = DBHelper.SCHEMA + ".material_receipt";

    @Override
    public void insert(MaterialReceipt receipt) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_register_material_receipt",
                receipt.getMaterialId(),
                receipt.getSupplierId(),
                receipt.getQuantity(),
                receipt.getPrice(),
                receipt.getReceiptDate()
        );
        receipt.setId(id);
    }

    @Override
    public Optional<MaterialReceipt> findById(Long id) throws SQLException {
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
    public List<MaterialReceipt> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<MaterialReceipt> list = new ArrayList<>();
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
    public void update(MaterialReceipt receipt) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET material_id=?, supplier_id=?, quantity=?, price=?, receipt_date=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, receipt.getMaterialId());
            ps.setLong(2, receipt.getSupplierId());
            ps.setBigDecimal(3, receipt.getQuantity());
            ps.setBigDecimal(4, receipt.getPrice());
            ps.setObject(5, receipt.getReceiptDate());
            ps.setLong(6, receipt.getId());
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

    private MaterialReceipt mapRow(ResultSet rs) throws SQLException {
        MaterialReceipt receipt = new MaterialReceipt();
        receipt.setId(rs.getLong("id"));
        receipt.setMaterialId(rs.getLong("material_id"));
        receipt.setSupplierId(rs.getLong("supplier_id"));
        receipt.setQuantity(rs.getBigDecimal("quantity"));
        receipt.setPrice(rs.getBigDecimal("price"));
        receipt.setReceiptDate(rs.getObject("receipt_date", java.time.LocalDate.class));
        return receipt;
    }
}
