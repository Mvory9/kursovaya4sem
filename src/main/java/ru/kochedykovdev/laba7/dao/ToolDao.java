package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.Tool;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с инструментами в базе данных.
 */
public interface ToolDao {

    /**
     * Добавить инструмент.
     *
     * @param tool инструмент (не null)
     */
    void insert(Tool tool) throws SQLException;

    /**
     * Найти инструмент по id.
     *
     * @param id номер записи
     * @return инструмент или пусто, если не найден
     */
    Optional<Tool> findById(Long id) throws SQLException;

    /** Получить все инструменты. */
    List<Tool> findAll() throws SQLException;

    /**
     * Обновить инструмент.
     *
     * @param tool инструмент с заполненным id
     */
    void update(Tool tool) throws SQLException;

    /**
     * Удалить инструмент по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
