package com.aldreduser.housemate.data

import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.firestore.DbApiService
import com.aldreduser.housemate.util.CHORE_ITEM
import com.aldreduser.housemate.util.SHOPPING_ITEM
import kotlinx.coroutines.flow.Flow

class ListsRepository {

    private val dbApiService = DbApiService()

    fun setUpShoppingRealtimeFetching(groupID: String): Flow<List<ShoppingItem>> {
        return dbApiService.getShoppingItemsRealtime(groupID)
    }

    fun setUpChoresRealtimeFetching(groupID: String): Flow<List<ChoresItem>> {
        return dbApiService.getChoreItemsRealtime(groupID)
    }

    suspend fun addShoppingItemToDb(
            groupID: String,
            itemName: String,
            itemQuantity: Double,
            itemCost: Double,
            purchaseLocation: String,
            itemNeededBy: String,
            itemPriority: Int,
            addedBy: String,
            notes: String
    ) {
        dbApiService.addShoppingItemToDatabase(
            groupID, itemName, itemQuantity, itemCost,
            purchaseLocation, itemNeededBy, itemPriority, addedBy, notes
        )
    }
    suspend fun addChoresItemToDb(
        groupID: String,
        itemName: String,
        itemDifficulty: Int,
        itemNeededBy: String,
        itemPriority: Int,
        addedBy: String,
        notes: String
    ) {
        dbApiService.addChoresItemToDatabase(
            groupID, itemName, itemDifficulty, itemNeededBy, itemPriority, addedBy, notes
        )
    }

    suspend fun getLastGroupAdded(): String? {
        return dbApiService.getLastGroupAdded()
    }

    // get the latest clientID from the db
    suspend fun getLastClientAdded(groupID: String): String? {
        return dbApiService.getLastClientAdded(groupID)
    }

    fun toggleShoppingCompletion(groupID: String, itemName:String, isCompleted: Boolean) {
        dbApiService.toggleItemCompletion(groupID, SHOPPING_ITEM, itemName, isCompleted)
    }

    fun toggleChoreCompletion(groupID: String, itemName:String, isCompleted: Boolean) {
        dbApiService.toggleItemCompletion(groupID, CHORE_ITEM, itemName, isCompleted)
    }

    fun sendShoppingVolunteersToDb(groupID: String, listItem:String, volunteerName: String) {
        dbApiService.sendVolunteerToDb(groupID, SHOPPING_ITEM, listItem, volunteerName)
    }

    fun sendChoresVolunteersToDb(groupID: String, listItem:String, volunteerName: String) {
        dbApiService.sendVolunteerToDb(groupID, CHORE_ITEM, listItem, volunteerName)
    }

    fun deleteShoppingListItem(groupID: String, itemName: String) {
        dbApiService.deleteListItem(groupID, SHOPPING_ITEM, itemName)
    }

    fun deleteChoresListItem(groupID: String, itemName: String) {
        dbApiService.deleteListItem(groupID, CHORE_ITEM, itemName)
    }
}
