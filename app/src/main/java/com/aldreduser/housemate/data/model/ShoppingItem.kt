package com.aldreduser.housemate.data.model

import com.google.gson.annotations.SerializedName

// have the properties well organized bc it might make the recyclerview and storage easier.

data class ShoppingItem (
    // Necessary
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("quantity") // Chores didn't have this Qty property, but i put it here
    val quantity: Double = 0.0,
    @SerializedName("addedBy") // idk the format for the value, i just did camelCase
    val addedBy: String = "",
    @SerializedName("purchased")
    val purchased: Boolean = false,

    // Not necessary and not used in Chores
    @SerializedName("cost")
    val cost: Double = 0.0,
    @SerializedName("purchaseLocation")
    val purchaseLocation: String = "",

    // Not necessary and used in chores
    @SerializedName("neededBy")
    val neededBy: String = "",  //date
    @SerializedName("volunteer")
    val volunteer: String = "", //who's buying it
    @SerializedName("priority")
    val priority: Int
)

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
_________________________________________________________________________________

Chores properties:
-id
-chore name
-addedByWho
-is it done?

-difficultly 1-3

-needed by (this date)
-who volunteers to do it
-priority 1-3 (compared to others shopping items)
 */
