package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.util.function.Consumer;

public class NavBar {

    private final Consumer<String> switchViewCallback;

    public NavBar(Consumer<String> switchViewCallback) {
        this.switchViewCallback = switchViewCallback;
    }

    public HBox createNavBar() {
        HBox nav = new HBox();
        nav.setPadding(new Insets(12, 20, 12, 20));
        nav.setStyle("-fx-background-color: linear-gradient(to right, #4A00E0, #8E2DE2);");
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setSpacing(10);

        // Left side: App Name
        Label title = new Label("Daily Task Tracker");
        title.getStyleClass().add("navbar-title");
        title.setStyle("-fx-text-fill: white");
        title.setFont(Font.font("Segoe UI", 18));

        // Spacer between left and right
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // Right side: Buttons
        Button today = createButton("Today's Work");
        today.setOnAction(e -> switchViewCallback.accept("TODAY"));

        Button reports = createButton("Reports");
        reports.setOnAction(e -> switchViewCallback.accept("REPORTS"));

        HBox buttons = new HBox(10, today, reports);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        nav.getChildren().addAll(title, spacer, buttons);
        return nav;
    }

    private Button createButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("navbar-button");
        return btn;
    }
}
