package com.aldreduser.housemate.data.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem

@Database (entities = arrayOf(
    ShoppingItem::class,
    ChoresItem::class),
    version = 1,
    exportSchema = false)       // use exportSchema when dealing with migrations
public abstract class ListsRoomDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
    abstract fun ChoresDao(): ChoresDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ListsRoomDatabase? = null

        // returns the 'ListsRoomDatabase' singleton. It'll create the database the first time it's accessed
        // names it "word_database"
        fun getDatabase(context: Context): ListsRoomDatabase {
            // if the database INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListsRoomDatabase::class.java,
                    "lists_database"
                ).build()
                // return instance
                INSTANCE = instance
                instance
            }
        }
    }
}
// A Repository manages queries and allows you to use multiple backends.
//  In the most common example, the Repository implements the logic for deciding
//  whether to fetch data from a network or use results cached in a local database.