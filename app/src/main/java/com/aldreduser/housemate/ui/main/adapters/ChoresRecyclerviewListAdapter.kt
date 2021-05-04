package com.aldreduser.housemate.ui.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ChoresItem

// This is the list recyclerview adapter
class ChoresRecyclerviewListAdapter(private val choreItems: ArrayList<ChoresItem>):
    RecyclerView.Adapter<ChoresRecyclerviewListAdapter.DataViewHolder>() {

    private val choresItemLayout = R.layout.chores_item_layout

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(choreItem: ChoresItem) {
            //itemView.textViewUserName.text = shoppingItem.name   //todo: change the view name to an appropriate one
            //itemView.textViewUserEmail.text = shoppingItem.quantity.toString()     //todo: change the view name to an appropriate one
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(choresItemLayout, parent, false)
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(choreItems[position])

    override fun getItemCount(): Int = choreItems.size

    // not a default function from the interface
    fun addData(list: List<ChoresItem>) {
        choreItems.addAll(list)
    }
}