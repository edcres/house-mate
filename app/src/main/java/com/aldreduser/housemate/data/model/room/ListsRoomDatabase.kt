package com.aldreduser.housemate.data.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem

@Database (
    entities = [ShoppingItem::class, ChoresItem::class],
    version = 1,
    exportSchema = false)       // use exportSchema when dealing with migrations
abstract class ListsRoomDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDao
    abstract fun choresDao(): ChoresDao

    // A Singleton prevents multiple instances of database opening at the same time.
    companion object {
        @Volatile
        private var INSTANCE: ListsRoomDatabase? = null
        private const val DATABASE_NAME = "lists_database"

        // returns the 'ListsRoomDatabase' singleton. It'll create the database the first time it's accessed
        // names it "word_database"
        fun getDatabase(context: Context): ListsRoomDatabase {
            // if the database INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ListsRoomDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
