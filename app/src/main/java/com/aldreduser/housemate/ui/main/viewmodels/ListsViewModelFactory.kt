package com.aldreduser.housemate.ui.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.ListsRepository

class ListsViewModelFactory(private val repository: ListsRepository): ViewModelProvider.Factory {

    lateinit var listsViewModel: ListsViewModel

    // returns a repository to the viewModel <OR> gives u an error if the model class doesn't show up
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
//            listsViewModel = ListsViewModel(ListsRepository())
            return listsViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}
