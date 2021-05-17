package com.aldreduser.housemate.data.model.remote.api

import com.aldreduser.housemate.data.model.ShoppingItem
//import com.rx2androidnetworking.Rx2AndroidNetworking
//import io.reactivex.Single

// todo: this is where i get the data from the remote database,
//  rn its a random website from a tutorial, change it.
// i think i can use this class to get chores data, but might need to make another API class and rename this one

class ApiServiceImpl : ApiService {

    // i think that this gets the users and all the data(id, name, avatar, email) from the user
    override fun getShoppingItems() /*: Single<List<ShoppingItem>>*/ {
//        return Rx2AndroidNetworking.get("https://5e510330f2c0d300147c034c.mockapi.io/users")
//            .build()
//            .getObjectListSingle(ShoppingItem::class.java)
    }
    // todo: when adding the getChoreItems, add it to the Api Service interface first
}
