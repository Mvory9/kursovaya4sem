package ru.kochedykovdev.laba7.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private TableView<?> tableViewMaterials;  // таблица для материалов
    @FXML
    private TableView<?> tableViewSuppliers;  // таблица поставщиков
    @FXML
    private TableView<?> tableViewTools;      // таблица инструментов
    @FXML
    private TableView<?> tableViewStocks;     // таблица остатков

    // Кнопка "Приход"
    @FXML
    private void onPrihodClick() {
        openWindow("prihod-view.fxml", "Приход материала");
    }

    // Кнопка "Выдать материал"
    @FXML
    private void onGiveMaterialClick() {
        openWindow("give-material-view.fxml", "Выдача материала");
    }

    // Кнопка "Выдать инструмент"
    @FXML
    private void onGiveToolClick() {
        openWindow("give-tool-view.fxml", "Выдача инструмента");
    }

    // Кнопка "Вернуть инструмент"
    @FXML
    private void onReturnToolClick() {
        openWindow("return-tool-view.fxml", "Возврат инструмента");
    }

    // Общий метод для открытия окон
    private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ru/kochedykovdev/laba7/" + fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onRefreshClick() {
        System.out.println("Обновление данных..."); // TODO
    }

    @FXML
    private void onSearchClick() {
        System.out.println("Поиск..."); // TODO
    }
}