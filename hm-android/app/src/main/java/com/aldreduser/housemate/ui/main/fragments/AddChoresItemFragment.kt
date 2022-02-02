package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.aldreduser.housemate.R
import com.aldreduser.housemate.databinding.FragmentAddChoresItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.necessaryAreFilled

class AddChoresItemFragment : Fragment() {

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
                val necessaryAreFilled = necessaryAreFilled(
                    itemNameInput.toString(),
                    "placeholder"
                )
                if (necessaryAreFilled) {
                    addItem()
                }
                val navController = Navigation.findNavController(requireParentFragment().requireView())
                navController.navigate(R.id.action_addChoresItemFragment_to_startFragment)

            }
        }
        setupAppBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(fragmentTag, "onDestroyView: AddChoresItemFragment")
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
            listsViewModel.sendChoresItemToDatabase(
                itemNameInput.text.toString(),
                difficulty,
                whenNeededInput.text.toString(),
                priority
            )
        }
    }

    // SET UP FUNCTIONS //
    private fun setupAppBar() {
        binding!!.apply {
            addItemTopAppbar.title = "Add Chore Item"
            addItemTopAppbar.setNavigationOnClickListener {
                // todo: handle navigation click
            }
        }
    }
}