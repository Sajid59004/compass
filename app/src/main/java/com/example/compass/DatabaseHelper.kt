package com.example.compass

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.content.ContentValues
import java.util.ArrayList

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun getAllTasks(): List<MainActivity.TaskItem> {
        val taskList = mutableListOf<MainActivity.TaskItem>()
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_NAME,  // The table to query
            null,  // The array of columns to return (null to return all)
            null,  // The columns for the WHERE clause
            null,  // The values for the WHERE clause
            null,  // don't group the rows
            null,  // don't filter by row groups
            null  // The sort order
        )

        while (cursor.moveToNext()) {
            val taskId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val taskText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT))
            val isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1
            val createdTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_TIME))
            val completedTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_TIME))

            val taskItem = MainActivity.TaskItem(taskText, isCompleted, createdTime, completedTime)
            taskList.add(taskItem)
        }

        cursor.close()
        return taskList
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ToDoList.db"
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "id"
        const val COLUMN_TEXT = "text"
        const val COLUMN_IS_COMPLETED = "is_completed"
        const val COLUMN_CREATED_TIME = "created_time"
        const val COLUMN_COMPLETED_TIME = "completed_time"

        // Define your SQL commands for creating and deleting the table here
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TEXT TEXT, $COLUMN_IS_COMPLETED INTEGER, $COLUMN_CREATED_TIME TEXT, $COLUMN_COMPLETED_TIME TEXT)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
