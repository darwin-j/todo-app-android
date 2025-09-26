package com.example.todoapp.db;

// Android imports for SQLite database
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// App import
import com.example.todoapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    private static final String DB_NAME = "todo_db"; // Database name
    private static final int DB_VERSION = 1;         // Database version

    // Table and column constants
    private static final String TABLE_TASKS = "tasks"; // Table name
    private static final String COL_ID = "id";         // Primary key
    private static final String COL_TASK = "task";     // Task description
    private static final String COL_STATUS = "status"; // Task status (0 = pending, 1 = completed)

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Called when database is first created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TASK + " TEXT, "
                + COL_STATUS + " INTEGER)";
        db.execSQL(createTable); // Execute SQL to create table
    }

    // Called when database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old table and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Insert a new task
    public long addTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable DB
        ContentValues values = new ContentValues();
        values.put(COL_TASK, task);   // Add task text
        values.put(COL_STATUS, 0);    // Default status = pending
        return db.insert(TABLE_TASKS, null, values); // Insert and return row ID
    }

    // Read all tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable DB
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, null);

        // Loop through rows
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            String taskText = cursor.getString(cursor.getColumnIndexOrThrow(COL_TASK));
            int status = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STATUS));
            tasks.add(new Task(id, taskText, status)); // Add to list
        }
        cursor.close(); // Close cursor
        return tasks;   // Return list of tasks
    }

    // Update an existing task
    public int updateTask(int id, String newTask, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK, newTask);   // Update task text
        values.put(COL_STATUS, status);  // Update status
        return db.update(TABLE_TASKS, values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Delete a task
    public int deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASKS, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Count tasks by status (0 = pending, 1 = completed)
    public int getCountByStatus(int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_TASKS + " WHERE " + COL_STATUS + "=?",
                new String[]{String.valueOf(status)}
        );
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0); // Get count
        }
        cursor.close();
        return count;
    }
}
