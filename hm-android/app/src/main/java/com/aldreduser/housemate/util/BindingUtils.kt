package com.aldreduser.housemate.util

import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem

// SHOPPING //
@BindingAdapter("doCheck")
fun CheckBox.setDoCheck(item: ShoppingItem) {
    item?.let {
        isChecked = item.purchased
    }
}

@BindingAdapter("quantity")
fun TextView.setQuantity(item: ShoppingItem) {
    item?.let {
        text = item.quantity.toString()
    }
}

@BindingAdapter("name")
fun TextView.setName(item: ShoppingItem) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("date")
fun TextView.setDate(item: ShoppingItem) {
    item?.let {
        text = displayDate(item.neededBy)
    }
}

@BindingAdapter("purchaseLocation")
fun TextView.setPurchaseLocation(item: ShoppingItem) {
    item?.let {
        text = item.purchaseLocation
    }
}

@BindingAdapter("price")
fun TextView.setPrice(item: ShoppingItem) {
    item?.let {
        text = displayCost(item.cost)
    }
}

@BindingAdapter("volunteer")
fun TextView.setVolunteer(item: ShoppingItem) {
    item?.let {
        text = item.volunteer
    }
}

@BindingAdapter("priority")
fun TextView.setPriority(item: ShoppingItem) {
    item?.let {
        text = displayPriority(item.priority)
    }
}

@BindingAdapter("addedBy")
fun TextView.setAddedBy(item: ShoppingItem) {
    item?.let {
        text = displayAddedBy(item.addedBy)
    }
}

// CHORES //
@BindingAdapter("doCheck")
fun CheckBox.setDoCheck(item: ChoresItem) {
    item?.let {
        isChecked = item.completed
    }
}

@BindingAdapter("name")
fun TextView.setName(item: ChoresItem) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("date")
fun TextView.setDate(item: ChoresItem) {
    item?.let {
        text = displayDate(item.neededBy)
    }
}

@BindingAdapter("difficulty")
fun TextView.setDifficulty(item: ChoresItem) {
    item?.let {
        text = displayDifficulty(item.difficulty)
    }
}

@BindingAdapter("volunteer")
fun TextView.setVolunteer(item: ChoresItem) {
    item?.let {
        text = item.volunteer
    }
}

@BindingAdapter("priority")
fun TextView.setPriority(item: ChoresItem) {
    item?.let {
        text = displayPriority(item.priority)
    }
}

@BindingAdapter("addedBy")
fun TextView.setAddedBy(item: ChoresItem) {
    item?.let {
        text = displayAddedBy(item.addedBy)
    }
}
