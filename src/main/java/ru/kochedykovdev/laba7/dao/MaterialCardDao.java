package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.MaterialCard;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с карточками материалов в базе данных.
 */
public interface MaterialCardDao {

    /**
     * Добавить материал.
     *
     * @param card материал (не null)
     */
    void insert(MaterialCard card) throws SQLException;

    /**
     * Найти материал по id.
     *
     * @param id номер записи
     * @return материал или пусто, если не найден
     */
    Optional<MaterialCard> findById(Long id) throws SQLException;

    /** Получить все материалы. */
    List<MaterialCard> findAll() throws SQLException;

    /**
     * Обновить материал.
     *
     * @param card материал с заполненным id
     */
    void update(MaterialCard card) throws SQLException;

    /**
     * Удалить материал по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
