package com.example.mvvm_practise.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_practise.R
import com.example.mvvm_practise.model.TaskEntity
import com.example.mvvm_practise.repository.UserRepository
import com.example.mvvm_practise.viewModel.UserViewModel
import com.example.mvvm_practise.viewModel.UserViewModelFactory
import java.util.Date
import kotlin.getValue

class TaskDetailActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by viewModels {
        val repository = UserRepository(this)
        UserViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val editTextTaskTitle = findViewById<EditText>(R.id.editTextTaskTitle1)
        val editTextTaskDescription = findViewById<EditText>(R.id.editTextTaskDescription1)
        val radioGroupPriority = findViewById<RadioGroup>(R.id.radioGroupPriority1)
        val buttonAddTask = findViewById<Button>(R.id.buttonAddTask1)

        val title = intent.getStringExtra("taskTitle")
        val taskId = intent.getIntExtra("taskId", -1)
        val description = intent.getStringExtra("taskDescription")
        val dueDate = Date(intent.getLongExtra("taskDueDate", 0L))
        val priority = intent.getStringExtra("taskPriority")

        editTextTaskTitle.setText(title)
        editTextTaskDescription.setText(description)


        when (priority) {
            "High" -> radioGroupPriority.check(R.id.radioHigh)
            "Medium" -> radioGroupPriority.check(R.id.radioMedium)
            "Low" -> radioGroupPriority.check(R.id.radioLow)
        }

        buttonAddTask.setOnClickListener {
            val updatedTitle = editTextTaskTitle.text.toString()
            val updatedDescription = editTextTaskDescription.text.toString()
            val date1 = dueDate
            val selectedPriorityId = radioGroupPriority.checkedRadioButtonId
            val updatedPriority = when (selectedPriorityId) {
                R.id.radioHigh -> "High"
                R.id.radioMedium -> "Medium"
                R.id.radioLow -> "Low"
                else -> ""
            }

            if (updatedTitle.isEmpty() || updatedDescription.isEmpty() || updatedPriority.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            } else {
                val updatedTask =
                    TaskEntity(taskId, updatedTitle, updatedDescription, date1, updatedPriority)
                viewModel.updateTask(updatedTask)
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Navigate back to the previous activity
            }
        }
    }
}