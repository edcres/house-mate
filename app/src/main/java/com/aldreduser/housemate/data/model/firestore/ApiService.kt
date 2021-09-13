package com.aldreduser.housemate.data.model.firestore

import android.util.Log
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.ChoresItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//todo:
// figure out how to not get a null pinter after a db query (maybe look up fireStore with MVVM)
// Maybe make the query function suspend functions (with '.await()')

// todo: set the 'clientGroupIDCollection' and 'clientIDCollection' here from the viewModel

/* pass below info to this class from viewModel
private val mainSharedPrefTag = "TestHousemateActySP"
private val groupIdSPTag = "Group ID"
private val clientIdSPTag = "Client ID"
 */

// have this function in the viewModel
// generateClientGroupID() {}
// generateClientID() {}

class ApiService {

    private val db = Firebase.firestore
    private lateinit var groupIDCollectionDB: CollectionReference

    private val shoppingItemsList = mutableListOf<ShoppingItem>()
    private val choreItemsList = mutableListOf<ChoresItem>()
    private val shoppingItemsNames = mutableListOf<String>()
    private val choreItemsNames = mutableListOf<String>()

    companion object {
        const val TAG = "ApiServiceTAG"

        const val GENERAL_COLLECTION = "General Collection"
        const val GROUP_IDS_DOC = "Group IDs"
        const val CLIENT_IDS_DOC = "Client IDs"
        const val SHOPPING_LIST_DOC = "Shopping List"
        const val SHOPPING_ITEMS_COLLECTION = "Shopping Items"
        const val CHORES_LIST_DOC = "Chores List"
        const val CHORE_ITEMS_COLLECTION = "Chore Items"

        // these might be needed in the viewModel and passed to here
        var clientGroupIDCollection: String? = null
        var clientIDCollection: String? = null

        const val NAME_FIELD = "name"
        const val QUANTITY_FIELD = "quantity"
        const val ADDED_BY_FIELD = "added by"
        const val COMPLETED_FIELD = "completed"
        const val COST_FIELD = "cost"
        const val PURCHASE_LOCATION_FIELD = "purchase location"
        const val NEEDED_BY_FIELD = "needed by"
        const val VOLUNTEER_FIELD = "volunteer"
        const val PRIORITY_FIELD = "priority"
        const val DIFFICULTY_FIELD = "difficulty"
    }

    init {
        // todo: possible bug: idk if 'clientGroupIDCollection' and 'clientIDCollection'
        //  will always have a value in this class
        groupIDCollectionDB = db.collection(GENERAL_COLLECTION).document(GROUP_IDS_DOC)
            .collection(clientGroupIDCollection!!)
    }

    // SET UP FUNCTIONS //
    fun setUpRealtimeFetching() {
        var numOfShoppingItems = 0
        var numOfChoreItems = 0
        //todo: fetch numOfShoppingItems
        //todo: fetch numOfChoreItems
        Log.d(TAG, "setUpRealtimeFetching: called")
        // get items in shopping list
        for (i in 0 until numOfShoppingItems) {
            groupIDCollectionDB.document(SHOPPING_LIST_DOC)
                .collection(SHOPPING_ITEMS_COLLECTION)
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
        // get items in shopping list
        for (i in 0 until numOfChoreItems) {
            groupIDCollectionDB.document(CHORES_LIST_DOC)
                .collection(CHORE_ITEMS_COLLECTION)
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