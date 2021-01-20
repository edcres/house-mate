package com.aldreduser.housemate.data.api

class ApiHelper (private val apiService: ApiService) {

    fun getShoppingItems() = apiService.getShoppingItems()
}
