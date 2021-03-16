package com.aldreduser.housemate.ui.main.viewmodels.lists

import androidx.lifecycle.*
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.util.Resource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class ChoresListViewModel (private val listsRepository: ListsRepository) : ViewModel() {

    private val choreItems = MutableLiveData<Resource<List<ChoresItem>>>()
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
    fun getChoreItems(): LiveData<Resource<List<ChoresItem>>> {
        return choreItems
    }
}