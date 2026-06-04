package ru.kochedykovdev.laba7.dao.impl;

import ru.kochedykovdev.laba7.dao.ToolIssueDao;
import ru.kochedykovdev.laba7.model.ToolIssue;
import ru.kochedykovdev.laba7.util.DBHelper;
import ru.kochedykovdev.laba7.util.SqlProcedureHelper;

import java.time.LocalDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolIssueDaoImpl implements ToolIssueDao {

    private static final String TABLE = DBHelper.SCHEMA + ".tool_issue";

    @Override
    public void insert(ToolIssue issue) throws SQLException {
        long id = SqlProcedureHelper.callFunctionReturningLong(
                "fn_issue_tool",
                issue.getToolId(),
                issue.getObjectId(),
                issue.getIssuedTo(),
                issue.getIssueDate(),
                issue.getReturnDate()
        );
        issue.setId(id);
    }

    @Override
    public void returnTool(long issueId, LocalDate actualReturnDate, String condition) throws SQLException {
        SqlProcedureHelper.callFunctionVoid("fn_return_tool", issueId, actualReturnDate, condition);
    }

    @Override
    public Optional<ToolIssue> findById(Long id) throws SQLException {
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
    public List<ToolIssue> findAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE;
        List<ToolIssue> list = new ArrayList<>();
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
    public void update(ToolIssue issue) throws SQLException {
        String sql = "UPDATE " + TABLE + " SET tool_id=?, object_id=?, issued_to=?, issue_date=?, return_date=?, actual_return_date=? WHERE id=?";
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        DBHelper.logQuery(sql);
        Connection conn = DBHelper.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
