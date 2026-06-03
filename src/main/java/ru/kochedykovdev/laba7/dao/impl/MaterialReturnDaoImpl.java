package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.MaterialReturnDao;
import ru.kochedykovdev.laba7.model.MaterialReturn;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaterialReturnDaoImpl implements MaterialReturnDao {

    private static final String TABLE = DBHelper.SCHEMA + ".material_return";

    @Override
    public void insert(MaterialReturn materialReturn) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_return_material",
                materialReturn.getInvoiceId(),
                materialReturn.getMaterialId(),
                materialReturn.getQuantity(),
                materialReturn.getReturnDate()
        );
        materialReturn.setId(id);
    }

    @Override
    public Optional<MaterialReturn> findById(Long id) throws SQLException {
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
    public List<MaterialReturn> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<MaterialReturn> list = new ArrayList<>();
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
    public void update(MaterialReturn materialReturn) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET invoice_id=?, material_id=?, quantity=?, return_date=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, materialReturn.getInvoiceId());
            ps.setLong(2, materialReturn.getMaterialId());
            ps.setBigDecimal(3, materialReturn.getQuantity());
            ps.setObject(4, materialReturn.getReturnDate());
            ps.setLong(5, materialReturn.getId());
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

    private MaterialReturn mapRow(ResultSet rs) throws SQLException {
        MaterialReturn materialReturn = new MaterialReturn();
        materialReturn.setId(rs.getLong("id"));
        materialReturn.setInvoiceId(rs.getLong("invoice_id"));
        materialReturn.setMaterialId(rs.getLong("material_id"));
        materialReturn.setQuantity(rs.getBigDecimal("quantity"));
        materialReturn.setReturnDate(rs.getObject("return_date", java.time.LocalDate.class));
        return materialReturn;
    }
}
