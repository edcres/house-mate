package com.aldreduser.housemate.ui.main.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.ChoresItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.displayAddedBy
import com.aldreduser.housemate.util.displayDate
import com.aldreduser.housemate.util.displayDifficulty
import com.aldreduser.housemate.util.displayPriority

// This is the chores list recyclerview adapter
class ChoresRecyclerviewListAdapter(
    val listsViewModel: ListsViewModel,
    val fragLifecycleOwner: LifecycleOwner
) :
    ListAdapter<ChoresItem, ChoresRecyclerviewListAdapter.ChoresViewHolder>(ChoresItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ChoresViewHolder {
        return ChoresViewHolder.from(listsViewModel, fragLifecycleOwner,parent)
    }

    override fun onBindViewHolder(holderChores: ChoresViewHolder, position: Int) =
        holderChores.bind(getItem(position)!!)

    class ChoresViewHolder private constructor(
        val listsViewModel: ListsViewModel,
        private val fragLifecycleOwner: LifecycleOwner,
        val binding: ChoresItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChoresItem) {
            binding.apply {
                choresEntity = item
                // todo: might need to null check these (probably not)
                choresItIsDone.isChecked = item.completed!!
                choresItemName.text = item.name
                choresWhenNeededDoneText.text = displayDate(displayDate(item.neededBy!!))
                choresDifficulty.text = displayDifficulty(item.difficulty!!)
                choresPriorityText.text = displayPriority(item.priority!!)
                choresAddedByText.text = displayAddedBy(item.addedBy!!)
                choresWhoIsDoingItText.setText(item.volunteer)

                listsViewModel.menuEditIsOn.observe(fragLifecycleOwner, Observer { result ->
                    when (result) {
                        true -> {
                            removeItemButton.visibility = View.VISIBLE
                            choresExpandButton.visibility = View.GONE
                            choresExpandableContainer.visibility = View.GONE
                        }
                        else -> {
                            choresExpandButton.visibility = View.VISIBLE
                            removeItemButton.visibility = View.GONE
                        }
                    }
                })

                choresWhoIsDoingItText.doAfterTextChanged {
                    listsViewModel.choreVolunteersList[item.name!!] = it.toString()
                    listsViewModel.choreVolunteerWasChanged = true
                }
                removeItemButton.setOnClickListener {
                    listsViewModel.deleteChoresListItem(item.name!!)
                }
                choresItIsDone.setOnClickListener {
                    listsViewModel.toggleChoreCompletion(item.name!!, choresItIsDone.isChecked)
                }
                choresExpandButton.setOnClickListener {
                    // If view is GONE change image make view visible
                    // else if view is visible change image make view GONE
                    val expandableContainer = choresExpandableContainerCardview
                    val imageToContract: Drawable? = ContextCompat.getDrawable(
                        choresExpandButton.context, R.drawable.ic_expand_less_24
                    )
                    val imageToExpand: Drawable? = ContextCompat.getDrawable(
                        choresExpandButton.context, R.drawable.ic_expand_more_24
                    )
                    if (expandableContainer.visibility == View.GONE) {
                        expandableContainer.visibility = View.VISIBLE
                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            imageToContract, null, null, null
                        )
                    } else if (expandableContainer.visibility == View.VISIBLE) {
                        expandableContainer.visibility = View.GONE
                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            imageToExpand, null, null, null
                        )
                    }
                }
                executePendingBindings()    // idk what this is for
            }
        }

        companion object {
            fun from(
                listsViewModel: ListsViewModel,
                fragLifecycleOwner: LifecycleOwner,
                parent: ViewGroup
            ): ChoresViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChoresItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ChoresViewHolder(listsViewModel, fragLifecycleOwner, binding)
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
