package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDate;

public class InvoiceInterface {
    private Long id;
    private String invoiceNumber;
    private BuildingObjectInterface object;    // FK на BuildingObject
    private LocalDate issueDate;
}