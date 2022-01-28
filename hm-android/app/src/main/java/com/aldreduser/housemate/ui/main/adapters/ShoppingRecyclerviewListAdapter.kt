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

/*
ArrayList<ShoppingItem>     was taken out as a parameter, maybe bc of livedata
 */

// No need for 'getItemCount()', dataBinding takes care of it.
// Listen for clicks in the ViewHolder, handle clicks in the ViewModel

// This is the list recyclerview adapter
class ShoppingRecyclerviewListAdapter :
    ListAdapter<ShoppingItem, ShoppingRecyclerviewListAdapter.ViewHolder>(ShoppingItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position)!!)

    class ViewHolder private constructor(val binding: ShoppingItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem) {
            binding.shoppingEntity = item

            binding.shoppingItIsDone.setOnClickListener {
                // todo: handle when box is checked
            }
            binding.shoppingExpandButton.setOnClickListener {
                // If view is invisible change image make view visible
                // else if view is visible change image make view invisible
                val expandableContainer = binding.shoppingExpandableContainerCardview
                val imageToContract: Drawable? = ContextCompat.getDrawable(
                    binding.shoppingExpandButton.context, R.drawable.ic_expand_less_24
                )
                val imageToExpand: Drawable? = ContextCompat.getDrawable(
                    binding.shoppingExpandButton.context, R.drawable.ic_expand_more_24
                )
                if (expandableContainer.visibility == View.INVISIBLE) {
                    expandableContainer.visibility = View.VISIBLE
                    binding.shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                        imageToContract, null, null, null
                    )
                } else if (expandableContainer.visibility == View.VISIBLE) {
                    expandableContainer.visibility = View.INVISIBLE
                    binding.shoppingExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                        imageToExpand, null, null, null
                    )
                }
            }

            binding.executePendingBindings()    // idk what this is for
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ShoppingItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
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
