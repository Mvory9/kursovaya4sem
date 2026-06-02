package ru.kochedykovdev.laba7.DAO;

/**
 * Прораб - ответственное лицо на стройке
 * Может быть привязан к объекту ({@link BuildingObjectInterface})
 */
public class ProrabInterface {

    /** id записи в БД */
    private Long id;

    /** ФИО */
    private String name;

    /** телефон */
    private String phone;
}
