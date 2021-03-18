package com.aldreduser.housemate.ui.main.viewmodels.lists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// shoppingList has only remote viewModel for now. Chores has local
// gets data from the repo
// can't pass parameters to viewModel by default, therefore use a viewModel factory
class ShoppingListViewModel (private val listsRepository: ListsRepository): ViewModel() {

    private val shoppingItems = MutableLiveData<Resource<List<ShoppingItem>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        //fetchShoppingItems()  todo: uncomment this while implementing remote database
    }

    //todo work on this when doing remote database
    // -remember will only be talking to the repository (which should already have local and remote data synchronized)
    // gets data from the repo
    private fun fetchShoppingItems() {
//        shoppingItems.postValue(Resource.loading(null))
//        compositeDisposable.add(
//            listsRepository.getShoppingItems()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({shoppingItemList ->
//                    shoppingItems.postValue(Resource.success(shoppingItemList))
//                }, {throwable ->
//                    shoppingItems.postValue(Resource.error("Something Went Wrong.", null))
//                })
//        )
    }

    // This method will be called when this ViewModel is no longer used and will be destroyed.
    // It is useful when ViewModel observes some data and you need to clear this subscription to prevent a memory leak of this ViewModel.
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    // will be called by other classes
    fun getShoppingItems(): LiveData<Resource<List<ShoppingItem>>> {
        return shoppingItems
    }
}

// ViewModelFactory exists to pass arguments to the viewModel. Bc arguments can't be passes to the viewModel directly
// this returns a repository to the viewModel <OR> gives u an error if the model class doesn't show up
class ShoppingListViewModelFactory(private val repository: ListsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
//            return ShoppingListViewModel(ListsRepository(, ,apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}






/* basic main viewmodel from raywonderlich
class BasicMainViewModel(private val repository: Repository) : ViewModel() {
  val uiModel: MutableLiveData<UiModel> by lazy {
    MutableLiveData< UiModel >()
  }

  fun generateReport (){
    // TO DO Add complicated report computation
  }

  fun deleteUser(user: User?) {
    deleteUserFromRepository(user)
    uiModel.value?.userList?.remove(user)
  }

  fun deleteUserFromRepository(user: User?) {
    if(user != null) { repository.delete(user) }
  }
}

 */