package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.MaterialStock;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с остатками материалов на складе.
 */
public interface MaterialStockDao {

    /**
     * Добавить запись об остатке.
     *
     * @param stock остаток (не null)
     */
    void insert(MaterialStock stock) throws SQLException;

    /**
     * Найти остаток по id.
     *
     * @param id номер записи
     * @return остаток или пусто, если не найден
     */
    Optional<MaterialStock> findById(Long id) throws SQLException;

    /** Получить все остатки. */
    List<MaterialStock> findAll() throws SQLException;

    /**
     * Обновить остаток.
     *
     * @param stock остаток с заполненным id
     */
    void update(MaterialStock stock) throws SQLException;

    /**
     * Удалить остаток по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
