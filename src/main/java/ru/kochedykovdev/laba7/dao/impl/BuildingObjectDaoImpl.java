package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.BuildingObjectDao;
import ru.kochedykovdev.laba7.model.BuildingObject;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BuildingObjectDaoImpl implements BuildingObjectDao {

    private static final String TABLE = DBHelper.SCHEMA + ".building_object";

    @Override
    public void insert(BuildingObject object) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (name, address, prorab_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, object.getName());
            ps.setString(2, object.getAddress());
            ps.setObject(3, object.getProrabId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                object.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<BuildingObject> findById(Long id) throws SQLException {
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
    public List<BuildingObject> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<BuildingObject> list = new ArrayList<>();
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
    public void update(BuildingObject object) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, address=?, prorab_id=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, object.getName());
            ps.setString(2, object.getAddress());
            ps.setObject(3, object.getProrabId());
            ps.setLong(4, object.getId());
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

    private BuildingObject mapRow(ResultSet rs) throws SQLException {
        BuildingObject object = new BuildingObject();
        object.setId(rs.getLong("id"));
        object.setName(rs.getString("name"));
        object.setAddress(rs.getString("address"));
        object.setProrabId((Long) rs.getObject("prorab_id"));
        return object;
    }
}
