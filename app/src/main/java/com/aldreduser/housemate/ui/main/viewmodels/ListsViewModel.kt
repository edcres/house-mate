package com.aldreduser.housemate.ui.main.viewmodels

import androidx.lifecycle.*
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

// maybe have diferent viewmodels: mainActivity, shoppingList, choresList
// shared viewModel for MainActivity, ShoppingFragment, and ChoresFragment
class ListsViewModel (private val listsRepository: ListsRepository): ViewModel() {

    private val shoppingItems = MutableLiveData<List<ShoppingItem>>()
    private val compositeDisposable = CompositeDisposable()     // to dispose of multiple disposables at once

    init {
        //fetchShoppingItems()  //maybe use this
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()    //Useful when ViewModel observes some data and you need to clear this subscription to prevent a memory leak of this ViewModel.
    }

    // QUERIES //
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
