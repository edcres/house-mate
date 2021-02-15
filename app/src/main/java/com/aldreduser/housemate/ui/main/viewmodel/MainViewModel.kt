package com.aldreduser.housemate.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.MainRepository
import com.aldreduser.housemate.util.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// gets data from the repo
// can't pass parameters to viewModel by default, therefore use a viewModel factory
class MainViewModel (private val mainRepository: MainRepository): ViewModel() {

    private val shoppingItems = MutableLiveData<Resource<List<ShoppingItem>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchShoppingItems()
    }

    // gets data from the repo
    private fun fetchShoppingItems() {
        shoppingItems.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getShoppingItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({shoppingItemList ->
                    shoppingItems.postValue(Resource.success(shoppingItemList))
                }, {throwable ->
                    shoppingItems.postValue(Resource.error("Something Went Wrong.", null))
                })
        )
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