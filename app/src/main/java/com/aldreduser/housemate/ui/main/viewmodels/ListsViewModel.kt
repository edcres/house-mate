package com.aldreduser.housemate.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.*
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
    private val _listChosen = MutableLiveData<String>()      // Shopping or Chores. Use this to determine which list 'AddListItemFragment' is manipulating
    val listChosen: MutableLiveData<String> = _listChosen
    // Both Shopping and Chores
    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String> = _name
    private val _neededBy = MutableLiveData<String>()
    val neededBy: MutableLiveData<String> = _neededBy
    private val _priority = MutableLiveData<String>()
    val priority: MutableLiveData<String> = _priority
    // Only Shopping
    private val _quantity = MutableLiveData<String>()   // convert to double in the code
    val quantity: MutableLiveData<String> = _quantity
    private val _purchaseLocation = MutableLiveData<String>()
    val purchaseLocation: MutableLiveData<String> = _purchaseLocation
    private val _cost = MutableLiveData<String>()   // convert to double in the code
    val cost: MutableLiveData<String> = _cost
    // Only Chores
    private val _difficulty = MutableLiveData<String>()
    val difficulty: MutableLiveData<String> = _difficulty

    // Complete Lists
    private val shoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val choresItems = MutableLiveData<List<ChoresItem>>()

    init {
        //fetchShoppingItems()  //maybe use this
    }

    //todo: if Shopping list is chosen, hide only chores widgets, and show shopping widgets (helper function)
    //todo: if Chores list is chosen, hide only shopping widgets, and show chores widgets (helper function)

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
    fun setListToDisplay(listType: String) { _listChosen.value = listType }
    // might have to check if there is a flavor set, if so use the cupcake app as reference
    fun setPriority(desiredPriority: String) { _priority.value = desiredPriority }
    // might have to check if there is a flavor set, if so use the cupcake app as reference
    fun setDifficulty(desiredDifficulty: String) { _difficulty.value = desiredDifficulty }
}
