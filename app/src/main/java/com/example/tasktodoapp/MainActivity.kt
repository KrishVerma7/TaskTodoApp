package com.example.tasktodoapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var todoRv: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf<TodoModel>()
//    private lateinit var simpleItemTouchCallback:ItemTouchHelper.Callback
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
        initSwipe()

        todoRv.adapter = todoAdapter

        db.todoDao().getTask().observe(this, Observer {
            if(!it.isNullOrEmpty()){
                todoList.clear()
                todoList.addAll(it)
                todoAdapter.notifyDataSetChanged()
            }else{
                todoList.clear()
                todoAdapter.notifyDataSetChanged()
            }
        })

        //FloatingActionButton
        val fab: FloatingActionButton = findViewById(R.id.actionbtn)
        fab.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
    }

    //implementing swipe functions
    fun initSwipe(){
        val simpleItemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if(direction == ItemTouchHelper.LEFT){
                    GlobalScope.launch(Dispatchers.IO) {
                        db.todoDao().deleteTask(todoAdapter.getItemId(position))
                    }
                }else if(direction == ItemTouchHelper.RIGHT){
                    GlobalScope.launch(Dispatchers.IO){
                        db.todoDao().finishedTask(todoAdapter.getItemId(position))
                    }
                }
            }

            override fun onChildDraw(
                canvas: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    val itemView = viewHolder.itemView

                    val paint = Paint()
                    val icon:Bitmap

                    if(dX>0){
                        icon = BitmapFactory.decodeResource(resources,R.mipmap.tick)

                        paint.color = getResources().getColor(R.color.dark_blue)
                        canvas.drawRect(
                            itemView.left.toFloat(),itemView.top.toFloat(),
                            itemView.left.toFloat()+ dX,itemView.bottom.toFloat(),paint
                        )

                        canvas.drawBitmap(
                            icon,
                            itemView.left.toFloat(),
                            (itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2),
                            paint
                        )


                    }else{
                        icon = BitmapFactory.decodeResource(resources,R.mipmap.cross)

                        paint.color = getResources().getColor(R.color.teal_200)
                        canvas.drawRect(
                            itemView.right.toFloat() + dX,itemView.top.toFloat(),
                            itemView.right.toFloat(),itemView.bottom.toFloat(),paint
                        )

                        canvas.drawBitmap(
                            icon,
                            itemView.right.toFloat() - icon.width,
                            (itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height.toFloat()) / 2),
                            paint
                        )
                    }
                    viewHolder.itemView.translationX = dX

                }
            else {
                    super.onChildDraw(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallBack)
        itemTouchHelper.attachToRecyclerView(todoRv)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        val item = menu.findItem(R.id.search)
        val searchView= item.actionView as SearchView
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                displayTodo()
                return true
            }

        })
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()){
                    displayTodo(newText)
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun displayTodo(newText: String = ""){
        db.todoDao().getTask().observe(this, Observer {
            if(it.isNotEmpty()){
                todoList.clear()
                todoList.addAll(
                    it.filter { todo->
                        todo.title.contains(newText,true)
                    }
                )
                todoAdapter.notifyDataSetChanged()
            }
        })
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

    fun openNewTask(view: View){
        startActivity(Intent(this, TaskActivity::class.java))
    }
}