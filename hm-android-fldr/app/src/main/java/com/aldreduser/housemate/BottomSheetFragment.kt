package com.aldreduser.housemate

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.FragmentBottomSheetBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*
import java.nio.charset.StandardCharsets

/** todo:
 * (x)acty
 * (x)sheet frag
 *
 * shopping:
 * (x)- normal frag
 * (x)- item click (adapter)
 * (x)- vm var
 * (x)- frag listener
 *
 * do the logic in the sheet fragment
 *
 * chores:
 * - normal frag
 * - item click (adapter)
 * - vm var
 * - frag listener
 *
 * sheet frag logic
 *
 * take out the container
 */

private const val TAG = "ModalBottomSheet__TAG"

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var dialog: BottomSheetDialog
    private var binding: FragmentBottomSheetBinding? = null
    private val listsViewModel: ListsViewModel by activityViewModels()
    private lateinit var listType: ListType
    private lateinit var itemToView: Any

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        if(listType == ListType.SHOPPING) {
            startShoppingView(itemToView as ShoppingItem)
        } else if(listType == ListType.CHORES) {
            startChoresView(itemToView as ChoresItem)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        dialog.dismiss()
    }

    private fun startShoppingView(item: ShoppingItem) {
        binding?.apply {
            sheetQtyTxt.text = presentItemQty(item.quantity!!)
            sheetTitleTxt.text = item.name
            shoppingWhenNeededDoneText.text = if (item.neededBy!!.isNotEmpty()) {
                displayDate(item.neededBy)
            } else {
                shoppingWhenNeededDoneText.visibility = View.GONE; ""
            }
            shoppingWhereText.text = if (item.purchaseLocation!!.isNotEmpty()) {
                item.purchaseLocation
            } else {
                shoppingWhereText.visibility = View.GONE; ""
            }
            shoppingCostText.text = if (item.cost!! != 0.0) {
                displayCost(item.cost)
            } else {
                shoppingCostText.visibility = View.GONE; ""
            }
            shoppingPriorityText.text = displayPriority(item.priority!!)
            shoppingAddedByText.text = displayAddedBy(item.addedBy!!)
            shoppingWhoIsGettingItText.setText(item.volunteer)
        }
    }

    private fun startChoresView(item: ChoresItem) {
        // todo:
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