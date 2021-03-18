package com.aldreduser.housemate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

// have the properties well organized bc it might make the recyclerview and storage easier.

/*
Shopping items properties:
-id
-item name
-quantity
-addedByWho
-purchased?

-cost
-where it can be bought

-needed by (this date)
-who volunteers to buying it
-priority 1-3 (compared to others shopping items)
*/

@Entity(tableName = "shopping_item_table")
data class ShoppingItem (
    // Necessary
    @PrimaryKey(autoGenerate = true)    // 'autogenerate' increments by 1 from each list item added
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "quantity") // Chores didn't have this Qty property, but i put it here
    val quantity: Double = 0.0,
    @ColumnInfo(name = "addedBy") // idk the format for the value, i just did camelCase
    val addedBy: String = "",
    @ColumnInfo(name = "purchased")
    val purchased: Boolean = false,

    // Not necessary and not used in Chores
    @ColumnInfo(name = "cost")
    val cost: Double = 0.0,
    @ColumnInfo(name = "purchaseLocation")
    val purchaseLocation: String = "",

    // Not necessary and used in chores
    @ColumnInfo(name = "neededBy")
    val neededBy: String = "",  //date
    @ColumnInfo(name = "volunteer")
    val volunteer: String = "", //who's buying it
    @ColumnInfo(name = "priority")
    val priority: Int = 2
)
