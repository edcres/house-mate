package com.aldreduser.housemate.util

//todo: maybe delete this file

//// When using this class in another file, make sure u're using the correct import.
//
//// ojo i think this Resource file is used for live data.
//// a utility class that will be responsible to communicate the current state of Network Call to the UI Layer
//data class Resource<out T> (val status: Status, val data: T?, val message: String?) {
//
//    companion object{
//
//        fun <T> success(data: T?): Resource<T> {
//            return Resource(Status.SUCCESS, data, null)
//        }
//
//        fun <T> error(msg: String, data: T?): Resource<T> {
//            return Resource(Status.ERROR, data, msg)
//        }
//
//        fun <T> loading(data: T?): Resource<T> {
//            return Resource(Status.LOADING, data, null)
//        }
//    }
//}