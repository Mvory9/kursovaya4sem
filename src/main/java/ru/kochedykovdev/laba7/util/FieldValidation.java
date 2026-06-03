package ru.kochedykovdev.laba7.util;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public final class FieldValidation {

    private FieldValidation() {
    }

    /** Оставляет в поле только число (с точкой). */
    public static void onlyDecimal(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*(\\.\\d*)?")) {
                field.setText(oldVal);
            }
        });
    }

    /** Оставляет в поле только целое число. */
    public static void onlyInteger(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                field.setText(oldVal);
            }
        });
    }

    /** Кнопка неактивна, пока форма невалидна. */
    public static void bindSubmit(Button button, BooleanBinding formValid) {
        button.disableProperty().bind(formValid.not());
    }
}
