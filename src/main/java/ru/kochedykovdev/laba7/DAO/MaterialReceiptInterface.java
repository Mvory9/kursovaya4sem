package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

public class MaterialReceiptInterface {
    private Long id;
    private MaterialCardInterface material;    // FK на MaterialCard
    private SupplierInterface supplierInterface;        // FK на Supplier
    private Double quantity;
    private int price;
    private LocalDate receiptDate;
}