package com.aldreduser.housemate.ui.main.adapters

import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.ShoppingItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.*

class ShoppingRecyclerviewListAdapter(
    private val listsViewModel: ListsViewModel,
    private val fragLifecycleOwner: LifecycleOwner
) :
    ListAdapter<ShoppingItem, ShoppingRecyclerviewListAdapter.ShoppingViewHolder>(
        ShoppingItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder.from(listsViewModel, fragLifecycleOwner, parent)
    }

    override fun onBindViewHolder(holderShopping: ShoppingViewHolder, position: Int) =
        holderShopping.bind(getItem(position))

    class ShoppingViewHolder private constructor(
        private val listsViewModel: ListsViewModel,
        private val fragLifecycleOwner: LifecycleOwner,
        val binding: ShoppingItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem) {
            binding.apply {
                populateUI(item)
                observeHiddenTxt()
                observeEditMode()

                if (listsViewModel.itemsExpanded[item.name!!] == true) {
                    shoppingExpandableContainerCardview.visibility = View.VISIBLE
                }
                volunteerListener(item)
                removeItemButton.setOnClickListener {
                    listsViewModel.deleteListItem(listsViewModel.listTypes[0], item.name)
                }
                shoppingItIsDone.setOnClickListener {
                    listsViewModel.toggleItemCompletion(
                        listsViewModel.listTypes[0],
                        item.name,
                        shoppingItIsDone.isChecked
                    )
                }
                expandContainer(item)
                editItemButton.setOnClickListener {
                    listsViewModel.setItemToEdit(item)
                }
                executePendingBindings()
            }
        }

        private fun populateUI(item: ShoppingItem) {
            binding.apply {
                shoppingItIsDone.isChecked = item.completed!!
                shoppingItemName.text = item.name
                shoppingItemQty.text = presentItemQty(item.quantity!!)
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

        private fun observeHiddenTxt() {
            listsViewModel.hiddenTxt.observe(fragLifecycleOwner) {
                binding.apply {
                    when (dummyTxt.text.toString()) {
                        "a" -> dummyTxt.text = "a"
                        "b" -> dummyTxt.text = "b"
                        else -> dummyTxt.text = "a"
                    }
                }
            }
        }

        private fun observeEditMode() {
            binding.apply {
                listsViewModel.menuEditIsOn.observe(fragLifecycleOwner) { result ->
                    when (result) {
                        true -> {
                            removeItemButton.visibility = View.VISIBLE
                            editItemButton.visibility = View.VISIBLE
                            shoppingExpandButton.visibility = View.GONE
                        }
                        else -> {
                            shoppingExpandButton.visibility = View.VISIBLE
                            removeItemButton.visibility = View.GONE
                            editItemButton.visibility = View.GONE
                        }
                    }
                }
            }
        }

        private fun volunteerListener(item: ShoppingItem) {
            binding.apply {
                shoppingWhoIsGettingItText.setOnKeyListener { _, keyCode, keyEvent ->
                    if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                        listsViewModel.sendItemVolunteerToDb(
                            listsViewModel.listTypes[0],
                            item.name!!,
                            shoppingWhoIsGettingItText.text.toString()
                        )
                        true
                    } else false
                }
            }
        }

        private fun expandContainer(item: ShoppingItem) {
            binding.apply {
                shoppingExpandButton.setOnClickListener {
                    // If view is GONE change image make view visible
                    //   else if view is visible change image make view GONE
                    val expandableContainer = shoppingExpandableContainerCardview
                    val imageToContract: Drawable? = ContextCompat.getDrawable(
                        shoppingExpandButton.context, R.drawable.ic_expand_less_24
                    )
                    val imageToExpand: Drawable? = ContextCompat.getDrawable(
                        shoppingExpandButton.context, R.drawable.ic_expand_more_24
                    )
                    if (expandableContainer.visibility == View.GONE) {
                        listsViewModel.itemsExpanded[item.name!!] = true
                        expandableContainer.visibility = View.VISIBLE
                        shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            null, imageToContract, null, null
                        )
                    } else if (expandableContainer.visibility == View.VISIBLE) {
                        listsViewModel.itemsExpanded[item.name!!] = false
                        expandableContainer.visibility = View.GONE
                        shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            null, imageToExpand, null, null
                        )
                    }
                }
            }
        }

        companion object {
            fun from(
                listsViewModel: ListsViewModel,
                fragLifecycleOwner: LifecycleOwner,
                parent: ViewGroup
            ): ShoppingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingItemLayoutBinding
                    .inflate(layoutInflater, parent, false)
                return ShoppingViewHolder(listsViewModel, fragLifecycleOwner, binding)
            }
        }
    }

    class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
}
