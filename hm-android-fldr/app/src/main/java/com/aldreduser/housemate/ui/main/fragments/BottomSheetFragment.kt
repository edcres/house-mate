package com.aldreduser.housemate.ui.main.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.FragmentBottomSheetBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val TAG = "ModalBottomSheet__TAG"

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var dialog: BottomSheetDialog
    private var binding: FragmentBottomSheetBinding? = null
    private val vm: ListsViewModel by activityViewModels()
    private lateinit var listType: ListType
    private lateinit var itemToView: Any

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            shopSheetEditBtn.setOnClickListener {
                // Navigate to edit fragment
                dialog.dismiss()
                vm.setItemToEdit(vm.itemForSheet.value)
            }
            choresSheetEditBtn.setOnClickListener {
                // Navigate to edit fragment
                dialog.dismiss()
                vm.setItemToEdit(vm.itemForSheet.value)
            }
        }
        if (listType == ListType.SHOPPING) {
            startShoppingView(itemToView as ShoppingItem)
        } else if (listType == ListType.CHORES) {
            startChoresView(itemToView as ChoresItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        dialog.dismiss()
        vm.setItemForSheet(null)
    }

    private fun startShoppingView(item: ShoppingItem) {
        binding?.apply {
            shoppingExpandableContainer.visibility = View.VISIBLE
            setColorForSheet(bottomSheetFrame, item.priority)
            shoppingSheetQtyTxt.text = presentItemQty(item.quantity!!)
            shoppingSheetTitleTxt.text = item.name
            shoppingWhenNeededDoneText.text = if (item.neededBy!!.isNotEmpty()) {
                displayDate(item.neededBy)
            } else {
                shopDateImg.visibility = View.GONE
                shoppingWhenNeededDoneText.visibility = View.GONE; ""
            }
            shoppingWhereText.text = if (item.purchaseLocation!!.isNotEmpty()) {
                item.purchaseLocation
            } else {
                shopWhereImg.visibility = View.GONE
                shoppingWhereText.visibility = View.GONE; ""
            }
            shoppingCostText.text = if (item.cost!! != 0.0) {
                displayCost(item.cost)
            } else {
                shopCostImg.visibility = View.GONE
                shoppingCostText.visibility = View.GONE; ""
            }
            shoppingPriorityText.text = displayPriority(item.priority!!)
            shoppingAddedByText.text = displayAddedBy(item.addedBy!!)
            shoppingWhoIsGettingItText.setText(item.volunteer)
        }
        shoppingVolunteerListener(item)
    }

    private fun startChoresView(item: ChoresItem) {
        binding?.apply {
            choresExpandableContainer.visibility = View.VISIBLE
            choreSheetTitleTxt.text = item.name
            choresWhenNeededDoneText.text = if (item.neededBy!!.isNotEmpty()) {
                displayDate(item.neededBy)
            } else {
                choresDateImg.visibility = View.GONE
                choresWhenNeededDoneText.visibility = View.GONE; ""
            }
            choresDifficulty.text = displayDifficulty(item.difficulty!!)
            choresPriorityText.text = displayPriority(item.priority!!)
            choresAddedByText.text = displayAddedBy(item.addedBy!!)
            choresWhoIsDoingItText.setText(item.volunteer)
        }
        choresVolunteerListener(item)
    }

    private fun shoppingVolunteerListener(item: ShoppingItem) {
        // Send volunteer input to db.
        binding?.apply {
            shoppingWhoIsGettingItText.setOnKeyListener { _, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    vm.sendItemVolunteerToDb(
                        ListType.SHOPPING.toString(),
                        item.name!!,
                        shoppingWhoIsGettingItText.text.toString()
                    )
                    true
                } else false
            }
        }
    }

    private fun choresVolunteerListener(item: ChoresItem) {
        // Send volunteer input to db.
        binding?.apply {
            choresWhoIsDoingItText.setOnKeyListener { _, keyCode, keyEvent ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                    vm.sendItemVolunteerToDb(
                        ListType.CHORES.toString(),
                        item.name!!,
                        choresWhoIsDoingItText.text.toString()
                    )
                    true
                } else false
            }
        }
    }

    private fun setColorForSheet(bottomSheetFrame: FrameLayout, priority: Int?) {
        if (priority != null) when (priority) {
            1 -> bottomSheetFrame.setBackgroundResource(R.drawable.bottom_sheet_background_red)
            2 -> bottomSheetFrame.setBackgroundResource(R.drawable.bottom_sheet_background_yellow)
            3 -> bottomSheetFrame.setBackgroundResource(R.drawable.bottom_sheet_background)
        }
    }

    companion object {
        fun newInstance(itemSent: Any, listTypeSent: ListType) = BottomSheetFragment().apply {
            arguments = Bundle().apply {
                itemToView = itemSent
                listType = listTypeSent
            }
        }
    }
}