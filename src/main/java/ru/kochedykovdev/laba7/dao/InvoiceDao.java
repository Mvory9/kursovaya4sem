package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.Invoice;
import ru.kochedykovdev.laba7.model.InvoiceItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с расходными накладными.
 */
public interface InvoiceDao {

    /**
     * Добавить накладную.
     *
     * @param invoice накладная (не null)
     */
    void insert(Invoice invoice) throws SQLException;

    /**
     * Оформить выдачу материала (накладная, позиция, списание остатка) через fn_issue_material.
     *
     * @param invoice заголовок накладной (id заполняется после вызова)
     * @param item    позиция накладной (invoiceId заполняется после вызова)
     */
    void issueMaterial(Invoice invoice, InvoiceItem item) throws SQLException;

    /**
     * Найти накладную по id.
     *
     * @param id номер записи
     * @return накладная или пусто, если не найдена
     */
    Optional<Invoice> findById(Long id) throws SQLException;

    /** Получить все накладные. */
    List<Invoice> findAll() throws SQLException;

    /**
     * Обновить накладную.
     *
     * @param invoice накладная с заполненным id
     */
    void update(Invoice invoice) throws SQLException;

    /**
     * Удалить накладную по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
