package com.aldreduser.housemate.ui.main.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.ListsRepository

class ListsViewModelFactory(
    private val repository: ListsRepository,
    private val application: Application): ViewModelProvider.Factory {

    // returns a repository to the viewModel <OR> gives u an error if the model class doesn't show up
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            val listsViewModel = ListsViewModel(repository, application)
            return listsViewModel as T
        } else { throw IllegalArgumentException("Unknown class name") }
    }
}
