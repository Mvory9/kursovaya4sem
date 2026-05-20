package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

public class MaterialReturnInterface {
    private Long id;
    private InvoiceInterface invoiceInterface;          // FK на Invoice
    private MaterialCardInterface material;    // FK на MaterialCard
    private Double quantity;
    private LocalDate returnDate;
}