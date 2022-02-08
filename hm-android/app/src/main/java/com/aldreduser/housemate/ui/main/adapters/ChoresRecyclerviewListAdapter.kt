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
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.ChoresItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.displayAddedBy
import com.aldreduser.housemate.util.displayDate
import com.aldreduser.housemate.util.displayDifficulty
import com.aldreduser.housemate.util.displayPriority

class ChoresRecyclerviewListAdapter(
    val listsViewModel: ListsViewModel,
    private val fragLifecycleOwner: LifecycleOwner
) :
    ListAdapter<ChoresItem, ChoresRecyclerviewListAdapter.ChoresViewHolder>(
        ChoresItemDiffCallback()
    ) {

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
                choresItIsDone.isChecked = item.completed!!
                choresItemName.text = item.name
                choresWhenNeededDoneText.text = if(item.neededBy!!.isNotEmpty()) {
                    displayDate(item.neededBy)
                } else {choresWhenNeededDoneText.visibility = View.GONE; ""}
                choresDifficulty.text = displayDifficulty(item.difficulty!!)
                choresPriorityText.text = displayPriority(item.priority!!)
                choresAddedByText.text = displayAddedBy(item.addedBy!!)
                choresWhoIsDoingItText.setText(item.volunteer)

                listsViewModel.menuEditIsOn.observe(fragLifecycleOwner) { result ->
                    when (result) {
                        true -> {
                            removeItemButton.visibility = View.VISIBLE
                            editItemButton.visibility = View.VISIBLE
                            choresExpandButton.visibility = View.GONE
                        }
                        else -> {
                            choresExpandButton.visibility = View.VISIBLE
                            removeItemButton.visibility = View.GONE
                            editItemButton.visibility = View.GONE
                        }
                    }
                }

                if (listsViewModel.itemsExpanded[item.name!!] == true) {
                    choresExpandableContainerCardview.visibility = View.VISIBLE
                }
                choresWhoIsDoingItText.setOnKeyListener { _, keyCode, keyEvent ->
                    if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                        listsViewModel.sendItemVolunteerToDb(
                            listsViewModel.listTypes[1],
                            item.name,
                            choresWhoIsDoingItText.text.toString()
                        )
                        true
                    } else false
                }
                removeItemButton.setOnClickListener {
                    listsViewModel.deleteListItem(listsViewModel.listTypes[1], item.name)
                }
                choresItIsDone.setOnClickListener {
                    listsViewModel.toggleItemCompletion(
                        listsViewModel.listTypes[1],
                        item.name,
                        choresItIsDone.isChecked
                    )
                }
                choresExpandButton.setOnClickListener {
                    // If view is GONE change image make view visible
                    //  else if view is visible change image make view GONE
                    val expandableContainer = choresExpandableContainerCardview
                    val imageToContract: Drawable? = ContextCompat.getDrawable(
                        choresExpandButton.context, R.drawable.ic_expand_less_24
                    )
                    val imageToExpand: Drawable? = ContextCompat.getDrawable(
                        choresExpandButton.context, R.drawable.ic_expand_more_24
                    )
                    if (expandableContainer.visibility == View.GONE) {
                        listsViewModel.itemsExpanded[item.name] = true
                        expandableContainer.visibility = View.VISIBLE
                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            null, imageToContract, null, null
                        )
                    } else if (expandableContainer.visibility == View.VISIBLE) {
                        listsViewModel.itemsExpanded[item.name] = true
                        expandableContainer.visibility = View.GONE
                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
                            null, imageToExpand, null, null
                        )
                    }
                }
                editItemButton.setOnClickListener {
                    listsViewModel.setItemToEdit(item)
                }
                executePendingBindings()
            }
        }

        companion object {
            fun from(
                listsViewModel: ListsViewModel,
                fragLifecycleOwner: LifecycleOwner,
                parent: ViewGroup
            ): ChoresViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChoresItemLayoutBinding
                    .inflate(layoutInflater, parent, false)
                return ChoresViewHolder(listsViewModel, fragLifecycleOwner, binding)
            }
        }
    }
}

class ChoresItemDiffCallback : DiffUtil.ItemCallback<ChoresItem>() {

    override fun areItemsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
        return oldItem == newItem
    }
}
