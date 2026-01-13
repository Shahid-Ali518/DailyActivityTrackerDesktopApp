package org.example.service;

import org.example.entity.DayActivity;
import org.example.storage.FileStorageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DayActivityService {

    private static final Logger LOGGER = Logger.getLogger(DayActivityService.class.getName());

    private final FileStorageService storage;
    private final Map<LocalDate, DayActivity> activities;

    public DayActivityService(FileStorageService storage) {
        this.storage = storage;

        Map<LocalDate, DayActivity> loaded;
        try {
            loaded = storage.loadAll();
            if (loaded == null) {
                loaded = new HashMap<>();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load activities from storage. Starting with empty map.", e);
            loaded = new HashMap<>();
        }

        this.activities = loaded;
    }


    public DayActivity getOrCreate(LocalDate date) {
        try {
            return activities.computeIfAbsent(date, DayActivity::new);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating or retrieving DayActivity for date: " + date, e);
            // fallback: return a temporary DayActivity without storing it
            return new DayActivity(date);
        }
    }

    // all day activities
    public Map<LocalDate, DayActivity> getAll() {
        return Collections.unmodifiableMap(activities);
    }

    // store all to storage
    public void persist() {
        try {
            storage.saveAll(activities);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to persist activities to storage.", e);
            // optionally: notify user via UI or throw custom exception
        }
    }
}
