package org.example.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.example.entity.DayActivity;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileStorageService {

    private static final Path DATA_DIR =
            Paths.get(System.getProperty("user.home"), ".daily-task-tracker");
    private static final Path DATA_FILE = DATA_DIR.resolve("data.json");

    private final ObjectMapper mapper;
    private Map<LocalDate, DayActivity> cache;

    public FileStorageService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        cache = new HashMap<>(); // Initialize empty map
    }

    @PostConstruct
    private void init() {
        try {
            if (Files.notExists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }

            if (Files.exists(DATA_FILE)) {
                cache = mapper.readValue(
                        DATA_FILE.toFile(),
                        mapper.getTypeFactory().constructMapType(
                                Map.class, LocalDate.class, DayActivity.class
                        )
                );
            }
        } catch (IOException e) {
            System.err.println("Warning: Failed to load task data. Starting with empty dataset.");
            e.printStackTrace();
            cache = new HashMap<>(); // fallback to empty map
        }
    }

    public synchronized Map<LocalDate, DayActivity> loadAll() {
        return cache;
    }

    public synchronized void saveAll(Map<LocalDate, DayActivity> data) {
        cache = data;
        persist();
    }

    private void persist() {
        try {
            if (Files.notExists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            mapper.writeValue(DATA_FILE.toFile(), cache);
        } catch (IOException e) {
            System.err.println("Warning: Failed to save task data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
