package com.aldreduser.housemate.data.model.room

import androidx.room.*
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoresDao {

    @Query("SELECT * FROM chore_item_table ORDER BY id ASC")    //ASC -> ascending
    fun getIDsInOrder(): Flow<List<ChoresItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingItem: ChoresItem)

    //@Update
    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    //@Delete item
    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Query("DELETE FROM chore_item_table")
    suspend fun deleteAll()
}