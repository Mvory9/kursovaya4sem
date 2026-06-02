package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.ToolIssue;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с выдачами инструментов.
 */
public interface ToolIssueDao {

    /**
     * Добавить выдачу.
     *
     * @param issue выдача (не null)
     */
    void insert(ToolIssue issue) throws SQLException;

    /**
     * Найти выдачу по id.
     *
     * @param id номер записи
     * @return выдача или пусто, если не найдена
     */
    Optional<ToolIssue> findById(Long id) throws SQLException;

    /** Получить все выдачи. */
    List<ToolIssue> findAll() throws SQLException;

    /**
     * Обновить выдачу.
     *
     * @param issue выдача с заполненным id
     */
    void update(ToolIssue issue) throws SQLException;

    /**
     * Удалить выдачу по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
