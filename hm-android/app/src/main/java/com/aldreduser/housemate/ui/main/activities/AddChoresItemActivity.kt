package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.databinding.ActivityAddChoresItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.necessaryAreFilled

class AddChoresItemActivity : AppCompatActivity() {

    private val tag = "AddChoreItemTAG"
    private var binding: ActivityAddChoresItemBinding? = null
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddChoresItemBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        listsViewModel = ViewModelProvider(this)[ListsViewModel::class.java]
        binding!!.apply {
            lifecycleOwner = this@AddChoresItemActivity
            viewModel = listsViewModel
            fabAddItem.setOnClickListener {
                val necessaryAreFilled = necessaryAreFilled(
                    itemNameInput.toString(),
                    "placeholder"
                )
                if (necessaryAreFilled) {
                    addItem()
                }
            }
        }
        setupAppBar()
    }

    override fun onDestroy() {
        binding = null
        Log.i(tag, "onDestroy: AddChoreItemActivity")
        super.onDestroy()
    }

    // CLICK HANDLERS //
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