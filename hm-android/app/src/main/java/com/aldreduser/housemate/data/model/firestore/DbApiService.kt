package com.aldreduser.housemate.data.model.firestore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.model.ChoresItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

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

class ApiService {

    private val db = Firebase.firestore
    private var groupIDCollectionDB: CollectionReference = db.collection(GENERAL_COLLECTION)
        .document(GROUP_IDS_DOC).collection(clientGroupIDCollection!!)

    private var _shoppingItems = MutableLiveData<MutableList<ShoppingItem>>()
    private var _choreItems = MutableLiveData<MutableList<ChoresItem>>()

    private var numOfShoppingItems = 0
    private var numOfChoreItems = 0
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

    // INTERNAL VARIABLES (public versions of variables) //
    internal var shoppingItems:MutableLiveData<MutableList<ShoppingItem>>
     get() { return _shoppingItems }
     set(value) {_shoppingItems = value}

    internal var choreItems:MutableLiveData<MutableList<ChoresItem>>
     get() { return _choreItems }
     set(value) {_choreItems = value}
}