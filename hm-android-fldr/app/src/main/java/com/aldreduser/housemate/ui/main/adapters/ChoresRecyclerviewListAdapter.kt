package com.aldreduser.housemate.ui.main.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.databinding.ChoresItemLayoutBinding
import com.aldreduser.housemate.ui.main.viewmodels.ListsViewModel
import com.aldreduser.housemate.util.*

class ChoresRecyclerviewListAdapter(
    private val listsViewModel: ListsViewModel,
    private val fragLifecycleOwner: LifecycleOwner,
    private val resources: Resources
) :
    ListAdapter<ChoresItem, ChoresRecyclerviewListAdapter.ChoresViewHolder>(
        ChoresItemDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ChoresViewHolder {
        return ChoresViewHolder.from(listsViewModel, fragLifecycleOwner, resources, parent)
    }

    override fun onBindViewHolder(holderChores: ChoresViewHolder, position: Int) =
        holderChores.bind(getItem(position)!!)

    class ChoresViewHolder private constructor(
        private val listsViewModel: ListsViewModel,
        private val fragLifecycleOwner: LifecycleOwner,
        private val resources: Resources,
        val binding: ChoresItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChoresItem) {
            binding.apply {
                populateUI(item)
                observeHiddenTxt()
                observeEditMode()
                removeItemButton.setOnClickListener {
                    listsViewModel.deleteListItem(ListType.CHORES.toString(), item.name!!)
                }
                choresItIsDone.setOnClickListener {
                    listsViewModel.toggleItemCompletion(
                        ListType.CHORES.toString(),
                        item.name!!,
                        choresItIsDone.isChecked
                    )
                }
                editItemButton.setOnClickListener { listsViewModel.setItemToEdit(item) }
                choresCardview.setOnClickListener { listsViewModel.setItemForSheet(item) }
                executePendingBindings()
            }
        }

        private fun populateUI(item: ChoresItem) {
            binding.apply {
                choresItIsDone.isChecked = item.completed!!
                choresItemName.text = item.name
                when (item.priority) {
                    1 -> choresItemContainer
                        .setBackgroundColor(resources.getColor(R.color.red))
                    2 -> choresItemContainer
                        .setBackgroundColor(resources.getColor(R.color.not_urgent))
                    3 -> choresItemContainer
                        .setBackgroundColor(resources.getColor(R.color.recyclerItem))
                }
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
                        }
                        else -> {
                            removeItemButton.visibility = View.GONE
                            editItemButton.visibility = View.GONE
                        }
                    }
                }
            }
        }

//        private fun volunteerListener(item: ChoresItem) {
//            binding.apply {
//                choresWhoIsDoingItText.setOnKeyListener { _, keyCode, keyEvent ->
//                    if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
//                        listsViewModel.sendItemVolunteerToDb(
//                            ListType.CHORES.toString(),
//                            item.name!!,
//                            choresWhoIsDoingItText.text.toString()
//                        )
//                        true
//                    } else false
//                }
//            }
//        }

//        private fun expandContainer(item: ChoresItem) {
//            binding.apply {
//                choresExpandButton.setOnClickListener {
//                    // If view is GONE change image make view visible
//                    //  else if view is visible change image make view GONE
//                    val expandableContainer = choresExpandableContainerCardview
//                    val imageToContract: Drawable? = ContextCompat.getDrawable(
//                        choresExpandButton.context, R.drawable.ic_expand_less_24
//                    )
//                    val imageToExpand: Drawable? = ContextCompat.getDrawable(
//                        choresExpandButton.context, R.drawable.ic_expand_more_24
//                    )
//                    if (expandableContainer.visibility == View.GONE) {
//                        listsViewModel.itemsExpanded[item.name!!] = true
//                        expandableContainer.visibility = View.VISIBLE
//                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
//                            null, imageToContract, null, null
//                        )
//                    } else if (expandableContainer.visibility == View.VISIBLE) {
//                        listsViewModel.itemsExpanded[item.name!!] = true
//                        expandableContainer.visibility = View.GONE
//                        choresExpandButton.setCompoundDrawablesWithIntrinsicBounds(
//                            null, imageToExpand, null, null
//                        )
//                    }
//                }
//            }
//        }

        companion object {
            fun from(
                listsViewModel: ListsViewModel,
                fragLifecycleOwner: LifecycleOwner,
                resources: Resources,
                parent: ViewGroup
            ): ChoresViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChoresItemLayoutBinding
                    .inflate(layoutInflater, parent, false)
                return ChoresViewHolder(listsViewModel, fragLifecycleOwner, resources, binding)
            }
        }
    }

    class ChoresItemDiffCallback : DiffUtil.ItemCallback<ChoresItem>() {

        override fun areItemsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ChoresItem, newItem: ChoresItem): Boolean {
            return oldItem == newItem
        }
    }
}
