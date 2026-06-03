package ru.kochedykovdev.laba7.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

public class LoginDialog {

    public Optional<LoginResult> showAndWait() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(Messages.bundle.getString("login.title"));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        Label userLabel = new Label(Messages.bundle.getString("login.username"));
        TextField userField = new TextField();
        Label passLabel = new Label(Messages.bundle.getString("login.password"));
        PasswordField passField = new PasswordField();

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        Button btnOk = new Button(Messages.bundle.getString("login.submit"));
        Button btnCancel = new Button(Messages.bundle.getString("login.cancel"));
        HBox buttonBar = new HBox(10, btnOk, btnCancel);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        VBox root = new VBox(15, grid, buttonBar);
        root.setPadding(new Insets(10));

        final String[] username = {null};
        final String[] password = {null};
        final boolean[] okClicked = {false};

        btnOk.setOnAction(e -> {
            String u = userField.getText().trim();
            String p = passField.getText();
            if (u.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, Messages.bundle.getString("error.header"),
                        Messages.bundle.getString("login.error.emptyUsername"));
                return;
            }
            username[0] = u;
            password[0] = p;
            okClicked[0] = true;
            dialogStage.close();
        });

        btnCancel.setOnAction(e -> {
            okClicked[0] = false;
            dialogStage.close();
        });

        dialogStage.setScene(new Scene(root, 320, 160));
        dialogStage.showAndWait();

        if (okClicked[0]) {
            return Optional.of(new LoginResult(username[0], password[0]));
        }
        return Optional.empty();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Getter
    @AllArgsConstructor
    public static class LoginResult {
        private final String username;
        private final String password;
    }
}
