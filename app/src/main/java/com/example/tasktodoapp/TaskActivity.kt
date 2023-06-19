package com.example.tasktodoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar


const val DB_NAME = "todo.db"

class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var timeInptLay: TextInputLayout
    lateinit var timeEdt: TextInputEditText
    lateinit var dateEdt: TextInputEditText

    var finalDate=0L
    var finalTime=0L

    private val labels = arrayListOf<String>("Personal", "Business", "Insurance", "Shopping", "Banking")

    val db by lazy {
            AppDatabase.getDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val dateEdt:TextInputEditText= findViewById(R.id.dateEdt)
        val timeEdt:TextInputEditText=findViewById(R.id.timeEdt)
        val saveBtn:MaterialButton=findViewById(R.id.saveBtn)
        myCalendar = Calendar.getInstance()

        dateEdt.setOnClickListener(this)
        timeEdt.setOnClickListener(this)
        saveBtn.setOnClickListener(this)

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

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(year, monthOfYear, dayOfMonth)
                updateDate()
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        // Setting the minimum date to the current time
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