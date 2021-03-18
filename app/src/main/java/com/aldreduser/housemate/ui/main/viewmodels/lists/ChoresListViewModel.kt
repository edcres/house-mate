package com.aldreduser.housemate.ui.main.viewmodels.lists

import androidx.lifecycle.*
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import com.aldreduser.housemate.data.model.ShoppingItem
import com.aldreduser.housemate.util.Resource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

// chores has only local viewModel for now. Shopping list has remote
class ChoresListViewModel (private val listsRepository: ListsRepository) : ViewModel() {

    // get all chore items
    val allChores: LiveData<List<ChoresItem>> = listsRepository.allChoreItems.asLiveData()
    //insert
    fun insert(choresItem: ChoresItem) = viewModelScope.launch {
        listsRepository.insertChoresItem(choresItem)
    }

}

// pass a repository to the corresponding viewModel
class ChoresListViewModelFactory(private val repository: ListsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChoresListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChoresListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}