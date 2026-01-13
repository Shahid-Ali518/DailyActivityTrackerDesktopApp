package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayActivity {

    private LocalDate date;
    private List<Task> tasks = new ArrayList<>();


    public DayActivity(LocalDate date) {
        this.date = date;
    }


    // add task
    public void addTask(Task task){
        this.tasks.add(task);
    }

    public int totalTasks() {
        return tasks.size();
    }

    public int completedTasks() {
        return (int) tasks.stream().filter(Task::isCompleted).count();
    }
}
