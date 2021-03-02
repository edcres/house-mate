package com.aldreduser.housemate.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aldreduser.housemate.data.ListsRepository
import com.aldreduser.housemate.data.model.ChoresItem
import kotlinx.coroutines.launch

class ChoresListViewModel (private val listsRepository: ListsRepository) : ViewModel() {

    // Use LiveData to cache what allWords returns
    //  -then put an observer on the data and only update the the UI when the data actually changes.
    val allChoreItems: LiveData<List<ChoresItem>> = listsRepository.allChoreItems.asLiveData()

    // Launching a new coroutine to insert the data in a non-blocking way
    fun insert(item: ChoresItem) = viewModelScope.launch {
        listsRepository.insertChoresItem(item)
    }

}