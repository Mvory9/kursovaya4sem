package ru.kochedykovdev.laba7.DAO;

import java.time.LocalDateTime;

/**
 * Остаток материала на складе
 * Меняется при приходе, выдаче и возврате
 */
public class MaterialStockInterface {

    /** id записи в БД */
    private Long id;

    /** материал, FK {@link MaterialCardInterface} */
    private MaterialCardInterface material;

    /** количество на складе */
    private Double quantity;

    /** когда последний раз обновляли остаток */
    private LocalDateTime lastUpdated;
}
