package com.ucfjoe.teamplayers.common


sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    //data class Loading<out T : Any>(val data:T? = null, val message:String?=null):Resource<T>()
}