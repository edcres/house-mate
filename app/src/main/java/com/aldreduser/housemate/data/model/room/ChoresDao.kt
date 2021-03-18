package com.aldreduser.housemate.data.model.room

import androidx.room.*
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoresDao {

    // get list of items
    @Query("SELECT * FROM chore_item_table ORDER BY id ASC")    //ASC -> ascending
    fun getIDsInOrder(): Flow<List<ChoresItem>>

    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingItem: ChoresItem)

    // update
    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    // delete item
    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    // delete all
    @Query("DELETE FROM chore_item_table")
    suspend fun deleteAll()
}