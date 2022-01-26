package com.aldreduser.housemate.data

import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.firestore.DbApiService
import kotlinx.coroutines.flow.Flow

class ListsRepository {

    private val dbApiService = DbApiService()

    fun setUpShoppingRealtimeFetching(groupID: String): Flow<List<ShoppingItem>> {
        return dbApiService.getShoppingItemsRealtime(groupID)
    }

    fun setUpChoresRealtimeFetching(groupID: String): Flow<List<ChoresItem>> {
        return dbApiService.getChoreItemsRealtime(groupID)
    }

    // add item
    fun addShoppingItemToDb(
        groupID: String,
        itemName: String,
        itemQuantity: Double,
        itemCost: Double,
        purchaseLocation: String,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int,
        addedBy: String
    ) {
        dbApiService.addShoppingItemToDatabase(
            groupID, itemName, itemQuantity, itemCost,
            purchaseLocation, itemNeededBy, itemPriority, addedBy
        )
    }
    fun addChoresItemToDb(
        groupID: String,
        itemName: String,
        itemDifficulty: Int,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int,
        addedBy: String
    ) {
        dbApiService.addChoresItemToDatabase(
            groupID, itemName, itemDifficulty, itemNeededBy, itemPriority, addedBy
        )
    }

    // get the last group added String
    suspend fun getLastGroupAdded(): String? {
        return dbApiService.getLastGroupAdded()
    }

    // get the latest clientID from the db
    suspend fun getLastClientAdded(groupID: String): String? {
        return dbApiService.getLastClientAdded(groupID)
    }

    // send volunteer name to db
    fun sendShoppingVolunteerToDb(groupID: String, itemName: String, volunteerName: String) {
        dbApiService.sendVolunteerToDb(groupID, ShoppingItem::class, itemName, volunteerName)
    }

    fun sendChoresVolunteerToDb(groupID: String, itemName: String, volunteerName: String) {
        dbApiService.sendVolunteerToDb(groupID, ChoresItem::class, itemName, volunteerName)
    }

    // delete item
    fun deleteShoppingListItem(groupID: String, itemName: String) {
        dbApiService.deleteListItem(groupID, ShoppingItem::class, itemName)
    }

    fun deleteChoresListItem(groupID: String, itemName: String) {
        dbApiService.deleteListItem(groupID, ChoresItem::class, itemName)
    }
}
