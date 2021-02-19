package com.aldreduser.housemate.data.model

import com.google.gson.annotations.SerializedName

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
*/

data class ChoresItem (
    // Necessary
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("addedBy") // idk the format for the value, i just did camelCase
    val addedBy: String = "",
    @SerializedName("done")
    val purchased: Boolean = false,

    // Not necessary and not used in shoppingItem
    @SerializedName("difficulty") // 1 to 3
    val difficulty: Int = 1,

    // Not necessary and used in shoppingItem
    @SerializedName("neededBy")
    val neededBy: String = "",  //date
    @SerializedName("volunteer")
    val volunteer: String = "", //who's buying it
    @SerializedName("priority")
    val priority: Int = 2
)