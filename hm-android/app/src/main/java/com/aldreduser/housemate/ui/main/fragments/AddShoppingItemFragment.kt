package com.aldreduser.housemate.ui.main.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.FragmentAddShoppingItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.displayToast
import com.aldreduser.housemate.util.necessaryAreFilled
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*

class AddShoppingItemFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val fragmentTag = "AddShopItemTAG"
    private var binding: FragmentAddShoppingItemBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding =
            FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fabAddItem.setOnClickListener {
                addItemClicked()
            }
            whenNeededBtn.setOnClickListener {
                whenNeededClicked()
            }
        }
        setupAppBar()
        val itemToEdit = listsViewModel.itemToEdit.value
        if (itemToEdit != null) {
            setItemToView(itemToEdit as ShoppingItem)
            listsViewModel.setItemToEdit(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(fragmentTag, "onDestroy: AddShoppingItemFragment")
    }

    // Triggered when the user picks a date
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val dateToDisplay = "${month+1}/$dayOfMonth/$year"
        when_needed_btn.text = dateToDisplay
    }

    // CLICK HANDLERS //
    private fun addItemClicked() {
        val necessaryAreFilled = necessaryAreFilled(
            binding!!.itemNameInput.text.toString(),
            binding!!.itemQuantityInput.text.toString()
        )
        if (necessaryAreFilled) {
            addItem()
            val navController = Navigation
                .findNavController(requireParentFragment().requireView())
            navController.navigate(R.id.action_addShoppingItemFragment_to_startFragment)
        } else {
            displayToast(requireContext(),"Fill boxed marked with *")
        }
    }
    private fun whenNeededClicked() {
        val calendarDate = listsViewModel.getDateTimeCalendar()
        DatePickerDialog(
            requireContext(), this,
            calendarDate.year, calendarDate.month, calendarDate.day
        ).show()
    }
    // CLICK HANDLERS //


    // SET UP FUNCTIONS //
    private fun setupAppBar() {
        //title
        binding!!.apply {
            addItemTopAppbar.title = "Add Shopping Item"
            addItemTopAppbar.setNavigationOnClickListener {
                // todo: handle navigation click
            }
        }
    }

    // HELPERS //
    private fun setItemToView(itemToEdit: ShoppingItem) {
        binding?.apply {
            itemQuantityInput.setText(itemToEdit.quantity.toString())
            itemNameInput.setText(itemToEdit.name)
            if (!itemToEdit.neededBy.isNullOrEmpty()) whenNeededBtn.text = itemToEdit.neededBy
            whereToGetInput.setText(itemToEdit.purchaseLocation)
            costInput.setText(itemToEdit.cost.toString())
        }
    }

    private fun addItem() {
        binding!!.apply {
            val qty: Double = if (itemQuantityInput.text.toString().isEmpty()) 0.0 else {
                itemQuantityInput.text.toString().toDouble()
            }
            val cost: Double = if (costInput.text.toString().isEmpty()) 0.0 else {
                costInput.text.toString().toDouble()
            }
            val priority = when (choosePriorityButton.checkedRadioButtonId) {
                priorityButton1.id -> 1
                priorityButton3.id -> 3
                else -> 2
            }
            val whenNeeded =
                if(whenNeededBtn.text.toString() != getString(R.string.hint_when_needed)) {
                whenNeededBtn.text.toString()
            } else ""
            listsViewModel.sendItemToDatabase(
                listsViewModel.listTypes[0],
                itemNameInput.text.toString(),
                qty, cost,
                whereToGetInput.text.toString(),
                whenNeeded,
                priority, 0
            )
        }
    }
    // HELPERS //
}