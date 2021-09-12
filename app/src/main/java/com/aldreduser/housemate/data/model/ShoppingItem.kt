package com.aldreduser.housemate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

data class ShoppingItem (
    // Necessary
    val id: Long? = 0,
    val name: String? = "",
    val quantity: Double? = 0.0,
    val addedBy: String? = "",
    val purchased: Boolean? = false,

    // Not necessary and not used in Chores
    val cost: Double? = 0.0,
    val purchaseLocation: String? = "",

    // Not necessary and used in chores
    val neededBy: String? = "",  //date
    val volunteer: String? = "", //who's buying it
    val priority: Int? = 2
)
