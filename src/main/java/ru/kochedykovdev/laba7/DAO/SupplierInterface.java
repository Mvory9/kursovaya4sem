package ru.kochedykovdev.laba7.DAO;

/**
 * Поставщик материалов - справочник контрагентов
 * Используется в приходе ({@link MaterialReceiptInterface})
 */
public class SupplierInterface {

    /** id записи в БД */
    private Long id;

    /** наименование организации или ИП */
    private String name;

    /** контактное лицо */
    private String contactPerson;

    /** телефон */
    private String phone;

    /** email */
    private String email;

    /** адрес */
    private String address;
}
