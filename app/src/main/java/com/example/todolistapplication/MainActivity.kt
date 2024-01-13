package com.example.todolistapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistapplication.adapter.ListAdapter
import com.example.todolistapplication.databinding.ActivityMainBinding
import com.example.todolistapplication.viewmodel.ToDoListViewModel


import com.inavi.myapplication.model.OnItemClick
import com.inavi.myapplication.model.ToDoList

import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), OnItemClick {

    val list = mutableListOf<ToDoList>()
    val c = Calendar.getInstance()
    val month: Int = c.get(Calendar.MONTH)
    val year: Int = c.get(Calendar.YEAR)
    val day: Int = c.get(Calendar.DAY_OF_MONTH)
    var cal = Calendar.getInstance()
    private val listAdapter = ListAdapter(list, this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ToDoListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this)[ToDoListViewModel::class.java]

       binding.rvTodoList.layoutManager = LinearLayoutManager(this)
        binding.rvTodoList.adapter = listAdapter
        binding.vieModel = viewModel


        viewModel.getPreviousList()

        viewModel.toDoList.observe(this, androidx.lifecycle.Observer {
            //list.addAll(it)
            if (it == null)
                return@Observer

            list.clear()
            val tempList = mutableListOf<ToDoList>()
            it.forEach {
                tempList.add(
                    ToDoList(
                        title = it.title,
                        date = it.date,
                        time = it.time,
                        indexDb = it.id,
                        isShow = it.isShow
                    )
                )

            }

            list.addAll(tempList)
            listAdapter.notifyDataSetChanged()
            viewModel.position = -1;

            viewModel.toDoList.value = null
        })

        viewModel.toDoListData.observe(this, androidx.lifecycle.Observer {
            if (viewModel.position != -1) {
                list.set(viewModel.position, it)
                listAdapter.notifyItemChanged(viewModel.position)
            } else {
                list.add(it)
                listAdapter.notifyDataSetChanged()
            }
            viewModel.position = -1;
        })

        binding.etAddDate.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    binding. etAddDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                    viewModel.month = monthOfYear
                    viewModel.year = year
                    viewModel.day = dayOfMonth
                },
                year,
                month,
                day
            )

            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()

        }
        binding.etAddTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                this.cal.set(Calendar.HOUR_OF_DAY, hour)
                this.cal.set(Calendar.MINUTE, minute)

                viewModel.hour = hour
                viewModel.minute = minute

                binding.etAddTime.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }

            this.cal = cal
            TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onItemClick(v: View, position: Int) {
        showAlert(position)
    }

    private fun showAlert(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)

        // Set the dialog title and message
        alertDialogBuilder.setTitle("Alert Dialog")
        alertDialogBuilder.setMessage(list.get(position).title)

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("Edit") { dialog, which ->
            viewModel.title.set(list.get(position).title)
            viewModel.date.set(list.get(position).date)
            viewModel.time.set(list.get(position).time)
            viewModel.position = position
            viewModel.index = list.get(position).indexDb
            binding.etAddTime.isFocusable = true
        }

        // Create and show the alert dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onStop() {
        super.onStop()
    }

}