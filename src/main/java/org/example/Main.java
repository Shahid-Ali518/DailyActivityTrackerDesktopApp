package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.config.SpringConfig;
import org.example.service.DayActivityService;
import org.example.service.TaskService;
import org.example.ui.NavBar;
import org.example.ui.ReportView;
import org.example.ui.SplashView;
import org.example.ui.TodayTaskView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Objects;

public class Main extends Application {

    private AnnotationConfigApplicationContext springContext;


    private static BorderPane root;

    @Override
    public void init() {
        springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @Override
    public void start(Stage stage) {

        root = new BorderPane();

        // Show splash initially
        SplashView splash = new SplashView();
        root.setCenter(splash.getView());

        // render navbar at top
        NavBar navBar = new NavBar(this::switchView);
        root.setTop(navBar.createNavBar());


        Scene scene = new Scene(root, 900, 600);
        // styling the scene
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styles/app.css")).toExternalForm()
        );

        // set taskbar image of app
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tasktrackerimage.ico")));

        stage.setScene(scene);
        stage.setTitle("Daily Task Tracker");
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }

    // method to switch views
    private void switchView(String view) {
        switch (view) {
            case "TODAY" -> root.setCenter(new TodayTaskView(springContext.getBean(
                    TaskService.class),
                    springContext.getBean(DayActivityService.class)


            ).getView());
            case "REPORTS" -> root.setCenter(new ReportView(springContext.getBean(
                    DayActivityService.class
            )).getView());
//            case "ADD" -> root.setCenter(new AddTaskView().getView());
        }
    }
    public static void main(String[] args) {
        launch(args);
    }


}