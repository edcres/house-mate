package com.aldreduser.housemate.ui.main.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.ShoppingItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.displayDate

/*
ArrayList<ShoppingItem>     was taken out as a parameter, maybe bc of livedata
 */

// No need for 'getItemCount()', dataBinding takes care of it.
// Listen for clicks in the ViewHolder, handle clicks in the ViewModel

// This is the list recyclerview adapter
class ShoppingRecyclerviewListAdapter :
    ListAdapter<ShoppingItem, ShoppingRecyclerviewListAdapter.ShoppingViewHolder>(
        ShoppingItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holderShopping: ShoppingViewHolder, position: Int) =
        holderShopping.bind(getItem(position))

    class ShoppingViewHolder private constructor(val binding: ShoppingItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val listsViewModel = ListsViewModel()

        fun bind(item: ShoppingItem) {
            binding.apply {
                shoppingEntity = item
                // todo: might need to null check these (probably not)
                shoppingItIsDone.isChecked = item.purchased!!
                shoppingItemName.text = item.name
                shoppingItemQty.text = item.quantity.toString()
                shoppingWhenNeededDoneText.text = displayDate(item.neededBy!!)
                shoppingWhereText.text = item.purchaseLocation
                shoppingCostText.text = item.cost.toString()
                shoppingPriorityText.text = item.priority.toString()
                shoppingAddedByText.text = item.addedBy
                shoppingWhoIsGettingItText.setText(item.volunteer)

                removeItemButton.setOnClickListener {
                    listsViewModel.deleteShoppingListItem(item.name!!)
                }
                shoppingItIsDone.setOnClickListener {
                    listsViewModel.toggleShoppingCompletion(item.name!!, shoppingItIsDone.isChecked)
                }
                shoppingExpandButton.setOnClickListener {
                    // If view is invisible change image make view visible
                    // else if view is visible change image make view invisible
                    val expandableContainer = shoppingExpandableContainerCardview
                    val imageToContract: Drawable? = ContextCompat.getDrawable(
                        shoppingExpandButton.context, R.drawable.ic_expand_less_24
                    )
                    val imageToExpand: Drawable? = ContextCompat.getDrawable(
                        shoppingExpandButton.context, R.drawable.ic_expand_more_24
                    )
                    if (expandableContainer.visibility == View.INVISIBLE) {
                        expandableContainer.visibility = View.VISIBLE
                        shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            imageToContract, null, null, null
                        )
                    } else if (expandableContainer.visibility == View.VISIBLE) {
                        expandableContainer.visibility = View.INVISIBLE
                        shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            imageToExpand, null, null, null
                        )
                    }
                }
                executePendingBindings()    // idk what this is for
            }
        }

        companion object {
            fun from(parent: ViewGroup): ShoppingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ShoppingViewHolder(binding)
            }
        }
    }
}

// Compares the old to new recycler views and looks for changes
class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {

    override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
        return oldItem == newItem
    }
}
