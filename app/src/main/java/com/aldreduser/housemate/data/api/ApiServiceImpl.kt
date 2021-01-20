package com.aldreduser.housemate.data.api

import com.aldreduser.housemate.data.model.ShoppingItem
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

// todo: this is where i get the data from the remote database,
//  rn its a random website from a tutorial change it.

class ApiServiceImpl : ApiService {

    override fun getShoppingItems(): Single<List<ShoppingItem>> {
        return Rx2AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
            .build()
            .getObjectListSingle(ShoppingItem::class.java)
    }

}
