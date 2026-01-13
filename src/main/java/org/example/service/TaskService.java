package org.example.service;

import org.example.entity.DayActivity;
import org.example.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    private final DayActivityService activityService;

    public TaskService(DayActivityService activityService) {
        this.activityService = activityService;
    }

    // Adds a new task for the given date.
    public void addTask(LocalDate date, String title) {
        if (title == null || title.isBlank()) {
            LOGGER.warning("Cannot add empty task for date: " + date);
            return;
        }

        try {
            DayActivity day = activityService.getOrCreate(date);
            Task newTask = new Task(title);
            day.addTask(newTask);
            activityService.persist();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add task '" + title + "' for date: " + date, e);
        }
    }


    // Marks a task as completed by its ID.
    public void completeTask(LocalDate date, String taskId) {
        if (taskId == null || taskId.isBlank()) {
            LOGGER.warning("Cannot complete task with empty ID for date: " + date);
            return;
        }

        try {
            DayActivity day = activityService.getOrCreate(date);
            day.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .ifPresentOrElse(
                            Task::markCompleted,
                            () -> LOGGER.warning("Task with ID '" + taskId + "' not found for date: " + date)
                    );
            activityService.persist();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to complete task with ID '" + taskId + "' for date: " + date, e);
        }
    }

    // mark pending

    public void markPending(LocalDate date, String taskId){
        if (taskId == null || taskId.isBlank()) {
            LOGGER.warning("Cannot complete task with empty ID for date: " + date);
            return;
        }

        try {
            DayActivity day = activityService.getOrCreate(date);
            day.getTasks().stream()
                    .filter(t -> t.getId().equals(taskId))
                    .findFirst()
                    .ifPresentOrElse(
                            Task::markPending,
                            () -> LOGGER.warning("Task with ID '" + taskId + "' not found for date: " + date)
                    );
            activityService.persist();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to complete task with ID '" + taskId + "' for date: " + date, e);
        }
    }
}
