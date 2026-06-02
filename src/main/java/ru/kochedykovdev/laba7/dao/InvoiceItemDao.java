package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.InvoiceItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с позициями расходных накладных.
 */
public interface InvoiceItemDao {

    /**
     * Добавить позицию накладной.
     *
     * @param item позиция (не null)
     */
    void insert(InvoiceItem item) throws SQLException;

    /**
     * Найти позицию по id.
     *
     * @param id номер записи
     * @return позиция или пусто, если не найдена
     */
    Optional<InvoiceItem> findById(Long id) throws SQLException;

    /** Получить все позиции. */
    List<InvoiceItem> findAll() throws SQLException;

    /**
     * Обновить позицию.
     *
     * @param item позиция с заполненным id
     */
    void update(InvoiceItem item) throws SQLException;

    /**
     * Удалить позицию по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
