package com.overtime.hebrew.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.overtime.hebrew.R;
import com.overtime.hebrew.database.OvertimeDatabase;
import com.overtime.hebrew.models.OvertimeEntry;
import com.overtime.hebrew.utils.HebrewCalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * MainActivity - Main screen for Hebrew Overtime Tracker
 * 
 * Educational Note: Using Activity (not AppCompatActivity) for maximum compatibility
 * This follows the proven working pattern from the successful Android configuration
 */
public class MainActivity extends Activity {
    
    // UI Components
    private Button dateButton;
    private EditText hoursInput;
    private EditText minutesInput;
    private EditText notesInput;
    private Button saveButton;
    private TextView totalHoursText;
    private TextView requiredHoursText;
    private TextView balanceText;
    private ListView entriesList;
    
    // Data
    private OvertimeDatabase database;
    private String selectedDate;
    private List<OvertimeEntry> recentEntries;
    private ArrayAdapter<String> entriesAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize database
        database = new OvertimeDatabase(this);
        
        // Initialize UI components
        initializeViews();
        
        // Set up event listeners
        setupEventListeners();
        
        // Load initial data
        loadMonthlyData();
        loadRecentEntries();
        
        // Set default date to today
        selectedDate = HebrewCalendarUtils.getCurrentDateString();
        updateDateButton();
    }
    
    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        dateButton = findViewById(R.id.dateButton);
        hoursInput = findViewById(R.id.hoursInput);
        minutesInput = findViewById(R.id.minutesInput);
        notesInput = findViewById(R.id.notesInput);
        saveButton = findViewById(R.id.saveButton);
        totalHoursText = findViewById(R.id.totalHoursText);
        requiredHoursText = findViewById(R.id.requiredHoursText);
        balanceText = findViewById(R.id.balanceText);
        entriesList = findViewById(R.id.entriesList);
        
        // Initialize entries list
        recentEntries = new ArrayList<>();
        entriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        entriesList.setAdapter(entriesAdapter);
    }
    
    /**
     * Set up event listeners for UI components
     */
    private void setupEventListeners() {
        // Date picker button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        
        // Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOvertimeEntry();
            }
        });
    }
    
    /**
     * Show date picker dialog
     */
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // month is 0-based, so add 1
                    selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    updateDateButton();
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }
    
    /**
     * Update date button text with Hebrew formatting
     */
    private void updateDateButton() {
        if (selectedDate != null) {
            java.util.Date date = HebrewCalendarUtils.parseDate(selectedDate);
            if (date != null) {
                String hebrewDate = HebrewCalendarUtils.formatDateInHebrew(date);
                dateButton.setText(hebrewDate);
            } else {
                dateButton.setText(selectedDate);
            }
        }
    }
    
    /**
     * Save overtime entry to database
     */
    private void saveOvertimeEntry() {
        try {
            // Validate inputs
            if (selectedDate == null || selectedDate.isEmpty()) {
                Toast.makeText(this, "אנא בחר תאריך", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String hoursStr = hoursInput.getText().toString().trim();
            String minutesStr = minutesInput.getText().toString().trim();
            
            if (hoursStr.isEmpty() && minutesStr.isEmpty()) {
                Toast.makeText(this, "אנא הזן שעות או דקות", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int hours = hoursStr.isEmpty() ? 0 : Integer.parseInt(hoursStr);
            int minutes = minutesStr.isEmpty() ? 0 : Integer.parseInt(minutesStr);
            String notes = notesInput.getText().toString().trim();
            
            // Validate ranges
            if (hours < 0 || hours > 23) {
                Toast.makeText(this, "שעות חייבות להיות בין 0 ל-23", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (minutes < 0 || minutes > 59) {
                Toast.makeText(this, "דקות חייבות להיות בין 0 ל-59", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (hours == 0 && minutes == 0) {
                Toast.makeText(this, "אנא הזן לפחות דקה אחת", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Create and save entry
            OvertimeEntry entry = new OvertimeEntry(selectedDate, hours, minutes, notes);
            long id = database.addOvertimeEntry(entry);
            
            if (id > 0) {
                Toast.makeText(this, "נשמר בהצלחה!", Toast.LENGTH_SHORT).show();
                
                // Clear form
                hoursInput.setText("");
                minutesInput.setText("");
                notesInput.setText("");
                
                // Refresh data
                loadMonthlyData();
                loadRecentEntries();
            } else {
                Toast.makeText(this, "שגיאה בשמירה", Toast.LENGTH_SHORT).show();
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "אנא הזן מספרים תקינים", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Load and display monthly summary data
     */
    private void loadMonthlyData() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar is 0-based
        
        // Calculate total hours for current month
        double totalHours = database.getTotalHoursForMonth(currentYear, currentMonth);
        
        // For now, assume 20 required hours per month (can be made configurable)
        double requiredHours = 20.0;
        double balance = totalHours - requiredHours;
        
        // Update UI
        totalHoursText.setText(String.format("%.1f שעות", totalHours));
        requiredHoursText.setText(String.format("%.1f שעות", requiredHours));
        
        if (balance >= 0) {
            balanceText.setText(String.format("+%.1f שעות", balance));
            balanceText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            balanceText.setText(String.format("%.1f שעות", balance));
            balanceText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }
    
    /**
     * Load and display recent entries
     */
    private void loadRecentEntries() {
        recentEntries = database.getAllOvertimeEntries();
        
        List<String> entryStrings = new ArrayList<>();
        for (OvertimeEntry entry : recentEntries) {
            java.util.Date date = HebrewCalendarUtils.parseDate(entry.getDate());
            String dateStr = (date != null) ? HebrewCalendarUtils.formatDateInHebrew(date) : entry.getDate();
            String timeStr = HebrewCalendarUtils.formatTimeInHebrew(entry.getHours(), entry.getMinutes());
            
            String entryStr = dateStr + " - " + timeStr;
            if (entry.getNotes() != null && !entry.getNotes().isEmpty()) {
                entryStr += " (" + entry.getNotes() + ")";
            }
            
            entryStrings.add(entryStr);
        }
        
        entriesAdapter.clear();
        entriesAdapter.addAll(entryStrings);
        entriesAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
    }
}