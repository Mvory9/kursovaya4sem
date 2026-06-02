module ru.kochedykovdev.laba7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.compiler;


    opens ru.kochedykovdev.laba7 to javafx.fxml;
    exports ru.kochedykovdev.laba7;
    exports ru.kochedykovdev.laba7.controllers;
    opens ru.kochedykovdev.laba7.controllers to javafx.fxml;
}