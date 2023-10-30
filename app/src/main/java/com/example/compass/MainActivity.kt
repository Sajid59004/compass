package com.example.compass

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var button: Button
    private lateinit var items: ArrayList<TaskItem>
    private lateinit var itemsAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.list)
        button = findViewById(R.id.button)

        // Load tasks from the database
        val databaseHelper = DatabaseHelper(applicationContext)
        items = databaseHelper.getAllTasks() as ArrayList<TaskItem>

        button.setOnClickListener {
            addition(it)
        }

        itemsAdapter = TaskAdapter(this, items)
        list.adapter = itemsAdapter

        list.setOnItemLongClickListener { _, _, position, _ ->
            remove(position)
            true
        }

        list.setOnItemClickListener { _, _, position, _ ->
            markCompleted(position)
        }
    }


    private fun remove(position: Int) {
        val context: Context = applicationContext
        Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show()
        items.removeAt(position)
        itemsAdapter.notifyDataSetChanged()
    }

    private fun addition(view: View) {
        val input = findViewById<EditText>(R.id.edit_text)
        val itemText = input.text.toString()

        if (itemText.isNotEmpty()) {
            val taskItem = TaskItem(itemText)

            // Initialize the database helper and get a writable database
            val databaseHelper = DatabaseHelper(applicationContext)
            val db = databaseHelper.writableDatabase

// Create ContentValues to insert the task into the database
            val contentValues = ContentValues().apply {
                put(DatabaseHelper.COLUMN_TEXT, taskItem.text)
                put(DatabaseHelper.COLUMN_IS_COMPLETED, if (taskItem.isCompleted) 1 else 0)
                put(DatabaseHelper.COLUMN_CREATED_TIME, taskItem.createdTime)
                put(DatabaseHelper.COLUMN_COMPLETED_TIME, taskItem.completedTime)
            }

// Insert the task into the database
            val newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues)

// Add the task to your ArrayList and update the adapter
            items.add(taskItem)
            itemsAdapter.notifyDataSetChanged()

            input.text.clear()
        }
    }

    private fun markCompleted(position: Int) {
        val context: Context = applicationContext
        val taskItem = items[position]

        if (taskItem.isCompleted) {
            Toast.makeText(context, "Task already marked as completed.", Toast.LENGTH_LONG).show()
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())

            taskItem.isCompleted = true
            taskItem.completedTime = currentDate

            itemsAdapter.notifyDataSetChanged()
            Toast.makeText(context, "Task marked as completed.", Toast.LENGTH_LONG).show()
            moveCompletedTasksToBottom()
        }
    }

    private fun moveCompletedTasksToBottom() {
        items.sortWith(compareBy { it.isCompleted })
        itemsAdapter.notifyDataSetChanged()
    }

    data class TaskItem(
        val text: String,
        var isCompleted: Boolean = false,
        var createdTime: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
        var completedTime: String = ""
    )

    class TaskAdapter(context: Context, private val tasks: ArrayList<TaskItem>) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): TaskItem {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            var view = convertView
            if (view == null) {
                view = inflater.inflate(R.layout.list_item, parent, false)
            }

            val taskItem = getItem(position)

            val taskText = view?.findViewById<TextView>(R.id.task_text)
            taskText?.text = taskItem.text

            val radioButton = view?.findViewById<RadioButton>(R.id.radio_button)
            radioButton?.isChecked = taskItem.isCompleted
            radioButton?.setOnClickListener {
                taskItem.isCompleted = radioButton.isChecked
            }

            val createdTimeText = view?.findViewById<TextView>(R.id.task_created_time)
            createdTimeText?.text = "Created: ${taskItem.createdTime}"

            val completedTimeText = view?.findViewById<TextView>(R.id.task_completed_time)
            if (taskItem.isCompleted) {
                completedTimeText?.visibility = View.VISIBLE
                completedTimeText?.text = "Completed: ${taskItem.completedTime}"
            } else {
                completedTimeText?.visibility = View.GONE
            }

            return view
        }
    }
}
