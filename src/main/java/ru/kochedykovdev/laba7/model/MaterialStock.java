package ru.kochedykovdev.laba7.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialStock {

    private Long id;
    private Long materialId;
    private BigDecimal quantity;
    private LocalDateTime lastUpdated;
}
