package com.overtime.hebrew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.overtime.hebrew.models.OvertimeEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * OvertimeDatabase - SQLite database helper for Hebrew Overtime Tracker
 * Replicates the database structure from the Python web version
 */
public class OvertimeDatabase extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "overtime_tracker.db";
    private static final int DATABASE_VERSION = 1;
    
    // Table names (matching Python version)
    private static final String TABLE_WORK_DAYS = "work_days";
    private static final String TABLE_OVERTIME_ENTRIES = "overtime_entries";
    private static final String TABLE_MONTHLY_SUMMARIES = "monthly_summaries";
    
    // overtime_entries table columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_MINUTES = "minutes";
    private static final String COLUMN_TOTAL_HOURS = "total_hours";
    private static final String COLUMN_NOTES = "notes";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    public OvertimeDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create work_days table (matching Python structure)
        String createWorkDaysTable = "CREATE TABLE " + TABLE_WORK_DAYS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT UNIQUE NOT NULL, " +
                "day_type TEXT NOT NULL DEFAULT 'work', " +
                "is_work_day BOOLEAN NOT NULL DEFAULT 1, " +
                "required_overtime_hours REAL DEFAULT 1.0, " +
                "notes TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        // Create overtime_entries table (matching Python structure)
        String createOvertimeEntriesTable = "CREATE TABLE " + TABLE_OVERTIME_ENTRIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_HOURS + " INTEGER NOT NULL DEFAULT 0, " +
                COLUMN_MINUTES + " INTEGER NOT NULL DEFAULT 0, " +
                COLUMN_TOTAL_HOURS + " REAL NOT NULL, " +
                COLUMN_NOTES + " TEXT, " +
                COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_UPDATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        // Create monthly_summaries table (matching Python structure)
        String createMonthlySummariesTable = "CREATE TABLE " + TABLE_MONTHLY_SUMMARIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "year INTEGER NOT NULL, " +
                "month INTEGER NOT NULL, " +
                "total_work_days INTEGER NOT NULL, " +
                "total_required_hours REAL NOT NULL, " +
                "total_actual_hours REAL NOT NULL, " +
                "balance_hours REAL NOT NULL, " +
                "completion_percentage REAL NOT NULL, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE(year, month)" +
                ")";

        db.execSQL(createWorkDaysTable);
        db.execSQL(createOvertimeEntriesTable);
        db.execSQL(createMonthlySummariesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_DAYS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OVERTIME_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_SUMMARIES);
        onCreate(db);
    }

    /**
     * Add a new overtime entry
     */
    public long addOvertimeEntry(OvertimeEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COLUMN_DATE, entry.getDate());
        values.put(COLUMN_HOURS, entry.getHours());
        values.put(COLUMN_MINUTES, entry.getMinutes());
        values.put(COLUMN_TOTAL_HOURS, entry.getTotalHours());
        values.put(COLUMN_NOTES, entry.getNotes());
        values.put(COLUMN_CREATED_AT, getCurrentTimestamp());
        values.put(COLUMN_UPDATED_AT, getCurrentTimestamp());
        
        long id = db.insert(TABLE_OVERTIME_ENTRIES, null, values);
        db.close();
        return id;
    }

    /**
     * Get all overtime entries
     */
    public List<OvertimeEntry> getAllOvertimeEntries() {
        List<OvertimeEntry> entries = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OVERTIME_ENTRIES + " ORDER BY " + COLUMN_DATE + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                OvertimeEntry entry = new OvertimeEntry();
                entry.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                entry.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                entry.setHours(cursor.getInt(cursor.getColumnIndex(COLUMN_HOURS)));
                entry.setMinutes(cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTES)));
                entry.setTotalHours(cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_HOURS)));
                entry.setNotes(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES)));
                entry.setCreatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_AT)));
                entry.setUpdatedAt(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT)));
                
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return entries;
    }

    /**
     * Get overtime entries for a specific month
     */
    public List<OvertimeEntry> getOvertimeEntriesForMonth(int year, int month) {
        List<OvertimeEntry> entries = new ArrayList<>();
        String monthStr = String.format("%04d-%02d", year, month);
        String selectQuery = "SELECT * FROM " + TABLE_OVERTIME_ENTRIES + 
                           " WHERE " + COLUMN_DATE + " LIKE '" + monthStr + "%' " +
                           " ORDER BY " + COLUMN_DATE + " ASC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                OvertimeEntry entry = new OvertimeEntry();
                entry.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                entry.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                entry.setHours(cursor.getInt(cursor.getColumnIndex(COLUMN_HOURS)));
                entry.setMinutes(cursor.getInt(cursor.getColumnIndex(COLUMN_MINUTES)));
                entry.setTotalHours(cursor.getDouble(cursor.getColumnIndex(COLUMN_TOTAL_HOURS)));
                entry.setNotes(cursor.getString(cursor.getColumnIndex(COLUMN_NOTES)));
                
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return entries;
    }

    /**
     * Calculate total hours for a specific month
     */
    public double getTotalHoursForMonth(int year, int month) {
        double totalHours = 0;
        String monthStr = String.format("%04d-%02d", year, month);
        String selectQuery = "SELECT SUM(" + COLUMN_TOTAL_HOURS + ") as total FROM " + TABLE_OVERTIME_ENTRIES + 
                           " WHERE " + COLUMN_DATE + " LIKE '" + monthStr + "%'";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            totalHours = cursor.getDouble(0);
        }
        
        cursor.close();
        db.close();
        return totalHours;
    }

    /**
     * Get current timestamp in SQLite format
     */
    private String getCurrentTimestamp() {
        return "datetime('now')";
    }
}