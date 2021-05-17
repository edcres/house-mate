package com.aldreduser.housemate.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

@Entity(tableName = "chore_item_table")
data class ChoresItem (
    // Necessary
    @PrimaryKey(autoGenerate = true)    // 'autogenerate' increments by 1 from each list item added
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "addedBy") // idk the format for the value, i just did camelCase
    val addedBy: String = "",
    @ColumnInfo(name = "completed")
    val purchased: Boolean = false,

    // Not necessary and not used in shoppingItem
    @ColumnInfo(name = "difficulty") // 1 to 3
    val difficulty: Int = 1,

    // Not necessary and used in shoppingItem
    @ColumnInfo(name = "neededBy")
    val neededBy: String = "",  //date
    @ColumnInfo(name = "volunteer")
    val volunteer: String = "", //who's buying it
    @ColumnInfo(name = "priority")
    val priority: Int = 2
)