package com.aldreduser.housemate.data.api

import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.Single

// Network Layer

interface ApiService {
    fun getShoppingItems(): Single<List<ShoppingItem>>
    // todo: add getChores() here
}