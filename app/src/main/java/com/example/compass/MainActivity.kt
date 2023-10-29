package com.example.compass

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.newFixedThreadPoolContext

class MainActivity : AppCompatActivity() {
    private lateinit var list: ListView
    private lateinit var button: Button
    private lateinit var items: ArrayList<String>
    private lateinit var itemsAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.lsit)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            addition(it)
        }

        items = ArrayList()
        itemsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        list.adapter = itemsAdapter

        list.setOnItemLongClickListener { parent, view, position, id ->
            remove(position)
            true
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
            items.add(itemText)
            itemsAdapter.notifyDataSetChanged()
            input.text.clear()
        } else {
            Toast.makeText(applicationContext, "Please Enter Text...", Toast.LENGTH_LONG).show()
        }
    }
}
