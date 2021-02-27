package com.aldreduser.housemate.data.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem

@Database (entities = arrayOf(
    ShoppingItem::class,
    ChoresItem::class),
    version = 1,
    exportSchema = false)
public abstract class ListsRoomDatabase : RoomDatabase() {

}