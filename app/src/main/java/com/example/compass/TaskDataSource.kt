package com.example.compass
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.text.SimpleDateFormat
import java.util.*

class TaskDataSource(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    fun insertTask(taskName: String) {
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_TASK_NAME, taskName)
            put(TaskContract.TaskEntry.COLUMN_IS_COMPLETED, 0)
            put(TaskContract.TaskEntry.COLUMN_CREATED_TIME, getCurrentDateTime())
            put(TaskContract.TaskEntry.COLUMN_COMPLETED_TIME, "")
        }

        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
    }

    fun updateTask(taskId: Long, isCompleted: Boolean) {
        val values = ContentValues().apply {
            put(TaskContract.TaskEntry.COLUMN_IS_COMPLETED, if (isCompleted) 1 else 0)
            put(TaskContract.TaskEntry.COLUMN_COMPLETED_TIME, getCurrentDateTime())
        }

        val selection = "${TaskContract.TaskEntry._ID} = ?"
        val selectionArgs = arrayOf(taskId.toString())

        db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    fun deleteTask(taskId: Long) {
        val selection = "${TaskContract.TaskEntry._ID} = ?"
        val selectionArgs = arrayOf(taskId.toString())

        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun getAllTasks(): Cursor {
        val projection = arrayOf(
            TaskContract.TaskEntry._ID,
            TaskContract.TaskEntry.COLUMN_TASK_NAME,
            TaskContract.TaskEntry.COLUMN_IS_COMPLETED,
            TaskContract.TaskEntry.COLUMN_CREATED_TIME,
            TaskContract.TaskEntry.COLUMN_COMPLETED_TIME
        )

        return db.query(
            TaskContract.TaskEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}
