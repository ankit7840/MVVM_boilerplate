package com.example.mvvm_practise.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.mvvm_practise.R
import com.example.mvvm_practise.viewModel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddProductBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var editTextProductName: EditText
    private lateinit var editTextProductPrice: EditText
    private lateinit var editTextProductType: EditText
    private lateinit var editTextProductTax: EditText
    private lateinit var buttonAddProduct: Button

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_add_product, container, false)

        editTextProductName = view.findViewById(R.id.editTextProductName)
        editTextProductPrice = view.findViewById(R.id.editTextProductPrice)
        editTextProductType = view.findViewById(R.id.editTextProductType)
        editTextProductTax = view.findViewById(R.id.editTextProductTax)
        buttonAddProduct = view.findViewById(R.id.buttonAddProduct)


        buttonAddProduct.setOnClickListener {
            val name = editTextProductName.text.toString().trim()
            val price = editTextProductPrice.text.toString().trim()
            val type = editTextProductType.text.toString().trim()
            val tax = editTextProductTax.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || type.isEmpty() || tax.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val priceValue = price.toFloatOrNull() ?: 0f
                val taxValue = tax.toFloatOrNull() ?: 0f

                viewModel.addProduct(name, priceValue, type, taxValue)
                observeAddProductResult()
                dismiss()
            }
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return view
    }


    private fun observeAddProductResult() {
        viewModel.addProductResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                Toast.makeText(
                    context,
                    "Product added successfully: ${response.message}",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            }.onFailure { exception ->
                Toast.makeText(
                    context,
                    "Failed to add product: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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