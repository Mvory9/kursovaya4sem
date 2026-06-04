package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.MaterialCardDao;
import ru.kochedykovdev.laba7.model.MaterialCard;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialCardDaoImpl implements MaterialCardDao {

    private static final String TABLE = DBHelper.SCHEMA + ".material_card";

    @Override
    public void insert(MaterialCard card) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_insert_material_card",
                card.getName(),
                card.getArticle(),
                card.getUnit(),
                card.getWriteOffRate()
        );
        card.setId(id);
    }

    @Override
    public Optional<MaterialCard> findById(Long id) throws SQLException {
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
    public List<MaterialCard> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<MaterialCard> list = new ArrayList<>();
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
    public void update(MaterialCard card) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET name=?, article=?, unit=?, write_off_rate=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getName());
            ps.setObject(2, card.getArticle());
            ps.setString(3, card.getUnit());
            ps.setObject(4, card.getWriteOffRate());
            ps.setLong(5, card.getId());
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

    private MaterialCard mapRow(ResultSet rs) throws SQLException {
        MaterialCard card = new MaterialCard();
        card.setId(rs.getLong("id"));
        card.setName(rs.getString("name"));
        card.setArticle((Integer) rs.getObject("article"));
        card.setUnit(rs.getString("unit"));
        card.setWriteOffRate((Integer) rs.getObject("write_off_rate"));
        return card;
    }
}
