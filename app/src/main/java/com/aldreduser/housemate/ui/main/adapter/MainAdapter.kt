package com.aldreduser.housemate.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aldreduser.housemate.R
import com.aldreduser.housemate.data.model.ShoppingItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.shopping_item_layout.view.*

// This is the recyclerview adapter
class MainAdapter(private val shoppingItems: ArrayList<ShoppingItem>):
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(shoppingItem: ShoppingItem) {
            itemView.textViewUserName.text = shoppingItem.name   //todo: change the view name to an appropriate one
            itemView.textViewUserEmail.text = shoppingItem.quantity.toString()     //todo: change the view name to an appropriate one

            // todo: the image in the recyclerview goes here (it's just an edit button, the same in every item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.shopping_item_layout, parent, false
        )
    )

    override fun getItemCount(): Int = shoppingItems.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(shoppingItems[position])

    // not a default function from the interface
    fun addData(list: List<ShoppingItem>) {
        shoppingItems.addAll(list)
    }
}