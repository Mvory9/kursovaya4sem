package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialReturn {

    private Long id;
    private Long invoiceId;
    private Long materialId;
    private BigDecimal quantity;
    private LocalDate returnDate;
}
