package com.aldreduser.housemate.data

import androidx.annotation.WorkerThread
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.remote.api.ApiHelper
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.room.ChoresDao
import com.aldreduser.housemate.data.model.room.ShoppingDao
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

// A Repository manages queries and allows you to use multiple backends.
//  -In the most common example, the Repository implements the logic for deciding
//    whether to fetch data from a network or use results cached in a local database.
// Only the DAOs are exposed to the repository, not the entire database
class ListsRepository(
    private val shoppingDao: ShoppingDao,
    private val choresDao: ChoresDao,
    private val apiHelper: ApiHelper) {

    // LOCAL //
    //Shopping Items
    // get list of items
    val allShoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getIDsInOrder()
    // insert
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insert(shoppingItem)
    }
    // update
    // delete
    // delete all

    //Chores Items
    // getIDsInOrder
    val allChoreItems: Flow<List<ChoresItem>> = choresDao.getIDsInOrder()
    // insert
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertChoresItem(choresItem: ChoresItem) {
        choresDao.insert(choresItem)
    }
    // update
    // delete item
    // delete all

    // REMOTE //
    // todo: make remote database work with local database
    // this is code for the remote database
//    fun getShoppingItems(): Single<List<ShoppingItem>> {
//        return apiHelper.getShoppingItems()
//    }
}
