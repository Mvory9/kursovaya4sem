package ru.kochedykovdev.laba7.DAO;

public class InvoiceItemInterface {
    private Long id;
    private InvoiceInterface invoiceInterface;          // FK на Invoice
    private MaterialCardInterface material;    // FK на MaterialCard
    private Double quantity;
    private int priceAtMoment;
}