package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.MaterialReceipt;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с документами прихода материалов.
 */
public interface MaterialReceiptDao {

    /**
     * Добавить приход.
     *
     * @param receipt приход (не null)
     */
    void insert(MaterialReceipt receipt) throws SQLException;

    /**
     * Найти приход по id.
     *
     * @param id номер записи
     * @return приход или пусто, если не найден
     */
    Optional<MaterialReceipt> findById(Long id) throws SQLException;

    /** Получить все приходы. */
    List<MaterialReceipt> findAll() throws SQLException;

    /**
     * Обновить приход.
     *
     * @param receipt приход с заполненным id
     */
    void update(MaterialReceipt receipt) throws SQLException;

    /**
     * Удалить приход по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
