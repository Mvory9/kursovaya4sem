package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDateTime;

public class MaterialStockInterface {
    private Long id;
    private MaterialCardInterface material;    // FK на MaterialCard
    private Double quantity;
    private LocalDateTime lastUpdated;
}