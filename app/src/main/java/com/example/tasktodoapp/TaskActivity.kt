package com.example.tasktodoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar


const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private val labels = arrayListOf<String>("Personal", "Business", "Insurance", "Shopping", "Banking")
    lateinit var timeInptLay: TextInputLayout
    lateinit var timeEdt: TextInputEditText
    lateinit var dateEdt: TextInputEditText

    val db by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val dateEdt:TextInputEditText= findViewById(R.id.dateEdt)
        val timeInptLay:TextInputLayout = findViewById(R.id.timeInptLay)
        val timeEdt:TextInputEditText=findViewById(R.id.timeEdt)

        dateEdt.setOnClickListener(this)
        timeInptLay.setOnClickListener(this)

        setUpSpinner()
    }

//Spinner
    private fun setUpSpinner() {
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)
        labels.sort()
        val spinnerCategory: Spinner = findViewById(R.id.spinnerCategory)
        spinnerCategory.adapter = adapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.dateEdt -> {
                setListener()
            }

            R.id.timeEdt -> {
                setTimeListener()
            }
        }
    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener( ) { _, hourOfDay, min ->
            myCalendar.set(Calendar.HOUR, hourOfDay)
            myCalendar.set(Calendar.MINUTE, min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()

    }

    private fun updateTime() {
         timeEdt=findViewById(R.id.timeEdt)
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        timeEdt.setText(sdf.format(myCalendar.time))
    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_YEAR, dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_YEAR)
        )
        //setting min date to current time
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val dateEdt: TextInputEditText = findViewById(R.id.dateEdt)
        val timeInptLay: TextInputLayout = findViewById(R.id.timeInptLay)
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)

        dateEdt.setText(sdf.format(myCalendar.time))
        timeInptLay.visibility = View.VISIBLE
    }
}