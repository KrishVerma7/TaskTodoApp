package com.example.tasktodoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.sql.RowSetListener

const val DB_NAME= "todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    val db by lazy{
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            DB_NAME
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val dateEdt:TextInputEditText=findViewById(R.id.dateEdt)
        val timeEdt:TextInputEditText=findViewById(R.id.timeEdt)

        dateEdt.setOnClickListener(this)
    }
    override fun onClick(v: View){
        when(v.id){
            R.id.dateEdt ->{
                setListener()
            }
            R.id.timeEdt ->{
                setTimeListener()
            }
        }
    }

    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, min ->
            myCalendar.set(Calendar.HOUR,hourOfDay)
            myCalendar.set(Calendar.MINUTE,min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(
            this,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),false
        )
        timePickerDialog.show()    }

    private fun updateTime() {
        val  myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)

        val dateEdt:TextInputEditText=findViewById(R.id.dateEdt)
        dateEdt.setText(sdf.format(myCalendar.time))

        val timeInptLay: TextInputLayout = findViewById(R.id.timeEdt)
        timeInptLay.visibility=View.VISIBLE
    }

    private fun setListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_YEAR,dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(
            this,dateSetListener,myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_YEAR)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)

        val dateEdt:TextInputEditText=findViewById(R.id.dateEdt)
        dateEdt.setText(sdf.format(myCalendar.time))

        val timeInptLay: TextInputLayout = findViewById(R.id.timeEdt)
        timeInptLay.visibility=View.VISIBLE
    }
}