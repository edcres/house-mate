package com.aldreduser.housemate.ui.main.viewmodels.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.ListsRepository

// pass a repository to the corresponding viewModel
class ChoresListViewModelFactory(private val repository: ListsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoresListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChoresListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") //this might need to be in an else statement
    }
}