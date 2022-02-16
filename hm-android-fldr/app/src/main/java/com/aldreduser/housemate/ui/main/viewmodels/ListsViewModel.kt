package com.aldreduser.housemate.ui.main.viewmodels

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.CalendarDate
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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
    var fragmentInView: String? = null
    var listInView = mutableMapOf<Int, String>()
    val listTypes = listOf("Shopping", "Chores")
    val itemsExpanded = mutableMapOf<String, Boolean>()
    private var _menuEditIsOn = MutableLiveData(false)
    val menuEditIsOn: LiveData<Boolean> get() = _menuEditIsOn
    private var _itemToEdit = MutableLiveData<Any?>()
    val itemToEdit: LiveData<Any?> get() = _itemToEdit

    companion object {
        const val TAG = "ListsVmTAG"
        const val USER_NAME_SP_TAG = "User Name"
        const val GROUP_ID_SP_TAG = "Group ID"
        const val CLIENT_ID_SP_TAG = "Client ID"
        const val PAST_GROUPS_SP_TAG = "Past Groups" //separated by "-"
    }

    init {
        Log.i(TAG, "ViewModel initialized")
        Log.i(TAG, "_shoppingItems: ${_shoppingItems.value?.size}")
        Log.i(TAG, "_choreItems: ${_choreItems.value?.size}")
    }

    // HELPERS //
    fun toggleEditBtn() {
        _menuEditIsOn.value = !_menuEditIsOn.value!!
    }
    fun setItemToEdit(chosenItem: Any?) {
        _itemToEdit.value = chosenItem
    }
    fun getDateTimeCalendar(): CalendarDate {
        val calendarDate = CalendarDate()
        val calendar = Calendar.getInstance()
        calendarDate.day = calendar.get(Calendar.DAY_OF_MONTH)
        calendarDate.month = calendar.get(Calendar.MONTH)
        calendarDate.year = calendar.get(Calendar.YEAR)
        return calendarDate
    }
    fun clearLists() {
        _shoppingItems.postValue(mutableListOf())
        _choreItems.postValue(mutableListOf())
    }
    fun setGroupID(selectedGroup: String) {
        clientGroupIDCollection = selectedGroup
        sendGroupToSP()
        setItemsRealtime(listTypes[0])
        setItemsRealtime(listTypes[1])
    }
    private fun sendGroupToSP() {
        sendDataToSP(GROUP_ID_SP_TAG, clientGroupIDCollection!!)
        val pastGroups = getDataFromSP(PAST_GROUPS_SP_TAG)?.split("-")
        if(pastGroups.isNullOrEmpty()) {
            sendDataToSP(PAST_GROUPS_SP_TAG, clientGroupIDCollection!!)
        } else {
            val newGroups = pastGroups.toMutableList()
            if (newGroups.size >= 5) newGroups.removeAt(0)
            newGroups.add(clientGroupIDCollection!!)
            sendDataToSP(PAST_GROUPS_SP_TAG, newGroups.joinToString("-"))
        }
    }
    // HELPERS //

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
        itemNeededBy: String,
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
//    @SuppressLint("ApplySharedPref")
//    fun clearSPs() {
//        sharedPrefs!!.edit().clear().commit()
//        Log.i(TAG, "clearSPs: SPs cleared")
//    }
    // SHARED PREFERENCE //

    // ID QUERIES //
    fun generateClientGroupID() {
        // Get the latest groupID from the remote db (ie. 00000001asdfg)
        CoroutineScope(Dispatchers.IO).launch {
            clientGroupIDCollection = listsRepository.getLastGroupAdded()
            withContext(Dispatchers.Main) {
                if (clientGroupIDCollection != null) {
                    sendGroupToSP()
                    setClientID()
                } else {
                    Log.e(TAG, "generateClientGroupID(): clientGroupIDCollection is null")
                }
            }
        }
    }
    fun setClientID() {
        // Client Id is changed permanently whenever a new group is added
        clientIDCollection = null
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

    fun getCurrentGroupID(): String? {
        clientGroupIDCollection = getDataFromSP(GROUP_ID_SP_TAG)
        return clientGroupIDCollection
    }
    // ID QUERIES //
}
