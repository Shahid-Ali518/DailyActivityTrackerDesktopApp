package org.example.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SplashView {

    private final VBox view = new VBox(20);

    public SplashView() {
        view.setAlignment(Pos.CENTER);

        Label greeting = new Label("Welcome to Daily Task Tracker!");
        greeting.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        greeting.setStyle("-fx-text-fill: linear-gradient(to right, #4A00E0, #8E2DE2);");

        Label subtitle = new Label("Stay productive and track your daily activities.");
        subtitle.setFont(Font.font("Segoe UI", 16));
        subtitle.setStyle("-fx-text-fill: #555;");

        view.getChildren().addAll(greeting, subtitle);
    }

    public VBox getView() {
        return view;
    }
}

