package com.aldreduser.housemate.util

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.ui.main.adapters.ChoresRecyclerviewListAdapter
import com.aldreduser.housemate.ui.main.adapters.ShoppingRecyclerviewListAdapter

class DragManageAdapter(
    shoppingAdapter: ShoppingRecyclerviewListAdapter?,
    choresAdapter: ChoresRecyclerviewListAdapter?,
    context: Context,
    dragDirs: Int,
    swipeDirs: Int
) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
    var adapter = shoppingAdapter ?: choresAdapter
    override fun onItemMove(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
    }

}