package com.aldreduser.housemate.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

/*
Chores properties:
-id
-chore name
-addedByWho
-is it done?

-difficultly 1-3

-needed by (this date)
-who volunteers to do it
-priority 1-3 (compared to others shopping items)
-notes
*/
@IgnoreExtraProperties
data class ChoresItem (
    val id: Long? = 0,
    val name: String? = "",
    val addedBy: String? = "",
    val completed: Boolean? = false,

    // Not necessary and not used in shoppingItem
    val difficulty: Int? = 1,

    // Not necessary and used in shoppingItem
    val neededBy: String? = "",  //date
    val volunteer: String? = "", //who's buying it
    val priority: Int? = 2,
    val notes: String = ""
)