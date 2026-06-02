package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private Long id;
    private String invoiceNumber;
    private Long objectId;
    private LocalDate issueDate;
}
