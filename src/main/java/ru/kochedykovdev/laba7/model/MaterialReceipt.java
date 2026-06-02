package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialReceipt {

    private Long id;
    private Long materialId;
    private Long supplierId;
    private BigDecimal quantity;
    private BigDecimal price;
    private LocalDate receiptDate;
}
