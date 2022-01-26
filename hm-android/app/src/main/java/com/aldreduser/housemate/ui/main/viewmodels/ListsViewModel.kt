package com.aldreduser.housemate.ui.main.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListsViewModel: ViewModel() {

    private val listsRepository = ListsRepository()
    var sharedPrefs: SharedPreferences? = null
    var userName: String? = null
    var clientGroupIDCollection: String? = null
    var clientIDCollection: String? = null
    private var _shoppingItems = MutableLiveData<MutableList<ShoppingItem>>()
    val shoppingItems: LiveData<MutableList<ShoppingItem>> get() = _shoppingItems
    private var _choreItems = MutableLiveData<MutableList<ChoresItem>>()
    val choreItems: LiveData<MutableList<ChoresItem>> get() = _choreItems

    companion object {
        const val TAG = "ListsVmTAG"
        const val USER_NAME_SP_TAG = "User Name"
        const val GROUP_ID_SP_TAG = "Group ID"
        const val CLIENT_ID_SP_TAG = "Client ID"
    }

    init {
        Log.i(TAG, "ViewModel initialized")
        Log.i(TAG, "_shoppingItems: ${_shoppingItems.value?.size}")
        Log.i(TAG, "_choreItems: ${_choreItems.value?.size}")
    }

    // DATABASE FUNCTIONS //
    // Set up list items realtime fetching
    fun setShoppingItemsRealtime() {
        CoroutineScope(Dispatchers.IO).launch {
            listsRepository.setUpShoppingRealtimeFetching(clientGroupIDCollection!!)
                .collect {
                    _shoppingItems.postValue(it.toMutableList())
                }
        }
    }
    fun setChoreItemsRealtime() {
        CoroutineScope(Dispatchers.IO).launch {
            listsRepository.setUpChoresRealtimeFetching(clientGroupIDCollection!!)
                .collect {
                    _choreItems.postValue(it.toMutableList())
                }
        }
    }
    // Send data to database
    fun sendShoppingItemToDatabase(
        itemName: String,
        itemQuantity: Double,
        itemCost: Double,
        purchaseLocation: String,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int
    ) {
        listsRepository.addShoppingItemToDb(
            clientGroupIDCollection!!, itemName, itemQuantity, itemCost,
            purchaseLocation, itemNeededBy, itemPriority, userName!!
        )
    }
    fun sendChoresItemToDatabase(
        itemName: String,
        itemDifficulty: Int,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int
    ) {
        listsRepository.addChoresItemToDb(
            clientGroupIDCollection!!, itemName, itemDifficulty,
            itemNeededBy, itemPriority, userName!!
        )
    }
    fun sendShoppingVolunteerToDb(itemName: String, volunteerName: String) {
        listsRepository.sendShoppingVolunteerToDb(
            clientGroupIDCollection!!,
            itemName,
            volunteerName
        )
    }
    fun sendChoresVolunteerToDb(itemName: String, volunteerName: String) {
        listsRepository.sendChoresVolunteerToDb(
            clientGroupIDCollection!!,
            itemName,
            volunteerName
        )
    }
    fun deleteShoppingListItem(itemName: String) {
        listsRepository.deleteShoppingListItem(clientGroupIDCollection!!, itemName)
    }
    fun deleteChoresListItem(itemName: String) {
        listsRepository.deleteChoresListItem(clientGroupIDCollection!!, itemName)
    }
    // DATABASE FUNCTIONS //

    // SHARED PREFERENCE //
    fun getDataFromSP(theTag: String): String? {
        return sharedPrefs!!.getString(theTag, null)
    }
    @SuppressLint("ApplySharedPref")
    fun sendDataToSP(theTag: String, dataToSend: String) {
        val spEditor: SharedPreferences.Editor = sharedPrefs!!.edit()
        spEditor.putString(theTag, dataToSend).commit()
    }
    @SuppressLint("ApplySharedPref")
    fun clearSP() {
        sharedPrefs!!.edit().clear().commit()
    }
    // SHARED PREFERENCE //

    // ID QUERIES //
    fun generateClientGroupID() {
        // Get the latest groupID from the remote db (ie. 00000001asdfg)
        CoroutineScope(Dispatchers.IO).launch {
            clientGroupIDCollection = listsRepository.getLastGroupAdded()
            withContext(Dispatchers.Main) {
                if (clientGroupIDCollection != null) {
                    sendDataToSP(GROUP_ID_SP_TAG, clientGroupIDCollection!!)
                    setClientID()
                } else {
                    Log.e(TAG, "generateClientGroupID(): clientGroupIDCollection is null")
                }
            }
        }
    }
    private fun setClientID() {
        clientIDCollection = getDataFromSP(CLIENT_ID_SP_TAG)
        if (clientIDCollection == null) {

            CoroutineScope(Dispatchers.IO).launch {
                clientIDCollection =
                    listsRepository.getLastClientAdded(clientGroupIDCollection!!)

                withContext(Dispatchers.Main) {
                    if (clientIDCollection != null) {
                        sendDataToSP(GROUP_ID_SP_TAG, clientIDCollection!!)
                    } else {
                        Log.e(TAG, "generateClientGroupID(): clientIDCollection is null")
                    }
                }
            }
        }
    }
    // ID QUERIES //
}
