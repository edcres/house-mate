package com.aldreduser.housemate.ui.main.activities
//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import androidx.lifecycle.ViewModelProvider
//import com.aldreduser.housemate.databinding.ActivityAddShoppingItemBinding
//import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
//import com.aldreduser.housemate.util.necessaryAreFilled
//
//class AddShoppingItemActivity : AppCompatActivity() {
//
//    private val tag = "AddShopItemTAG"
//    private var binding: ActivityAddShoppingItemBinding? = null
//    private lateinit var listsViewModel: ListsViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityAddShoppingItemBinding.inflate(layoutInflater)
//        setContentView(binding?.root)
//
//        listsViewModel = ViewModelProvider(this)[ListsViewModel::class.java]
//        binding!!.apply {
//            lifecycleOwner = this@AddShoppingItemActivity
//            viewModel = listsViewModel
//            fabAddItem.setOnClickListener {
//                val necessaryAreFilled = necessaryAreFilled(
//                    itemNameInput.text.toString(),
//                    itemQuantityInput.text.toString()
//                )
//                if (necessaryAreFilled) {
//                    addItem()
//                }
//            }
//        }
//        setupAppBar()
//    }
//
//    override fun onDestroy() {
//        binding = null
//        Log.i(tag, "onDestroy: AddShoppingItemActivity")
//        super.onDestroy()
//    }
//
//    // CLICK HANDLERS //
//    private fun addItem() {
//        binding!!.apply {
//            val qty: Double = if (itemQuantityInput.text.toString().isEmpty()) 0.0 else {
//                itemQuantityInput.text.toString().toDouble()
//            }
//            val cost: Double = if (costInput.text.toString().isEmpty()) 0.0 else {
//                costInput.text.toString().toDouble()
//            }
//            val priority = when (choosePriorityButton.checkedRadioButtonId) {
//                priorityButton1.id -> 1
//                priorityButton3.id -> 3
//                else -> 2
//            }
//            listsViewModel.sendShoppingItemToDatabase(
//                itemNameInput.text.toString(),
//                qty,
//                cost,
//                whereToGetInput.text.toString(),
//                whenNeededInput.text.toString(),
//                priority
//            )
//        }
//    }
//
//    // SET UP FUNCTIONS //
//    private fun setupAppBar() {
//        //title
//        binding!!.apply {
//            addItemTopAppbar.title = "Add Shopping Item"
//            addItemTopAppbar.setNavigationOnClickListener {
//                // todo: handle navigation click
//            }
//        }
//    }
//}