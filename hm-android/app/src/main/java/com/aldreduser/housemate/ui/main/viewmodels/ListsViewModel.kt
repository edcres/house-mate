package com.aldreduser.housemate.ui.main.viewmodels

import android.annotation.SuppressLint
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
    private var clientIDCollection: String? = null
    private var _shoppingItems = MutableLiveData<MutableList<ShoppingItem>>()
    val shoppingItems: LiveData<MutableList<ShoppingItem>> get() = _shoppingItems
    private var _choreItems = MutableLiveData<MutableList<ChoresItem>>()
    val choreItems: LiveData<MutableList<ChoresItem>> get() = _choreItems

    val itemsExpanded = mutableMapOf<String, Boolean>()
    private var _menuEditIsOn = MutableLiveData(false)
    val menuEditIsOn: LiveData<Boolean> get() = _menuEditIsOn

    var fragmentInView: String? = null
    var listInView = mutableMapOf<Int, String>()
    val listTypes = listOf("Shopping", "Chores")

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

    // UI //
    fun toggleEditBtn() {
        _menuEditIsOn.value = !_menuEditIsOn.value!!
    }
    // UI //

    // DATABASE FUNCTIONS //
    fun setItemsRealtime(listTag: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (listTag) {
                listTypes[0] ->
                    listsRepository.setUpShoppingRealtimeFetching(clientGroupIDCollection!!)
                        .collect { _shoppingItems.postValue(it.toMutableList()) }
                listTypes[1] ->
                    listsRepository.setUpChoresRealtimeFetching(clientGroupIDCollection!!)
                        .collect { _choreItems.postValue(it.toMutableList()) }
            }
        }
    }

    fun sendItemToDatabase(
        listTag: String, itemName: String, itemQuantity: Double,
        itemCost: Double, purchaseLocation: String,
        itemNeededBy: String,   // try and make this a date
        itemPriority: Int, itemDifficulty: Int,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (listTag) {
                listTypes[0] -> {
                    listsRepository.addShoppingItemToDb(
                        clientGroupIDCollection!!, itemName, itemQuantity, itemCost,
                        purchaseLocation, itemNeededBy, itemPriority, userName!!
                    )
                }
                listTypes[1] -> {
                    listsRepository.addChoresItemToDb(
                        clientGroupIDCollection!!, itemName, itemDifficulty,
                        itemNeededBy, itemPriority, userName!!
                    )
                }
            }
        }
    }

    fun toggleItemCompletion(listTag: String, itemName: String, isCompleted: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            when (listTag) {
                listTypes[0] -> {
                    listsRepository.toggleShoppingCompletion(
                        clientGroupIDCollection!!,
                        itemName,
                        isCompleted
                    )
                }
                listTypes[1] -> {
                    listsRepository.toggleChoreCompletion(
                        clientGroupIDCollection!!,
                        itemName,
                        isCompleted
                    )
                }
            }
        }
    }

    fun sendItemVolunteerToDb(listTag: String, listItem: String, volunteerName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when(listTag) {
                listTypes[0] -> {listsRepository.sendShoppingVolunteersToDb(
                    clientGroupIDCollection!!,
                    listItem,
                    volunteerName
                )}
                listTypes[1] -> {
                    listsRepository.sendChoresVolunteersToDb(
                        clientGroupIDCollection!!,
                        listItem,
                        volunteerName
                    )
                }
            }
        }
    }

    fun deleteListItem(listTag: String, itemName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (listTag) {
                listTypes[0] -> {
                    listsRepository.deleteShoppingListItem(clientGroupIDCollection!!, itemName)
                }
                listTypes[1] -> {
                    listsRepository.deleteChoresListItem(clientGroupIDCollection!!, itemName)
                }
            }
        }
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
    fun clearSPs() {
        sharedPrefs!!.edit().clear().commit()
        Log.i(TAG, "clearSPs: SPs cleared")
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
    fun setClientID() {
        clientIDCollection = getDataFromSP(CLIENT_ID_SP_TAG)
        if (clientIDCollection == null) {
            CoroutineScope(Dispatchers.IO).launch {
                clientIDCollection =
                    listsRepository.getLastClientAdded(clientGroupIDCollection!!)

                withContext(Dispatchers.Main) {
                    if (clientIDCollection != null) {
                        sendDataToSP(CLIENT_ID_SP_TAG, clientIDCollection!!)
                    } else {
                        Log.e(TAG, "generateClientGroupID(): clientIDCollection is null")
                    }
                }
            }
        }
    }
    fun getCurrentGroupID(): String? {
        clientGroupIDCollection = getDataFromSP(GROUP_ID_SP_TAG)
        return clientGroupIDCollection
    }
    // ID QUERIES //
}
