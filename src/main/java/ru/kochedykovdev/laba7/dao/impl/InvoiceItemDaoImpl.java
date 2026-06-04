package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.InvoiceItemDao;
import ru.kochedykovdev.laba7.model.InvoiceItem;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceItemDaoImpl implements InvoiceItemDao {

    private static final String TABLE = DBHelper.SCHEMA + ".invoice_item";

    @Override
    public void insert(InvoiceItem item) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (invoice_id, material_id, quantity, price_at_moment) VALUES (?, ?, ?, ?) RETURNING id";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getInvoiceId());
            ps.setLong(2, item.getMaterialId());
            ps.setBigDecimal(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPriceAtMoment());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                item.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<InvoiceItem> findById(Long id) throws SQLException {
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
    public List<InvoiceItem> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<InvoiceItem> list = new ArrayList<>();
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
    public void update(InvoiceItem item) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET invoice_id=?, material_id=?, quantity=?, price_at_moment=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, item.getInvoiceId());
            ps.setLong(2, item.getMaterialId());
            ps.setBigDecimal(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPriceAtMoment());
            ps.setLong(5, item.getId());
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

    private InvoiceItem mapRow(ResultSet rs) throws SQLException {
        InvoiceItem item = new InvoiceItem();
        item.setId(rs.getLong("id"));
        item.setInvoiceId(rs.getLong("invoice_id"));
        item.setMaterialId(rs.getLong("material_id"));
        item.setQuantity(rs.getBigDecimal("quantity"));
        item.setPriceAtMoment(rs.getBigDecimal("price_at_moment"));
        return item;
    }
}
