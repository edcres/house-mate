package com.aldreduser.housemate.data.model.room

import androidx.room.*
import com.aldreduser.housemate.data.model.ShoppingItem

// specify SQL queries and associate them with method calls.

@Dao
interface ShoppingDao {

    // get list of items
    @Query("SELECT * FROM shopping_item_table ORDER BY id ASC")
    fun getIDsInOrder(): List<ShoppingItem>

    // onConflict strategy ignores a new word if it's exactly the same as one already in the list.
    // add a new item
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingItem: ShoppingItem)
    //'suspend' is for coroutines

    //Update item
    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    //Delete item
    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    // delete all items
    @Query("DELETE FROM shopping_item_table")
    suspend fun deleteAll()
}