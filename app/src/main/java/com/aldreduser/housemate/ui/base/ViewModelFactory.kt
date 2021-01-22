package com.aldreduser.housemate.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aldreduser.housemate.data.api.ApiHelper
import com.aldreduser.housemate.data.repository.MainRepository
import com.aldreduser.housemate.ui.main.viewmodel.MainViewModel

// i think this gives u an error if the model class doesn't show up
class ViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}