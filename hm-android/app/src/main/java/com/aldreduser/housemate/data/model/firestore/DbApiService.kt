package com.aldreduser.housemate.data.model.firestore

import android.util.Log
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.util.add1AndScrambleLetters
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class DbApiService {

    private val shoppingItem = "Shopping"
    private val choreItem = "Chore"
    private val db = Firebase.firestore
    private var groupIDsDocumentDB: DocumentReference = db.collection(GENERAL_COLLECTION)
        .document(GROUP_IDS_DOC)

    companion object {
        const val TAG = "DbApiServiceTAG"
        const val DEFAULT_CLIENT_ID = "00000000asdfg"
        const val GENERAL_COLLECTION = "General Collection"
        const val GROUP_IDS_DOC = "Group IDs"
        const val CLIENT_IDS_DOC = "Client IDs"
        const val SHOPPING_LIST_DOC = "Shopping List"
        const val SHOPPING_ITEMS_COLLECTION = "Shopping Items"
        const val CHORES_LIST_DOC = "Chores List"
        const val CHORE_ITEMS_COLLECTION = "Chore Items"

        const val LAST_GROUP_ADDED_FIELD = "lastGroupAdded"
        const val LAST_CLIENT_ADDED_FIELD = "lastClientAdded"
        const val NAME_FIELD = "name"
        const val QUANTITY_FIELD = "quantity"
        const val ADDED_BY_FIELD = "addedBy"
        const val COMPLETED_FIELD = "completed"
        const val COST_FIELD = "cost"
        const val PURCHASE_LOCATION_FIELD = "purchaseLocation"
        const val NEEDED_BY_FIELD = "neededBy"
        const val VOLUNTEER_FIELD = "volunteer"
        const val PRIORITY_FIELD = "priority"
        const val DIFFICULTY_FIELD = "difficulty"
    }

    // SET UP FUNCTIONS //
    fun getShoppingItemsRealtime(clientGroupIDCollection: String): Flow<List<ShoppingItem>> {
        return callbackFlow {
            val listenerRegistration =
                groupIDsDocumentDB.collection(clientGroupIDCollection).document(SHOPPING_LIST_DOC)
                    .collection(SHOPPING_ITEMS_COLLECTION)
                    .addSnapshotListener {
                            querySnapshot: QuerySnapshot?,
                            firebaseFirestoreException: FirebaseFirestoreException? ->
                        if (firebaseFirestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firebaseFirestoreException
                            )
                            return@addSnapshotListener
                        }

                        if (querySnapshot != null) {
                            val itemsList = querySnapshot.toObjects(ShoppingItem::class.java)
                            offer(itemsList)
                        } else {
                            Log.i(TAG, "getShoppingItemsRealtime: querySnapshot is null")
                        }
                    }
            awaitClose {
                Log.i(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }

    fun getChoreItemsRealtime(clientGroupIDCollection: String): Flow<List<ChoresItem>> {
        return callbackFlow {
            val listenerRegistration =
                groupIDsDocumentDB.collection(clientGroupIDCollection).document(CHORES_LIST_DOC)
                    .collection(CHORE_ITEMS_COLLECTION)
                    .addSnapshotListener {
                            querySnapshot: QuerySnapshot?,
                            firebaseFirestoreException: FirebaseFirestoreException? ->
                        if (firebaseFirestoreException != null) {
                            cancel(
                                message = "Error fetching posts",
                                cause = firebaseFirestoreException
                            )
                            return@addSnapshotListener
                        }

                        if (querySnapshot != null) {
                            val itemsList = querySnapshot.toObjects(ChoresItem::class.java)
                            offer(itemsList)
                        } else {
                            Log.i(TAG, "getChoreItemsRealtime: querySnapshot is null")
                        }
                    }
            awaitClose {
                Log.i(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }
    // SET UP FUNCTIONS //

    // DATABASE WRITES //
    fun addShoppingItemToDatabase(
        clientGroupIDCollection: String,
        itemName: String,
        itemQuantity: Double,
        itemCost: Double,
        purchaseLocation: String,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int,
        addedBy: String
    ) {
        val shoppingItemData = hashMapOf(
            NAME_FIELD to itemName,
            QUANTITY_FIELD to itemQuantity,
            COST_FIELD to itemCost,
            PURCHASE_LOCATION_FIELD to purchaseLocation,
            NEEDED_BY_FIELD to itemNeededBy,
            PRIORITY_FIELD to itemPriority,
            COMPLETED_FIELD to false,
            VOLUNTEER_FIELD to "",
            ADDED_BY_FIELD to addedBy
        )
        groupIDsDocumentDB.collection(clientGroupIDCollection).document(SHOPPING_LIST_DOC)
            .collection(SHOPPING_ITEMS_COLLECTION).document(itemName).set(shoppingItemData)
            .addOnSuccessListener {
                Log.i(TAG, "$itemName DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.e(TAG, "Error writing document", e) }
    }

    fun addChoresItemToDatabase(
        clientGroupIDCollection: String,
        itemName: String,
        itemDifficulty: Int,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int,
        addedBy: String
    ) {
        val choresItemData = hashMapOf(
            NAME_FIELD to itemName,
            DIFFICULTY_FIELD to itemDifficulty,
            NEEDED_BY_FIELD to itemNeededBy,
            PRIORITY_FIELD to itemPriority,
            COMPLETED_FIELD to false,
            VOLUNTEER_FIELD to "",
            ADDED_BY_FIELD to addedBy
        )
        groupIDsDocumentDB.collection(clientGroupIDCollection).document(CHORES_LIST_DOC)
            .collection(CHORE_ITEMS_COLLECTION).document(itemName).set(choresItemData)
            .addOnSuccessListener {
                Log.i(TAG, "$itemName DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.e(TAG, "Error writing document", e) }
    }

    fun sendVolunteerToDb(
        clientGroupIDCollection: String,
        listType: String,
        listItem: String,
        volunteerName: String
    ) {
        val listDoc = getListDoc(listType)
        val itemsCollection = getItemsCollection(listType)
        val itemsCollectionPath = groupIDsDocumentDB.collection(clientGroupIDCollection)
            .document(listDoc).collection(itemsCollection)
        itemsCollectionPath
            .document(listItem).update(VOLUNTEER_FIELD, volunteerName)
            .addOnSuccessListener {
                Log.i(TAG, "sendVolunteerToDb: updated")
            }.addOnFailureListener { e ->
                Log.e(TAG, "sendVolunteerToDb: failed", e)
            }
        // I was going to use this batch write when the fragment is destroyed but
        //  there's not enough time for the db operation to complete if the app closes.
//        parameter -> volunteersList: Map<String, String>
//        db.runBatch { batch ->
//            volunteersList.forEach {
//                batch.update(itemsCollectionPath.document(it.key), VOLUNTEER_FIELD, it.value)
//            }
//        }
    }

    fun toggleItemCompletion(
        groupID: String,
        listType: String,
        itemName: String,
        isCompleted: Boolean
    ) {
        val listDoc = getListDoc(listType)
        val itemsCollection = getItemsCollection(listType)
        groupIDsDocumentDB.collection(groupID).document(listDoc)
            .collection(itemsCollection).document(itemName)
            .update(COMPLETED_FIELD, isCompleted)
            .addOnSuccessListener {
                Log.i(TAG, "$itemName completion successfully updated to $isCompleted")
            }
            .addOnFailureListener { Log.e(TAG, "Error updating doc") }
    }
    // DATABASE WRITES //

    // DATABASE READS //
    suspend fun getLastGroupAdded(): String? {
        return try {
            val newIDList = db.collection(GENERAL_COLLECTION).get().await().documents.mapNotNull {
                val oldID = it.data!![LAST_GROUP_ADDED_FIELD] as String
                val newID = add1AndScrambleLetters(oldID)

                Log.i(TAG, "getLastGroupAdded: new group id created")
                db.collection(GENERAL_COLLECTION).document(GROUP_IDS_DOC)
                    .update(LAST_GROUP_ADDED_FIELD, newID)
                    .addOnSuccessListener {
                        Log.i(TAG, "getLastGroupAdded: LAST_GROUP_ADDED_FIELD updated")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "getLastGroupAdded: database fetch failed", e)
                    }
                newID
            }
            // If this ('newIDList[0]') is empty, there is no last group added
            newIDList[0]
        } catch (e: Exception) {
            Log.e(TAG, "Error getting group id", e)
            null
        }
    }

    suspend fun getLastClientAdded(clientGroupIDCollection: String): String? {
        val clientIDsDoc =
            groupIDsDocumentDB.collection(clientGroupIDCollection).document(CLIENT_IDS_DOC)
        return try {
            var newIDList = groupIDsDocumentDB.collection(clientGroupIDCollection)
                .get().await().documents.mapNotNull {
                    val oldID = it.data!![LAST_CLIENT_ADDED_FIELD] as String
                    val newID = add1AndScrambleLetters(oldID)
                    Log.i(TAG, "getLastClientAdded: new client id created")

                    clientIDsDoc.update(LAST_CLIENT_ADDED_FIELD, newID)
                        .addOnSuccessListener {
                            Log.i(TAG, "getLastClientAdded: LAST_CLIENT_ADDED_FIELD updated")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "getLastClientAdded: database fetch failed", e)
                        }
                    newID
                }
            if (newIDList.isEmpty()) {
                val newID = add1AndScrambleLetters(DEFAULT_CLIENT_ID)
                Log.d(TAG, "getLastClientAdded: new client id added $newID")
                val firstDocData = hashMapOf<String, Any>(LAST_CLIENT_ADDED_FIELD to newID)
                clientIDsDoc.set(firstDocData)
                    .addOnSuccessListener {
                        Log.i(TAG, "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error writing document", e)
                    }
                newIDList = listOf(newID)
            }
            newIDList[0]
        } catch (e: Exception) {
            Log.e(TAG, "Error getting client id", e)
            null
        }
    }
    // DATABASE READS //

    // DELETE DOCUMENT //
    fun deleteListItem(clientGroupIDCollection: String, listType: String, itemName: String) {
        val listDoc = getListDoc(listType)
        val itemsCollection = getItemsCollection(listType)

        groupIDsDocumentDB.collection(clientGroupIDCollection)
            .document(listDoc).collection(itemsCollection).document(itemName)
            .delete()
            .addOnSuccessListener { Log.i(TAG, "$itemName document deleted") }
            .addOnFailureListener { Log.e(TAG, "Failure to delete $itemName document") }
    }
    // DELETE DOCUMENT //

    // HELPER FUNCTIONS //
    private fun getListDoc(listType: String): String {
        return when (listType) {
            shoppingItem -> SHOPPING_LIST_DOC
            choreItem -> CHORES_LIST_DOC
            else -> "placeholder"
        }
    }
    private fun getItemsCollection(listType: String): String {
        return when (listType) {
            shoppingItem -> SHOPPING_ITEMS_COLLECTION
            choreItem -> CHORE_ITEMS_COLLECTION
            else -> "placeholder"
        }
    }
    // HELPER FUNCTIONS //
}