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
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.FragmentAddChoresItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.displayToast
import com.aldreduser.housemate.util.necessaryAreFilled
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*

class AddChoresItemFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private val fragmentTag = "AddChoreItemTAG"
    private var binding: FragmentAddChoresItemBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding =
            FragmentAddChoresItemBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.apply {
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
            setItemToView(itemToEdit as ChoresItem)
            listsViewModel.setItemToEdit(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(fragmentTag, "onDestroyView: AddChoresItemFragment")
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
            "placeholder"
        )
        Log.d(fragmentTag, "necessaryAreFilled: $necessaryAreFilled")
        if (necessaryAreFilled) {
            addItem()
            val navController = Navigation
                .findNavController(requireParentFragment().requireView())
            navController.navigate(R.id.action_addChoresItemFragment_to_startFragment)
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
        binding!!.apply {
            addItemTopAppbar.title = "Add Chore Item"
            addItemTopAppbar.setNavigationOnClickListener {
                // todo: handle navigation click
            }
        }
    }

    // HELPERS //
    private fun setItemToView(itemToEdit: ChoresItem) {
        binding?.apply {
            itemNameInput.setText(itemToEdit.name)
            if (!itemToEdit.neededBy.isNullOrEmpty()) whenNeededBtn.text = itemToEdit.neededBy
        }
    }
    private fun addItem() {
        binding!!.apply {
            val difficulty = when (chooseDifficultyButton.checkedRadioButtonId) {
                difficultyButton1.id -> 1
                difficultyButton3.id -> 3
                else -> 2
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
                listsViewModel.listTypes[1],
                itemNameInput.text.toString(),
                0.0, 0.0,
                "",
                whenNeeded,
                priority, difficulty
            )
        }
    }
    // HELPERS //
}