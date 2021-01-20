package com.aldreduser.housemate.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.data.repository.MainRepository
import com.aldreduser.housemate.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel (private val mainRepository: MainRepository): ViewModel() {

    private val shoppingItems = MutableLiveData<Resource<List<ShoppingItem>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchShoppingItems()
    }

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

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getShoppingItems(): LiveData<Resource<List<ShoppingItem>>> {
        return shoppingItems
    }
}