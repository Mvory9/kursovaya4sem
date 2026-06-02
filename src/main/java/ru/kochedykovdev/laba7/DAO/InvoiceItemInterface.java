package ru.kochedykovdev.laba7.DAO;

/**
 * Строка расходной накладной - материал, количество, цена
 */
public class InvoiceItemInterface {

    /** id записи в БД */
    private Long id;

    /** накладная, FK {@link InvoiceInterface} */
    private InvoiceInterface invoiceInterface;

    /** материал, FK {@link MaterialCardInterface} */
    private MaterialCardInterface material;

    /** количество */
    private Double quantity;

    /** цена на момент выдачи */
    private int priceAtMoment;
}
