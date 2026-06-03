module ru.kochedykovdev.laba7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires static lombok;

    opens ru.kochedykovdev.laba7 to javafx.fxml;
    opens ru.kochedykovdev.laba7.controllers to javafx.fxml;

    exports ru.kochedykovdev.laba7;
    exports ru.kochedykovdev.laba7.controllers;
    exports ru.kochedykovdev.laba7.model;
    exports ru.kochedykovdev.laba7.dao;
    exports ru.kochedykovdev.laba7.dao.impl;
    exports ru.kochedykovdev.laba7.util;
}
