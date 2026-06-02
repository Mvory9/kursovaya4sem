package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.ToolDao;
import ru.kochedykovdev.laba7.model.Tool;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolDaoImpl implements ToolDao {

    private static final String TABLE = DBHelper.SCHEMA + ".tool";

    @Override
    public void insert(Tool tool) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (name, inventory_number, \"condition\") VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tool.getName());
            ps.setString(2, tool.getInventoryNumber());
            ps.setString(3, tool.getCondition());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tool.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<Tool> findById(Long id) throws SQLException {
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
    public List<Tool> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<Tool> list = new ArrayList<>();
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
    public void update(Tool tool) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, inventory_number=?, \"condition\"=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tool.getName());
            ps.setString(2, tool.getInventoryNumber());
            ps.setString(3, tool.getCondition());
            ps.setLong(4, tool.getId());
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

    private Tool mapRow(ResultSet rs) throws SQLException {
        Tool tool = new Tool();
        tool.setId(rs.getLong("id"));
        tool.setName(rs.getString("name"));
        tool.setInventoryNumber(rs.getString("inventory_number"));
        tool.setCondition(rs.getString("condition"));
        return tool;
    }
}
