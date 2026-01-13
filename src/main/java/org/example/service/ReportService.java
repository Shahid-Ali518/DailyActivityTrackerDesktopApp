package org.example.service;

import org.example.entity.DayActivity;
import org.example.dto.TaskReport;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ReportService {

    private static final Logger LOGGER = Logger.getLogger(ReportService.class.getName());

    // Calculates the task report summary from a collection of DayActivity
    private TaskReport calculate(Collection<DayActivity> days) {
        if (days == null || days.isEmpty()) {
            return new TaskReport(0, 0, 0, 0.0);
        }

        try {
            int total = days.stream().mapToInt(DayActivity::totalTasks).sum();
            int completed = days.stream().mapToInt(DayActivity::completedTasks).sum();
            int pending = total - completed;
            double rate = total == 0 ? 0 : (completed * 100.0) / total;

            return new TaskReport(total, completed, pending, rate);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating task report", e);
            // return an empty report on failure
            return new TaskReport(0, 0, 0, 0.0);
        }
    }

    // Weekly report: calculates tasks between start date and start + 6 days
    public TaskReport weekly(LocalDate start, Collection<DayActivity> all) {
        if (all == null) {
            LOGGER.warning("Weekly report called with null collection");
            return new TaskReport(0, 0, 0, 0.0);
        }

        try {
            return calculate(
                    all.stream()
                            .filter(d -> {
                                LocalDate date = d.getDate();
                                return !date.isBefore(start) && !date.isAfter(start.plusDays(6));
                            })
                            .toList()
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to calculate weekly report for start date: " + start, e);
            return new TaskReport(0, 0, 0, 0.0);
        }
    }

    // Monthly report: calculates tasks in a given YearMonth
    public TaskReport monthly(YearMonth month, Collection<DayActivity> all) {
        if (all == null) {
            LOGGER.warning("Monthly report called with null collection");
            return new TaskReport(0, 0, 0, 0.0);
        }

        try {
            return calculate(
                    all.stream()
                            .filter(d -> YearMonth.from(d.getDate()).equals(month))
                            .toList()
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to calculate monthly report for month: " + month, e);
            return new TaskReport(0, 0, 0, 0.0);
        }
    }
}
