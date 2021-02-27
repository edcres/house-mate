package com.aldreduser.housemate.data.model.remote.api

// is called from MainRepository
// calls ApiService to get the data
class ApiHelper (private val apiService: ApiService) {

    fun getShoppingItems() = apiService.getShoppingItems()
}
