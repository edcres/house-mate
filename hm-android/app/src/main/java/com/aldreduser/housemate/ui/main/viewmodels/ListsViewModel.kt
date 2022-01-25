package com.aldreduser.housemate.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import kotlinx.coroutines.launch

// todo: If i need to null check the dataBinding properties for the views.:
// android:text='@{item.title ?? ""}'          or              android:text='@{item.title != null ? user.title : ""}'

// maybe have different viewmodels: mainActivity, shoppingList, choresList
// Shared viewModel for MainActivity, ShoppingFragment, and ChoresFragment
class ListsViewModel(
    private val listsRepository: ListsRepository,
    private val application: Application): ViewModel() {

    // DataBound Variables
    private val _shoppingPriority = MutableLiveData<String>()//leave this on, connect it to the Entity, make it an int type (like in the Entity)
    val shoppingPriority: MutableLiveData<String> = _shoppingPriority
    private val _choresPriority = MutableLiveData<String>()//leave this on, connect it to the Entity, make it an int type (like in the Entity)
    val choresPriority: MutableLiveData<String> = _choresPriority
    private val _difficulty = MutableLiveData<String>()//leave this on, connect it to the Entity, make it an int type (like in the Entity)
    val difficulty: MutableLiveData<String> = _difficulty

    // Complete Lists
    private val shoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val choresItems = MutableLiveData<List<ChoresItem>>()

    init {
        //fetchShoppingItems()  //maybe use this
    }

    // DATABASE QUERIES //
    //get items
    fun getShoppingItems(): LiveData<List<ShoppingItem>> { return shoppingItems }
    //insert item
    fun insert(choresItem: ChoresItem) = viewModelScope.launch {
        listsRepository.insertChoresItem(choresItem)
    }
    //delete item
    fun delete(shoppingItem: ShoppingItem) = viewModelScope.launch {
//        listsRepository.delete
    }
    //other queries...

    // CLICK HANDLERS //

    // SETTERS //
    // might have to check if there is a flavor set, if so use the cupcake app as reference
    fun setPriority(desiredPriority: String) { _shoppingPriority.value = desiredPriority }  //leave this on
    // might have to check if there is a flavor set, if so use the cupcake app as reference
    fun setDifficulty(desiredDifficulty: String) { _difficulty.value = desiredDifficulty }  //leave this on
}



// More DataBound variables, probably delete
//    private val _quantity = MutableLiveData<String>()   // convert to double in the code
//    val quantity: MutableLiveData<String> = _quantity
//    private val _purchaseLocation = MutableLiveData<String>()
//    val purchaseLocation: MutableLiveData<String> = _purchaseLocation
//    private val _cost = MutableLiveData<String>()   // convert to double in the code
//    val cost: MutableLiveData<String> = _cost
//    private val _listChosen = MutableLiveData<String>()
//    val listChosen: MutableLiveData<String> = _listChosen
//    // Both Shopping and Chores
//    private val _name = MutableLiveData<String>()
//    val name: MutableLiveData<String> = _name
//    private val _neededBy = MutableLiveData<String>()
//    val neededBy: MutableLiveData<String> = _neededBy