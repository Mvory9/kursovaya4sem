package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

/**
 * Расходная накладная - заголовок выдачи материала на объект
 * Строки накладной - {@link InvoiceItemInterface}
 */
public class InvoiceInterface {

    /** id записи в БД */
    private Long id;

    /** номер накладной */
    private String invoiceNumber;

    /** объект, FK {@link BuildingObjectInterface} */
    private BuildingObjectInterface object;

    /** дата выдачи */
    private LocalDate issueDate;
}
