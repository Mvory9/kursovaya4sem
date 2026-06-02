package ru.kochedykovdev.laba7.DAO;

/**
 * Инструмент - справочник складского инструмента
 * Связан с выдачей ({@link ToolIssueInterface})
 */
public class ToolInterface {

    /** id записи в БД */
    private Long id;

    /** наименование */
    private String name;

    /** инвентарный номер */
    private String inventoryNumber;

    /** состояние (исправен, в ремонте и тд) */
    private String condition;
}
