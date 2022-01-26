package com.aldreduser.housemate.ui.main.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.databinding.ActivityAddShoppingItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import kotlinx.android.synthetic.main.activity_add_shopping_item.*

class AddShoppingItemActivity : AppCompatActivity() {

    private val TAG = "AddShopItemTAG"
    private var binding: ActivityAddShoppingItemBinding? = null
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddShoppingItemBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        listsViewModel = ViewModelProvider(this)[ListsViewModel::class.java]
        binding!!.apply {
            lifecycleOwner = this@AddShoppingItemActivity
            viewModel = listsViewModel
            fabAddItem.setOnClickListener {
                // todo: check if all necessary data is filled
                addItem()
            }
        }
        setupAppBar()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    // CLICK HANDLERS //
    private fun addItem() {
        binding!!.apply {
            val priority = when (choosePriorityButton.checkedRadioButtonId) {
                priorityButton1.id -> 1
                priorityButton3.id -> 3
                else -> 2
            }
            listsViewModel.sendShoppingItemToDatabase(
                itemNameInput.text.toString(),
                item_quantity_input.text.toString().toDouble(),
                costInput.text.toString().toDouble(),
                whereToGetInput.text.toString(),
                whenNeededInput.text.toString(),
                priority
            )
        }
    }

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

    // HELPER FUNCTIONS //
}