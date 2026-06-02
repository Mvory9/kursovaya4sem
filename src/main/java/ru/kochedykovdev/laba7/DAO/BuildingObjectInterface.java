package ru.kochedykovdev.laba7.DAO;

/**
 * Объект строительства - куда выдают материалы и инструменты
 */
public class BuildingObjectInterface {

    /** id записи в БД */
    private Long id;

    /** наименование */
    private String name;

    /** адрес */
    private String address;

    /** прораб объекта, FK {@link ProrabInterface} */
    private ProrabInterface prorabInterface;
}
