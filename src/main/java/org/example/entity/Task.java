package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    private String id;
    private String title;
    private TaskStatus status;

    private LocalDateTime completedAt;


    // constructor
    public Task(String title){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.status = TaskStatus.PENDING;
    }


    // mark completed
    public void markCompleted() {
        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    public void markPending(){
        this.status = TaskStatus.PENDING;
        this.completedAt = LocalDateTime.now();
    }

    // check whether is completed
    public boolean isCompleted(){
        return status == TaskStatus.COMPLETED;
    }





}
