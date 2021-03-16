package com.aldreduser.housemate.ui.main.viewmodels.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.model.remote.api.ApiHelper
import com.aldreduser.housemate.data.ListsRepository

// ViewModelFactory exists to pass arguments to the viewModel. Bc arguments can't be passes to the viewModel directly
// this returns a repository to the viewModel <OR> gives u an error if the model class doesn't show up
class ShoppingListViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            //return ShoppingListViewModel(ListsRepository(, ,apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}