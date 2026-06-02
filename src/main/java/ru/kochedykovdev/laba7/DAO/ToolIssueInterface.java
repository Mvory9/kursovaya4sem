package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

/**
 * Выдача инструмента на объект
 * Формы «Выдать инструмент» и «Вернуть инструмент»
 */
public class ToolIssueInterface {

    /** id записи в БД */
    private Long id;

    /** инструмент, FK {@link ToolInterface} */
    private ToolInterface toolInterface;

    /** объект, FK {@link BuildingObjectInterface} */
    private BuildingObjectInterface object;

    /** кому выдан */
    private String issuedTo;

    /** дата выдачи */
    private LocalDate issueDate;

    /** плановый срок возврата */
    private LocalDate returnDate;

    /** фактический возврат, null пока не вернули */
    private LocalDate actualReturnDate;
}
