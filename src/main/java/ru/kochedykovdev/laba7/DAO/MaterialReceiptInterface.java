package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

/**
 * Приход материала на склад
 * Форма «Приход», увеличивает {@link MaterialStockInterface}
 */
public class MaterialReceiptInterface {

    /** id записи в БД */
    private Long id;

    /** материал, FK {@link MaterialCardInterface} */
    private MaterialCardInterface material;

    /** поставщик, FK {@link SupplierInterface} */
    private SupplierInterface supplierInterface;

    /** количество */
    private Double quantity;

    /** цена за единицу */
    private int price;

    /** дата прихода */
    private LocalDate receiptDate;
}
