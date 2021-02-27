package com.aldreduser.housemate.data

import com.aldreduser.housemate.data.model.remote.api.ApiHelper
import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.Single

// todo: use this class when making local storage

// get items from storage. Rn it's only from remote storage
class Repository(private val apiHelper: ApiHelper) {

    fun getShoppingItems(): Single<List<ShoppingItem>> {
        return apiHelper.getShoppingItems()
    }
}
