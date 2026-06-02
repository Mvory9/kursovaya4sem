package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

/**
 * Возврат материала на склад с объекта
 * По ранее выданной накладной, увеличивает остаток
 */
public class MaterialReturnInterface {

    /** id записи в БД */
    private Long id;

    /** накладная выдачи, FK {@link InvoiceInterface} */
    private InvoiceInterface invoiceInterface;

    /** материал, FK {@link MaterialCardInterface} */
    private MaterialCardInterface material;

    /** количество */
    private Double quantity;

    /** дата возврата */
    private LocalDate returnDate;
}
