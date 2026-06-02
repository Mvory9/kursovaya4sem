package ru.kochedykovdev.laba7.DAO;

/**
 * Карточка материала - справочник номенклатуры склада
 * Наименование, артикул, единица измерения, норма списания
 */
public class MaterialCardInterface {

    /** id записи в БД */
    private Long id;

    /** наименование материала */
    private String name;

    /** артикул */
    private Integer article;

    /** единица измерения (кг, м³, шт) */
    private String unit;

    /** норма списания при выдаче */
    private Integer writeOffRate;
}
