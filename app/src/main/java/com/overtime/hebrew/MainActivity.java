package com.overtime.hebrew;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * MainActivity - Main screen for Hebrew Overtime Tracker
 * Using proven working pattern - programmatic UI creation, no XML layouts
 */
public class MainActivity extends Activity {
    
    private static final String TAG = "HebrewOvertimeApp";
    private TextView welcomeText;
    private Button addTimeButton;
    private TextView summaryText;
    private int clickCount = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "🏗️ onCreate() called - Hebrew Overtime Tracker starting!");
        
        // Create UI programmatically for GitHub Actions compatibility
        createUI();
        
        // Set up event listeners
        setupEventListeners();
        
        Log.d(TAG, "✅ onCreate() completed - Hebrew Overtime Tracker ready!");
    }
    
    /**
     * Create UI programmatically (no XML layouts for GitHub Actions compatibility)
     */
    private void createUI() {
        // Create main welcome text with Hebrew
        welcomeText = new TextView(this);
        welcomeText.setText("🕐 מעקב שעות נוספות\nHebrew Overtime Tracker\n🏗️ Built with GitHub Actions");
        welcomeText.setTextSize(18);
        welcomeText.setPadding(40, 40, 40, 30);
        welcomeText.setGravity(android.view.Gravity.CENTER);
        
        // Create add time button
        addTimeButton = new Button(this);
        addTimeButton.setText("הוסף שעות נוספות");
        addTimeButton.setTextSize(16);
        addTimeButton.setPadding(40, 20, 40, 20);
        
        // Create summary text
        summaryText = new TextView(this);
        summaryText.setTextSize(14);
        summaryText.setPadding(40, 20, 40, 40);
        summaryText.setGravity(android.view.Gravity.CENTER);
        
        // Update summary display
        updateSummaryDisplay();
        
        // Create vertical layout
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setGravity(android.view.Gravity.CENTER);
        layout.setPadding(30, 30, 30, 30);
        
        // Add views to layout
        layout.addView(welcomeText);
        layout.addView(addTimeButton);
        layout.addView(summaryText);
        
        // Set the layout as content view
        setContentView(layout);
        
        Log.d(TAG, "🎨 UI created programmatically with Hebrew support");
    }
    
    /**
     * Set up event listeners
     */
    private void setupEventListeners() {
        addTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddTimeClick();
            }
        });
        
        Log.d(TAG, "👆 Event listeners set up");
    }
    
    /**
     * Handle add time button click
     */
    private void handleAddTimeClick() {
        clickCount++;
        
        Log.d(TAG, "🕐 Add time clicked! Count: " + clickCount);
        
        updateSummaryDisplay();
        showHebrewMessage();
    }
    
    /**
     * Update summary display
     */
    private void updateSummaryDisplay() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        
        // Simulate overtime tracking
        double simulatedHours = clickCount * 0.5; // Each click = 30 minutes
        
        String summary = String.format(
            "חודש %d/%d\nשעות נוספות: %.1f\nרשומות: %d",
            month, year, simulatedHours, clickCount
        );
        
        summaryText.setText(summary);
        
        Log.d(TAG, "📊 Summary updated: " + simulatedHours + " hours, " + clickCount + " entries");
    }
    
    /**
     * Show Hebrew toast message
     */
    private void showHebrewMessage() {
        String message;
        
        if (clickCount == 1) {
            message = "🎉 מעולה! הוספת את הרשומה הראשונה!";
        } else if (clickCount <= 5) {
            message = "👍 כל הכבוד! " + clickCount + " רשומות";
        } else if (clickCount <= 10) {
            message = "🔥 אתה במסלול הנכון! " + clickCount + " רשומות";
        } else {
            message = "🏆 מדהים! " + clickCount + " רשומות ועוד!";
        }
        
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        
        Log.d(TAG, "💬 Showed Hebrew message: " + message);
    }
}
}