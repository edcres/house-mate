package com.aldreduser.housemate.data.repository

import com.aldreduser.housemate.data.api.ApiHelper
import com.aldreduser.housemate.data.model.ShoppingItem
import io.reactivex.Single

class MainRepository(private val apiHelper: ApiHelper) {

    fun getShoppingItems(): Single<List<ShoppingItem>> {
        return apiHelper.getShoppingItems()
    }
}
