package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.entity.DayActivity;
import org.example.entity.Task;
import org.example.service.DayActivityService;
import org.example.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodayTaskView {

    private final TaskService taskService;
    private final DayActivityService dayActivityService;

    private final VBox view = new VBox(15);
    private final VBox taskContainer = new VBox(10);

    private final ProgressBar progressBar = new ProgressBar(0);
    private final Label progressLabel = new Label();

    public TodayTaskView(TaskService taskService, DayActivityService activityService) {
        this.taskService = taskService;
        this.dayActivityService = activityService;
        view.setPadding(new Insets(20));
    }

    public VBox getView() {
        view.getChildren().clear();

        Label header = new Label("Today's Tasks");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        progressBar.setPrefWidth(300);
        progressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        VBox progressBox = new VBox(5, progressBar, progressLabel);

        view.getChildren().addAll(
                header,
                progressBox,
                createAddTaskSection(),
                taskContainer
        );

        refreshTaskList();
        return view;
    }

    /* ---------------- Add Task Section ---------------- */

    private Node createAddTaskSection() {
        HBox box = new HBox(10);
        box.setPadding(new Insets(10, 0, 10, 0));

        TextField taskField = new TextField();
        taskField.setPromptText("Enter new task");

        Button addBtn = new Button("Add Task");
        addBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        addBtn.setOnAction(e -> {
            String title = taskField.getText().trim();
            if (!title.isEmpty()) {
                taskService.addTask(LocalDate.now(), title);
                taskField.clear();
                refreshTaskList();
            }
        });

        box.getChildren().addAll(taskField, addBtn);
        return box;
    }

    /* ---------------- Task List ---------------- */

    private void refreshTaskList() {
        taskContainer.getChildren().clear();

        DayActivity today = dayActivityService.getOrCreate(LocalDate.now());
        updateProgress(today);

        if (today.getTasks().isEmpty()) {
            Label empty = new Label("No tasks yet. Add one above.");
            empty.setStyle("-fx-font-style: italic; -fx-text-fill: #555;");
            taskContainer.getChildren().add(empty);
            return;
        }

        ListView<Task> listView = new ListView<>();
        listView.getItems().addAll(today.getTasks());

        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setGraphic(null);
                    return;
                }

                CheckBox checkBox = new CheckBox(task.getTitle());
                checkBox.setSelected(task.isCompleted());

                Label timeLabel = new Label();
                timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888;");

                if (task.isCompleted()) {
                    checkBox.setStyle(
                            "-fx-text-fill: #777; " +
                                    "-fx-strikethrough: true;"
                    );

                    if (task.getCompletedAt() != null) {
                        timeLabel.setText(
                                "Completed at " +
                                        task.getCompletedAt()
                                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        );
                    }
                }

                checkBox.setOnAction(e -> {
                    if (checkBox.isSelected()) {
                        taskService.completeTask(LocalDate.now(), task.getId());
                    } else {
                        taskService.markPending(LocalDate.now(), task.getId());
                    }
                    refreshTaskList();
                });

                VBox wrapper = new VBox(2, checkBox, timeLabel);
                setGraphic(wrapper);
            }
        });

        taskContainer.getChildren().add(listView);
    }

    /* ---------------- Progress ---------------- */

    private void updateProgress(DayActivity today) {
        int total = today.totalTasks();
        int completed = today.completedTasks();

        double progress = total == 0 ? 0 : (double) completed / total;
        progressBar.setProgress(progress);

        progressLabel.setText(
                completed + " of " + total + " tasks completed"
        );
    }
}
