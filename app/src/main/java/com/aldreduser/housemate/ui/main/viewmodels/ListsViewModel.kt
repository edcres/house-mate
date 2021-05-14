package com.aldreduser.housemate.ui.main.viewmodels

import androidx.lifecycle.*
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

// todo: If i need to null check the dataBinding properties for the views.:
// android:text='@{item.title ?? ""}'          or              android:text='@{item.title != null ? user.title : ""}'

// maybe have different viewmodels: mainActivity, shoppingList, choresList
// Shared viewModel for MainActivity, ShoppingFragment, and ChoresFragment
class ListsViewModel (private val listsRepository: ListsRepository): ViewModel() {

    // DataBound Variables
    private val _listChosen = MutableLiveData<String>()      // Shopping or Chores. Use this to determine which list 'AddListItemFragment' is manipulating
    val listChosen: LiveData<String> = _listChosen
    // Both Shopping and Chores
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _neededBy = MutableLiveData<String>()
    val neededBy: LiveData<String> = _neededBy
    private val _priority = MutableLiveData<String>()
    val priority: LiveData<String> = _priority
    // Only Shopping
    private val _quantity = MutableLiveData<String>()   // convert to double in the code
    val quantity: LiveData<String> = _quantity
    private val _purchaseLocation = MutableLiveData<String>()
    val purchaseLocation: LiveData<String> = _purchaseLocation
    private val _cost = MutableLiveData<String>()   // convert to double in the code
    val cost: LiveData<String> = _cost
    // Only Chores
    private val _difficulty = MutableLiveData<String>()
    val difficulty: LiveData<String> = _difficulty

    // Complete Lists
    private val shoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val choresItems = MutableLiveData<List<ChoresItem>>()

    // Objects
    private val compositeDisposable = CompositeDisposable()     // to dispose of multiple disposables at once

    init {
        //fetchShoppingItems()  //maybe use this
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()    //Useful when ViewModel observes some data and you need to clear this subscription to prevent a memory leak of this ViewModel.
    }

    //todo: if Shopping list is chosen, hide only chores widgets, and show shopping widgets
    //todo: if Chores list is chosen, hide only shopping widgets, and show chores widgets

    // DATABASE QUERIES //
    //get items
    fun getShoppingItems(): LiveData<List<ShoppingItem>> { return shoppingItems }
    //insert item
    fun insert(choresItem: ChoresItem) = viewModelScope.launch {
        listsRepository.insertChoresItem(choresItem)
    }
    //delete item
    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
    }
    //other queries...

    // SETTERS //
    fun setListToDisplay(listType: String) {
        _listChosen.value = listType
    }
    fun setPriority(desiredPriority: String) {
        // might have to check if there is a flavor set, if so use the cupcake app as refference
        _priority.value = desiredPriority
    }
    fun setDifficulty(desiredDifficulty: String) {
        // might have to check if there is a flavor set, if so use the cupcake app as refference
        _difficulty.value = desiredDifficulty
    }

    // HELPER FUNCTIONS //
}

// ViewModelFactory exists to pass arguments to the viewModel. Bc arguments can't be passes to the viewModel directly
class ListsViewModelFactory(private val repository: ListsRepository): ViewModelProvider.Factory {

    lateinit var listsViewModel: ListsViewModel

    // returns a repository to the viewModel <OR> gives u an error if the model class doesn't show up
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
//            listsViewModel = ListsViewModel(ListsRepository(, ,apiHelper))
//            return listsViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
