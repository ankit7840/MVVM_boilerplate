package com.example.mvvm_practise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mvvm_practise.R
import com.example.mvvm_practise.viewModel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddProductBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var taskTitle: EditText
    private lateinit var taskDescription: EditText
    private lateinit var radioGroupPriority: RadioGroup

    private lateinit var buttonAddTask: Button

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_product, container, false)

        taskTitle = view.findViewById(R.id.editTextTaskTitle)
        taskDescription = view.findViewById(R.id.editTextTaskDescription)
        buttonAddTask = view.findViewById(R.id.buttonAddTask)
        radioGroupPriority = view.findViewById(R.id.radioGroupPriority)


        buttonAddTask.setOnClickListener {

            val title = taskTitle.text.toString().trim()
            val Description = taskDescription.text.toString().trim()
            val selectedPriorityId = radioGroupPriority.checkedRadioButtonId
            val selectedPriority =
                view.findViewById<RadioButton>(selectedPriorityId)?.text.toString()



            if (title.isEmpty() || Description.isEmpty() || selectedPriorityId == -1) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {

                viewModel.addTask(title, Description, selectedPriority)
                Toast.makeText(context, "Task added successfully", Toast.LENGTH_SHORT).show()
//                taskTitle.text.clear()
//                taskDescription.text.clear()
//                radioGroupPriority.clearCheck()
                dismiss()
            }
        }

        // for managing  kyboard does not overlap the code
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return view
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog as BottomSheetDialog
        val bottomSheetInternal =
            bottomSheet.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheetInternal?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

}