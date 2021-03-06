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
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.ChoresItemLayoutBinding

// This is the list recyclerview adapter
class ChoresRecyclerviewListAdapter() :
    ListAdapter<ChoresItem, ChoresRecyclerviewListAdapter.ViewHolder>(ChoresItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ChoresItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChoresItem) {
            binding.choresEntity = item

            binding.choresItIsDone.setOnClickListener {
                // todo: handle when box is checked
            }

            binding.choresExpandButton.setOnClickListener {
                // If view is invisible change image make view visible
                // else if view is visible change image make view invisible
                val expandableContainer = binding.choresExpandableContainerCardview
                val imageToContract: Drawable? = ContextCompat.getDrawable(
                    binding.choresExpandButton.context, R.drawable.ic_expand_less_24)
                val imageToExpand: Drawable? = ContextCompat.getDrawable(
                    binding.choresExpandButton.context, R.drawable.ic_expand_more_24)
                if (expandableContainer.visibility == View.INVISIBLE) {
                    expandableContainer.visibility = View.VISIBLE
                    binding.choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                        imageToContract, null, null, null
                    )
                } else if (expandableContainer.visibility == View.VISIBLE) {
                    expandableContainer.visibility = View.INVISIBLE
                    binding.choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                        imageToExpand, null, null, null)
                }
            }

            binding.executePendingBindings()    // idk what this is for
        }

        companion object {
            fun from(parent: ViewGroup): ChoresRecyclerviewListAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChoresItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

// Compares the old to new recycler views and looks for changes
class ChoresItemDiffCallback : DiffUtil.ItemCallback<ChoresItem>() {

    override fun areItemsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
        return oldItem == newItem
    }
}
