package com.aldreduser.housemate.data.model.firestore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.ChoresItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose

// If an item is added to the database by another user, it will only show up when the activity is created again

//todo:
// figure out how to not get a null pinter after a db query (maybe look up fireStore with MVVM)
// Maybe make the query function suspend functions (with '.await()')
// -make/get client group id    (in the viewModel)
// -make/get client id          (in the viewModel)

// todo: set the 'clientGroupIDCollection' and 'clientIDCollection' here from the viewModel
// todo: set up an observer/observers in the view

/* pass below info to this class from viewModel
private val mainSharedPrefTag = "TestHousemateActySP"
private val groupIdSPTag = "Group ID"
private val clientIdSPTag = "Client ID"
 */

// have this function in the viewModel
// generateClientGroupID() {}
// generateClientID() {}

class DbApiService {

//    private val db = Firebase.firestore
//    private var groupIDCollectionDB: CollectionReference = db.collection(GENERAL_COLLECTION)
//        .document(GROUP_IDS_DOC).collection(clientGroupIDCollection!!)
//
//    private var _shoppingItems = MutableLiveData<MutableList<ShoppingItem>>()
//    private var _choreItems = MutableLiveData<MutableList<ChoresItem>>()
//
//    private var numOfShoppingItems = 0
//    private var numOfChoreItems = 0
//    private val shoppingItemsList = mutableListOf<ShoppingItem>()
//    private val choreItemsList = mutableListOf<ChoresItem>()
//    private val shoppingItemsNames = mutableListOf<String>()
//    private val choreItemsNames = mutableListOf<String>()

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
        listType: Any,
        itemName: String,
        volunteerName: String
    ) {
        when (listType) {
            ShoppingItem::class -> {
                groupIDsDocumentDB.collection(clientGroupIDCollection).document(SHOPPING_LIST_DOC)
                    .collection(SHOPPING_ITEMS_COLLECTION).document(itemName)
                    .update(VOLUNTEER_FIELD, volunteerName)
                    .addOnSuccessListener {
                        Log.i(TAG, "$itemName successfully updated to $volunteerName")
                    }
                    .addOnFailureListener { Log.e(TAG, "Error updating doc") }
            }
            ChoresItem::class -> {
                groupIDsDocumentDB.collection(clientGroupIDCollection).document(CHORES_LIST_DOC)
                    .collection(CHORE_ITEMS_COLLECTION).document(itemName)
                    .update(VOLUNTEER_FIELD, volunteerName)
                    .addOnSuccessListener {
                        Log.i(TAG, "$itemName successfully updated to $volunteerName")
                    }
                    .addOnFailureListener { Log.e(TAG, "Error updating doc") }
            }
            else -> {
                Log.e(TAG, "sendVolunteerToDb: Error recognizing list Type")
            }
        }
    }
    // DATABASE WRITES //

    // DATABASE READS //
    suspend fun getLastGroupAdded(): String? {
        return try {
            val newIDList = db.collection(HOUSEMATE_COLLECTION).get().await().documents.mapNotNull {
                val oldID = it.data!![LAST_GROUP_ADDED_FIELD] as String
                val newID = add1AndScrambleLetters(oldID)

                Log.i(TAG, "getLastGroupAdded: new group id created")
                db.collection(HOUSEMATE_COLLECTION).document(GROUP_IDS_DOC)
                    .update(LAST_GROUP_ADDED_FIELD, newID)
                    .addOnSuccessListener {
                        Log.i(TAG, "getLastGroupAdded: LAST_GROUP_ADDED_FIELD updated")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "getLastGroupAdded: database fetch failed", e)
                    }
                newID
            }
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
    fun deleteListItem(clientGroupIDCollection: String, listType: Any, itemName: String) {
        var itemsCollectionDB: CollectionReference =
            groupIDsDocumentDB.collection(clientGroupIDCollection)
        when (listType) {
            ShoppingItem::class -> {
                itemsCollectionDB = groupIDsDocumentDB.collection(clientGroupIDCollection)
                    .document(SHOPPING_LIST_DOC).collection(SHOPPING_ITEMS_COLLECTION)
            }
            ChoresItem::class -> {
                itemsCollectionDB = groupIDsDocumentDB.collection(clientGroupIDCollection)
                    .document(CHORES_LIST_DOC).collection(CHORE_ITEMS_COLLECTION)
            }
            else -> {
                Log.e(TAG, "deleteListItem: Error verifying the List Type")
            }
        }

        itemsCollectionDB.document(itemName).delete()
            .addOnSuccessListener { Log.i(TAG, "$itemName document deleted") }
            .addOnFailureListener { Log.e(TAG, "Failure to delete $itemName document") }
    }
    // DELETE DOCUMENT //








































    // SET UP FUNCTIONS //
    fun setUpRealtimeFetching() {
        // todo: use objects instead of hashmaps

        Log.d(TAG, "setUpRealtimeFetching: called")
        val shoppingItemCollectionDB = groupIDCollectionDB.document(SHOPPING_LIST_DOC)
            .collection(SHOPPING_ITEMS_COLLECTION)
        val choresItemCollectionDB = groupIDCollectionDB.document(CHORES_LIST_DOC)
            .collection(CHORE_ITEMS_COLLECTION)


        // try .addSnapshotListener from the collection
        shoppingItemCollectionDB.addSnapshotListener { snapshot, e ->

            if (e != null) {
                // if there is an exception, skip
                Log.d(TAG, "setUpRealtimeFetching: DB Query Fail in Shopping.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // turn each shopping item into an object
                val allShoppingItems =
                    snapshot.toObjects(ShoppingItem::class.java) as MutableList<ShoppingItem>
                _shoppingItems.value = allShoppingItems
            }
        }






        // fetch numOfShoppingItems (replace it with the code above)
        shoppingItemCollectionDB.get()
            .addOnSuccessListener { shoppingResult ->
                Log.d(TAG, "get3ItemsFromDB: shopping called")
                for (document in shoppingResult) {
                    // add all items to the list and exit the get call
                    val thisItem = document.data as MutableMap<String, Any>
                    shoppingItemsNames.add(thisItem[NAME_FIELD] as String)
                    shoppingItemsList.add(thisItem)


                    // set up shopping list realtime here
                    for (i in 0 until numOfShoppingItems) {
                        shoppingItemCollectionDB
                            .document(shoppingItemsNames[i])
                            .addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Log.d(TAG, "setUpRealtimeFetching: DB Listen Fail in shopping.", e)
                                    return@addSnapshotListener
                                }
                                if (snapshot != null && snapshot.exists()) {
                                    // Get 3 item maps from db and set them to threeShoppingItems
                                    shoppingItemsList[i] = snapshot.data as HashMap<String, Any>
                                    Log.d(TAG, "setUpRealtimeFetching: " +
                                            "${shoppingItemsNames[i]} fetch successful.")
                                } else {
                                    Log.d(TAG, "setUpRealtimeFetching: Data is null.")
                                }
                            }
                    }


                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error getting documents: shopping ", e)
            }

        // fetch numOfChoreItems
        choresItemCollectionDB.get()
            .addOnSuccessListener { choresResult ->
                Log.d(TAG, "get3ItemsFromDB: chores called")
                for (document in choresResult) {
                    // add all items to the list and exit the get call
                    val thisItem = document.data as MutableMap<String, Any>
                    choreItemsNames.add(thisItem[NAME_FIELD] as String)
                    choreItemsList.add(thisItem)

                    // set up chores list realtime here
                    for (i in 0 until numOfChoreItems) {
                        choresItemCollectionDB
                            .document(choreItemsNames[i])
                            .addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Log.d(TAG, "setUpRealtimeFetching: DB Listen Fail in chores.", e)
                                    return@addSnapshotListener
                                }
                                if (snapshot != null && snapshot.exists()) {
                                    // get 3 item maps from db and set them to threeChoreItems
                                    choreItemsList[i] = snapshot.data as HashMap<String, Any>
                                    Log.d(TAG, "setUpRealtimeFetching: " +
                                            "${choreItemsNames[i]} fetch successful.")
                                } else {
                                    Log.d(TAG, "setUpRealtimeFetching: Data is null.")
                                }

                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error getting documents: chores ", e)
            }

        //todo: remember to populate the UI after this
    }
    // SET UP FUNCTIONS //

    // DATABASE READS //
    // return a list of the shopping items
    fun getShoppingItems(): List<ShoppingItem> {

        return shoppingItemsList
    }
    // return a list of the chore items
    fun getChoreItems(): List<ChoresItem> {

        return choreItemsList
    }

    fun setClientID() {

    }

    fun updateClientID() {

    }
    // DATABASE READS //

    // DATABASE WRITES //
    fun sendVolunteerInputToDb() {

    }

    fun sendCompletionInputToDb() {

    }
    // DATABASE WRITES //
}