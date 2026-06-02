package ru.kochedykovdev.laba7.dao;

import ru.kochedykovdev.laba7.model.BuildingObject;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Работа с объектами строительства в базе данных.
 */
public interface BuildingObjectDao {

    /**
     * Добавить объект.
     *
     * @param object объект (не null)
     */
    void insert(BuildingObject object) throws SQLException;

    /**
     * Найти объект по id.
     *
     * @param id номер записи
     * @return объект или пусто, если не найден
     */
    Optional<BuildingObject> findById(Long id) throws SQLException;

    /** Получить все объекты. */
    List<BuildingObject> findAll() throws SQLException;

    /**
     * Обновить объект.
     *
     * @param object объект с заполненным id
     */
    void update(BuildingObject object) throws SQLException;

    /**
     * Удалить объект по id.
     *
     * @param id номер записи
     */
    void deleteById(Long id) throws SQLException;
}
