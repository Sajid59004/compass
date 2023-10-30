package com.example.compass
import android.provider.BaseColumns

object TaskContract {
    object TaskEntry : BaseColumns {
        const val TABLE_NAME = "tasks"
        const val _ID = BaseColumns._ID
        const val COLUMN_TASK_NAME = "name"
        const val COLUMN_IS_COMPLETED = "is_completed"
        const val COLUMN_CREATED_TIME = "created_time"
        const val COLUMN_COMPLETED_TIME = "completed_time"
    }
}