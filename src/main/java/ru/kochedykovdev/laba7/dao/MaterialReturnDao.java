package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.MaterialReturn;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с возвратами материалов на склад.
 */
public interface MaterialReturnDao {

    /**
     * Добавить возврат.
     *
     * @param materialReturn возврат (не null)
     */
    void insert(MaterialReturn materialReturn) throws SQLException;

    /**
     * Найти возврат по id.
     *
     * @param id номер записи
     * @return возврат или пусто, если не найден
     */
    Optional<MaterialReturn> findById(Long id) throws SQLException;

    /** Получить все возвраты. */
    List<MaterialReturn> findAll() throws SQLException;

    /**
     * Обновить возврат.
     *
     * @param materialReturn возврат с заполненным id
     */
    void update(MaterialReturn materialReturn) throws SQLException;

    /**
     * Удалить возврат по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
