package com.aldreduser.housemate.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

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
-notes
*/
@IgnoreExtraProperties
data class ShoppingItem (
    val id: Long? = 0,
    val name: String? = "",
    val quantity: Double? = 0.0,
    val addedBy: String? = "",
    val completed: Boolean? = false,

    // Not necessary and not used in Chores
    val cost: Double? = 0.0,
    val purchaseLocation: String? = "",

    // Not necessary and used in chores
    val neededBy: String? = "",  // date
    val volunteer: String? = "",
    val priority: Int? = 2,      // 1 = urgent, 3 = eventually
    val notes: String = ""
)
