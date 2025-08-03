package com.overtime.hebrew.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * HebrewCalendarUtils - Utility class for Hebrew calendar and date formatting
 * Includes Israeli holidays and Hebrew date formatting
 */
public class HebrewCalendarUtils {
    
    // Hebrew month names
    private static final String[] HEBREW_MONTHS = {
        "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני",
        "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
    };
    
    // Hebrew day names
    private static final String[] HEBREW_DAYS = {
        "ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"
    };
    
    // Israeli holidays for 2025 (matching the Python version)
    private static final Map<String, String> ISRAELI_HOLIDAYS_2025 = new HashMap<>();
    static {
        // June 2025
        ISRAELI_HOLIDAYS_2025.put("2025-06-11", "ערב שבועות");
        ISRAELI_HOLIDAYS_2025.put("2025-06-12", "שבועות");
        
        // July 2025
        ISRAELI_HOLIDAYS_2025.put("2025-07-13", "צום תמוז");
        
        // September 2025
        ISRAELI_HOLIDAYS_2025.put("2025-09-15", "ערב ראש השנה");
        ISRAELI_HOLIDAYS_2025.put("2025-09-16", "ראש השנה");
        ISRAELI_HOLIDAYS_2025.put("2025-09-17", "ראש השנה");
        ISRAELI_HOLIDAYS_2025.put("2025-09-24", "ערב יום כיפור");
        ISRAELI_HOLIDAYS_2025.put("2025-09-25", "יום כיפור");
        ISRAELI_HOLIDAYS_2025.put("2025-09-29", "ערב סוכות");
        ISRAELI_HOLIDAYS_2025.put("2025-09-30", "סוכות");
    }

    /**
     * Format date in Hebrew
     * @param date Date to format
     * @return Hebrew formatted date string
     */
    public static String formatDateInHebrew(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH); // 0-based
        int year = cal.get(Calendar.YEAR);
        
        return dayOfMonth + " ב" + HEBREW_MONTHS[month] + " " + year;
    }

    /**
     * Format date in Hebrew with day name
     * @param date Date to format
     * @return Hebrew formatted date with day name
     */
    public static String formatDateWithDayInHebrew(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1; // Convert to 0-based
        String dayName = HEBREW_DAYS[dayOfWeek];
        String dateStr = formatDateInHebrew(date);
        
        return "יום " + dayName + ", " + dateStr;
    }

    /**
     * Get current date in YYYY-MM-DD format
     * @return Current date string
     */
    public static String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    /**
     * Parse date string (YYYY-MM-DD) to Date object
     * @param dateString Date string in YYYY-MM-DD format
     * @return Date object or null if parsing fails
     */
    public static Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a date is an Israeli holiday
     * @param dateString Date in YYYY-MM-DD format
     * @return Holiday name if it's a holiday, null otherwise
     */
    public static String getHolidayName(String dateString) {
        return ISRAELI_HOLIDAYS_2025.get(dateString);
    }

    /**
     * Check if a date is a holiday
     * @param dateString Date in YYYY-MM-DD format
     * @return true if it's a holiday
     */
    public static boolean isHoliday(String dateString) {
        return ISRAELI_HOLIDAYS_2025.containsKey(dateString);
    }

    /**
     * Check if a date is Friday (pre-Shabbat)
     * @param dateString Date in YYYY-MM-DD format
     * @return true if it's Friday
     */
    public static boolean isFriday(String dateString) {
        Date date = parseDate(dateString);
        if (date == null) return false;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
    }

    /**
     * Check if a date is Saturday (Shabbat)
     * @param dateString Date in YYYY-MM-DD format
     * @return true if it's Saturday
     */
    public static boolean isSaturday(String dateString) {
        Date date = parseDate(dateString);
        if (date == null) return false;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    /**
     * Get Hebrew month name
     * @param month Month number (1-12)
     * @return Hebrew month name
     */
    public static String getHebrewMonthName(int month) {
        if (month >= 1 && month <= 12) {
            return HEBREW_MONTHS[month - 1];
        }
        return "";
    }

    /**
     * Format hours and minutes in Hebrew
     * @param hours Hours
     * @param minutes Minutes
     * @return Hebrew formatted time string
     */
    public static String formatTimeInHebrew(int hours, int minutes) {
        if (minutes == 0) {
            if (hours == 1) {
                return "שעה אחת";
            } else {
                return hours + " שעות";
            }
        } else {
            String hoursText = (hours == 1) ? "שעה" : hours + " שעות";
            String minutesText = (minutes == 1) ? "דקה" : minutes + " דקות";
            return hoursText + " ו-" + minutesText;
        }
    }
}