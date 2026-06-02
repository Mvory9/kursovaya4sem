package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.MaterialStockDao;
import ru.kochedykovdev.laba7.model.MaterialStock;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialStockDaoImpl implements MaterialStockDao {

    private static final String TABLE = DBHelper.SCHEMA + ".material_stock";

    @Override
    public void insert(MaterialStock stock) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (material_id, quantity, last_updated) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, stock.getMaterialId());
            ps.setBigDecimal(2, stock.getQuantity());
            ps.setObject(3, stock.getLastUpdated());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stock.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<MaterialStock> findById(Long id) throws SQLException {
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
    public List<MaterialStock> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<MaterialStock> list = new ArrayList<>();
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
    public void update(MaterialStock stock) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET material_id=?, quantity=?, last_updated=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, stock.getMaterialId());
            ps.setBigDecimal(2, stock.getQuantity());
            ps.setObject(3, stock.getLastUpdated());
            ps.setLong(4, stock.getId());
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

    private MaterialStock mapRow(ResultSet rs) throws SQLException {
        MaterialStock stock = new MaterialStock();
        stock.setId(rs.getLong("id"));
        stock.setMaterialId(rs.getLong("material_id"));
        stock.setQuantity(rs.getBigDecimal("quantity"));
        stock.setLastUpdated(rs.getObject("last_updated", java.time.LocalDateTime.class));
        return stock;
    }
}
