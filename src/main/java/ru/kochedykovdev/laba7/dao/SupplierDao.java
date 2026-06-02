package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.Supplier;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с поставщиками в базе данных.
 */
public interface SupplierDao {

    /**
     * Добавить поставщика.
     *
     * @param supplier поставщик (не null)
     */
    void insert(Supplier supplier) throws SQLException;

    /**
     * Найти поставщика по id.
     *
     * @param id номер записи
     * @return поставщик или пусто, если не найден
     */
    Optional<Supplier> findById(Long id) throws SQLException;

    /** Получить всех поставщиков. */
    List<Supplier> findAll() throws SQLException;

    /**
     * Обновить поставщика.
     *
     * @param supplier поставщик с заполненным id
     */
    void update(Supplier supplier) throws SQLException;

    /**
     * Удалить поставщика по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
