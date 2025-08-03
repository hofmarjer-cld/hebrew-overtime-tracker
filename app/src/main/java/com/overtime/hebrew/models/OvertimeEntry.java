package com.overtime.hebrew.models;

/**
 * OvertimeEntry - Model class representing a single overtime entry
 * Corresponds to the overtime_entries table in the web version
 */
public class OvertimeEntry {
    private long id;
    private String date;        // YYYY-MM-DD format
    private int hours;          // Hours (0-23)
    private int minutes;        // Minutes (0-59)
    private double totalHours;  // Calculated total in decimal hours
    private String notes;       // Optional notes
    private String createdAt;   // Creation timestamp
    private String updatedAt;   // Last update timestamp

    // Default constructor
    public OvertimeEntry() {}

    // Constructor with essential fields
    public OvertimeEntry(String date, int hours, int minutes, String notes) {
        this.date = date;
        this.hours = hours;
        this.minutes = minutes;
        this.totalHours = hours + (minutes / 60.0);
        this.notes = notes;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getHours() { return hours; }
    public void setHours(int hours) { 
        this.hours = hours;
        calculateTotalHours();
    }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) { 
        this.minutes = minutes;
        calculateTotalHours();
    }

    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to calculate total hours in decimal format
    private void calculateTotalHours() {
        this.totalHours = hours + (minutes / 60.0);
    }

    // Format hours and minutes for display (Hebrew-friendly)
    public String getFormattedTime() {
        if (minutes == 0) {
            return hours + " שעות";
        } else {
            return hours + " שעות ו-" + minutes + " דקות";
        }
    }

    @Override
    public String toString() {
        return "OvertimeEntry{" +
                "date='" + date + '\'' +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", totalHours=" + totalHours +
                ", notes='" + notes + '\'' +
                '}';
    }
}