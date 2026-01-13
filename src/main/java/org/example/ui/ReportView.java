package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.entity.DayActivity;
import org.example.service.DayActivityService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportView {

    private final DayActivityService activityService;
    private final VBox view = new VBox(20);

    public ReportView(DayActivityService activityService) {
        this.activityService = activityService;
        view.setPadding(new Insets(20));
    }

    public VBox getView() {
        view.getChildren().clear();

        Label header = new Label("Productivity Analytics");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Map<LocalDate, DayActivity> all = activityService.getAll();

        if (all == null || all.isEmpty()) {
            view.getChildren().addAll(
                    header,
                    emptyMessage("No activity data available yet.\nStart completing tasks to see analytics.")
            );
            return view;
        }

        VBox selectorBox = createSelectors(all);
        view.getChildren().addAll(header, selectorBox);

        return view;
    }

    /* ===================== SELECTORS ===================== */

    private VBox createSelectors(Map<LocalDate, DayActivity> all) {
        VBox box = new VBox(15);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        Button dailyBtn = new Button("Daily Report");
        Button weeklyBtn = new Button("Weekly Report");
        Button monthlyBtn = new Button("Monthly Report");

        Label message = new Label();
        message.setStyle("-fx-text-fill: #c0392b;");

        HBox controls = new HBox(10, datePicker, dailyBtn, weeklyBtn, monthlyBtn);

        dailyBtn.setOnAction(e -> {
            view.getChildren().removeIf(n -> n instanceof Chart || n instanceof HBox && n != controls);
            renderDaily(all, datePicker.getValue(), message);
        });

        weeklyBtn.setOnAction(e -> {
            view.getChildren().removeIf(n -> n instanceof Chart || n instanceof HBox && n != controls);
            renderWeekly(all, datePicker.getValue(), message);
        });

        monthlyBtn.setOnAction(e -> {
            view.getChildren().removeIf(n -> n instanceof Chart || n instanceof HBox && n != controls);
            renderMonthly(all, YearMonth.from(datePicker.getValue()), message);
        });

        box.getChildren().addAll(controls, message);
        return box;
    }

    /* ===================== DAILY ===================== */

    private void renderDaily(Map<LocalDate, DayActivity> all, LocalDate date, Label msg) {
        msg.setText("");

        if (date.isAfter(LocalDate.now())) {
            msg.setText("Future dates are not allowed.");
            return;
        }

        DayActivity day = all.get(date);
        if (day == null || day.totalTasks() == 0) {
            msg.setText("No tasks found for " + date);
            return;
        }

        view.getChildren().add(createSingleDayChart(day));
    }

    /* ===================== WEEKLY ===================== */

    private void renderWeekly(Map<LocalDate, DayActivity> all, LocalDate date, Label msg) {
        msg.setText("");

        LocalDate start = date.minusDays(6);
        List<DayActivity> data = filterRange(all, start, date);

        if (data.isEmpty()) {
            msg.setText("No data available for selected week.");
            return;
        }

        view.getChildren().add(createWeeklyBarChart(data));
    }

    /* ===================== MONTHLY ===================== */

    private void renderMonthly(Map<LocalDate, DayActivity> all, YearMonth month, Label msg) {
        msg.setText("");

        if (month.isAfter(YearMonth.now())) {
            msg.setText("Future months are not allowed.");
            return;
        }

        List<DayActivity> data = filterMonth(all, month);
        if (data.isEmpty()) {
            msg.setText("No data available for " + month);
            return;
        }

        view.getChildren().add(createMonthlyTrendChart(data));
    }

    /* ===================== CHARTS ===================== */

    private BarChart<String, Number> createSingleDayChart(DayActivity d) {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setTitle("Daily Report - " + d.getDate());

        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Tasks");
        s.getData().add(new XYChart.Data<>("Completed", d.completedTasks()));
        s.getData().add(new XYChart.Data<>("Pending", d.totalTasks() - d.completedTasks()));

        chart.getData().add(s);
        return chart;
    }

    private BarChart<String, Number> createWeeklyBarChart(List<DayActivity> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Weekly Task Completion");

        XYChart.Series<String, Number> completed = new XYChart.Series<>();
        completed.setName("Completed");

        XYChart.Series<String, Number> pending = new XYChart.Series<>();
        pending.setName("Pending");

        data.forEach(d -> {
            completed.getData().add(new XYChart.Data<>(d.getDate().toString(), d.completedTasks()));
            pending.getData().add(new XYChart.Data<>(d.getDate().toString(),
                    d.totalTasks() - d.completedTasks()));
        });

        chart.getData().addAll(completed, pending);
        return chart;
    }

    private LineChart<String, Number> createMonthlyTrendChart(List<DayActivity> data) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 100, 10);

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Monthly Completion Trend");

        XYChart.Series<String, Number> trend = new XYChart.Series<>();
        trend.setName("Completion %");

        data.forEach(d -> {
            int total = d.totalTasks();
            int completed = d.completedTasks();
            double pct = total == 0 ? 0 : (completed * 100.0) / total;
            trend.getData().add(new XYChart.Data<>(d.getDate().toString(), pct));
        });

        chart.getData().add(trend);
        return chart;
    }

    /* ===================== UTIL ===================== */

    private List<DayActivity> filterRange(Map<LocalDate, DayActivity> all,
                                          LocalDate start, LocalDate end) {
        return all.values().stream()
                .filter(d -> !d.getDate().isBefore(start) && !d.getDate().isAfter(end))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private List<DayActivity> filterMonth(Map<LocalDate, DayActivity> all, YearMonth month) {
        return all.values().stream()
                .filter(d -> YearMonth.from(d.getDate()).equals(month))
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .collect(Collectors.toList());
    }

    private Label emptyMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-style: italic; -fx-text-fill: #666;");
        return label;
    }
}
