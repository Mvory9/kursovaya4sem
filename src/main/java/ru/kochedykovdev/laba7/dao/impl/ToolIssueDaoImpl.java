package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.ToolIssueDao;
import ru.kochedykovdev.laba7.model.ToolIssue;
import ru.kochedykovdev.laba7.util.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolIssueDaoImpl implements ToolIssueDao {

    private static final String TABLE = DBHelper.SCHEMA + ".tool_issue";

    @Override
    public void insert(ToolIssue issue) throws SQLException {
        String sql = "INSERT INTO " + TABLE + " (tool_id, object_id, issued_to, issue_date, return_date, actual_return_date) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, issue.getToolId());
            ps.setLong(2, issue.getObjectId());
            ps.setString(3, issue.getIssuedTo());
            ps.setObject(4, issue.getIssueDate());
            ps.setObject(5, issue.getReturnDate());
            ps.setObject(6, issue.getActualReturnDate());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                issue.setId(rs.getLong("id"));
            }
        }
    }

    @Override
    public Optional<ToolIssue> findById(Long id) throws SQLException {
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
    public List<ToolIssue> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<ToolIssue> list = new ArrayList<>();
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
    public void update(ToolIssue issue) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET tool_id=?, object_id=?, issued_to=?, issue_date=?, return_date=?, actual_return_date=? WHERE id=?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, issue.getToolId());
            ps.setLong(2, issue.getObjectId());
            ps.setString(3, issue.getIssuedTo());
            ps.setObject(4, issue.getIssueDate());
            ps.setObject(5, issue.getReturnDate());
            ps.setObject(6, issue.getActualReturnDate());
            ps.setLong(7, issue.getId());
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

    private ToolIssue mapRow(ResultSet rs) throws SQLException {
        ToolIssue issue = new ToolIssue();
        issue.setId(rs.getLong("id"));
        issue.setToolId(rs.getLong("tool_id"));
        issue.setObjectId(rs.getLong("object_id"));
        issue.setIssuedTo(rs.getString("issued_to"));
        issue.setIssueDate(rs.getObject("issue_date", java.time.LocalDate.class));
        issue.setReturnDate(rs.getObject("return_date", java.time.LocalDate.class));
        issue.setActualReturnDate(rs.getObject("actual_return_date", java.time.LocalDate.class));
        return issue;
    }
}
