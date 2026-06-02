package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.Prorab;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с прорабами в базе данных.
 */
public interface ProrabDao {

    /**
     * Добавить прораба.
     *
     * @param prorab прораб (не null)
     */
    void insert(Prorab prorab) throws SQLException;

    /**
     * Найти прораба по id.
     *
     * @param id номер записи
     * @return прораб или пусто, если не найден
     */
    Optional<Prorab> findById(Long id) throws SQLException;

    /** Получить всех прорабов. */
    List<Prorab> findAll() throws SQLException;

    /**
     * Обновить прораба.
     *
     * @param prorab прораб с заполненным id
     */
    void update(Prorab prorab) throws SQLException;

    /**
     * Удалить прораба по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
