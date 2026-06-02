package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    private Long id;
    private Long invoiceId;
    private Long materialId;
    private BigDecimal quantity;
    private BigDecimal priceAtMoment;
}
