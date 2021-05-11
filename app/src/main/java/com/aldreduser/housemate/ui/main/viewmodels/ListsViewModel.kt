package com.aldreduser.housemate.ui.main.viewmodels

import androidx.lifecycle.*
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

// todo: rn the AddListItemFragment is only for shopping items, make it for chores items also and create the dataBound variables

// maybe have different viewmodels: mainActivity, shoppingList, choresList
// shared viewModel for MainActivity, ShoppingFragment, and ChoresFragment
class ListsViewModel (private val listsRepository: ListsRepository): ViewModel() {

    // DataBound Variables
    private val _listChosen = MutableLiveData<String>()      // Shopping or Chores. Use this to determine which list 'AddListItemFragment' is manipulating
    val listChosen: LiveData<String> = _listChosen
    private val _quantity = MutableLiveData<Double>()
    val quantity: LiveData<Double> = _quantity


    private val shoppingItems = MutableLiveData<List<ShoppingItem>>()

    // Objects
    private val compositeDisposable = CompositeDisposable()     // to dispose of multiple disposables at once

    init {
        //fetchShoppingItems()  //maybe use this
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()    //Useful when ViewModel observes some data and you need to clear this subscription to prevent a memory leak of this ViewModel.
    }

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
