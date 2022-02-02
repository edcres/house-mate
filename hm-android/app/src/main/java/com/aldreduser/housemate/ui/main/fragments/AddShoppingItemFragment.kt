package com.aldreduser.housemate.ui.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aldreduser.housemate.databinding.FragmentAddShoppingItemBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.necessaryAreFilled

class AddShoppingItemFragment : Fragment() {

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
        binding!!.apply {
            lifecycleOwner = viewLifecycleOwner
            fabAddItem.setOnClickListener {
                val necessaryAreFilled = necessaryAreFilled(
                    itemNameInput.text.toString(),
                    itemQuantityInput.text.toString()
                )
                if (necessaryAreFilled) {
                    addItem()
                }
            }
        }
        setupAppBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        Log.i(fragmentTag, "onDestroy: AddShoppingItemFragment")
    }

    // CLICK HANDLERS //
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
            listsViewModel.sendShoppingItemToDatabase(
                itemNameInput.text.toString(),
                qty,
                cost,
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
}