package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.InvoiceDao;
import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceDaoImpl implements InvoiceDao {

    private static final String TABLE = DBHelper.SCHEMA + ".invoice";

    @Override
    public void insert(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (invoice_number, object_id, issue_date) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoice.getInvoiceNumber());
            ps.setLong(2, invoice.getObjectId());
            ps.setObject(3, invoice.getIssueDate());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                invoice.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<Invoice> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Invoice> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Invoice invoice) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET invoice_number=?, object_id=?, issue_date=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoice.getInvoiceNumber());
            ps.setLong(2, invoice.getObjectId());
            ps.setObject(3, invoice.getIssueDate());
            ps.setLong(4, invoice.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Invoice mapRow(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setObjectId(rs.getLong("object_id"));
        invoice.setIssueDate(rs.getObject("issue_date", java.time.LocalDate.class));
        return invoice;
    }
}
