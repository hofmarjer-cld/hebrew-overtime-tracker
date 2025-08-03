# Hebrew Overtime Tracker - ProGuard Rules
# Keep SQLite and database related classes
-keep class * extends android.database.sqlite.SQLiteOpenHelper
-keep class com.overtime.hebrew.models.** { *; }
-keep class com.overtime.hebrew.database.** { *; }

# Keep Hebrew text rendering
-keep class android.text.** { *; }
-keep class android.widget.TextView { *; }