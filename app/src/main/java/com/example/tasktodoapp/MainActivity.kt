package com.example.tasktodoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var todoRv: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<TodoModel>()

    val db by lazy {
        AppDatabase.getDatabase(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar:Toolbar = findViewById(R.id.toolbar)
        todoRv = findViewById(R.id.todoRv)

        setSupportActionBar(toolbar)

        todoAdapter = TodoAdapter(todoList)

        todoRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.todoAdapter
        }

        todoRv.adapter = todoAdapter

        db.todoDao().getTask().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                todoList.clear()
                todoList.addAll(it)
                todoAdapter.notifyDataSetChanged()
            }
        })

        val fab: FloatingActionButton = findViewById(R.id.actionbtn)
        fab.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    //this is similar to OnClickListner
    //this starts the HistoryActivity using intent
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.history -> {
                startActivity(Intent(this,HistoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}