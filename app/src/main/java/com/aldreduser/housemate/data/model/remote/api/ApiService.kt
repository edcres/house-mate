package com.aldreduser.housemate.data.model.remote.api

import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.Single

// looks like the purpose of the API is to communicate with the remote database

// interface for ApiServiceImpl
// is also called by ApiHelper to get the remote data
interface ApiService {
    fun getShoppingItems(): Single<List<ShoppingItem>>
    // todo: add getChores() here
}