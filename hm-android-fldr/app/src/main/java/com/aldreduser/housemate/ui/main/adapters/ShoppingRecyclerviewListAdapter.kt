package com.aldreduser.housemate.ui.main.adapters

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.databinding.ShoppingItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.*

private const val TAG = "ShopListAdapter__TAG"

class ShoppingRecyclerviewListAdapter(
    private val listsViewModel: ListsViewModel,
    private val fragLifecycleOwner: LifecycleOwner,
    private val resources: Resources
) :
    ListAdapter<ShoppingItem, ShoppingRecyclerviewListAdapter.ShoppingViewHolder>(
        ShoppingItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder.from(listsViewModel, fragLifecycleOwner, resources, parent)
    }

    override fun onBindViewHolder(holderShopping: ShoppingViewHolder, position: Int) =
        holderShopping.bind(getItem(position))

    class ShoppingViewHolder private constructor(
        private val listsViewModel: ListsViewModel,
        private val fragLifecycleOwner: LifecycleOwner,
        private val resources: Resources,
        val binding: ShoppingItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(shoppingItem: ShoppingItem) {
            binding.apply {
                populateUI(shoppingItem)
                observeHiddenTxt()
                observeEditMode()
                removeItemButton.setOnClickListener {
                    listsViewModel.deleteListItem(ListType.SHOPPING.toString(), shoppingItem.name!!)
                }
                shoppingItIsDone.setOnClickListener {
                    listsViewModel.toggleItemCompletion(
                        ListType.SHOPPING.toString(),
                        shoppingItem.name!!,
                        shoppingItIsDone.isChecked
                    )
                }
                editItemButton.setOnClickListener { listsViewModel.setItemToEdit(shoppingItem) }
                shoppingCardview.setOnClickListener { listsViewModel.setItemForSheet(shoppingItem) }
                executePendingBindings()
            }
        }

        private fun populateUI(item: ShoppingItem) {
            binding.apply {
                shoppingItIsDone.isChecked = item.completed!!
                shoppingItemName.text = item.name
                shoppingItemQty.text = presentItemQty(item.quantity!!)
                when (item.priority) {
                    1 -> shoppingItemContainer
                        .setBackgroundColor(resources.getColor(R.color.red))
                    2 -> shoppingItemContainer
                        .setBackgroundColor(resources.getColor(R.color.not_urgent))
                    3 -> shoppingItemContainer
                        .setBackgroundColor(resources.getColor(R.color.recyclerItem))
                }
            }
        }

        private fun observeHiddenTxt() {
            // Covers up a bug rendering updates in the list.
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
                        }
                        else -> {
                            removeItemButton.visibility = View.GONE
                            editItemButton.visibility = View.GONE
                        }
                    }
                }
            }
        }

        companion object {
            fun from(
                listsViewModel: ListsViewModel,
                fragLifecycleOwner: LifecycleOwner,
                resources: Resources,
                parent: ViewGroup
            ): ShoppingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingItemLayoutBinding
                    .inflate(layoutInflater, parent, false)
                return ShoppingViewHolder(listsViewModel, fragLifecycleOwner, resources, binding)
            }
        }
    }

    class ShoppingItemDiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }
}
