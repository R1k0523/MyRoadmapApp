package ru.boringowl.myroadmapapp.data.network.dto

import retrofit2.HttpException

fun HttpException.errorText(): String {
    val body = response()?.errorBody()?.string()
    return if (code() in 400..401 && body != null)
        body.toString()
    else
        "Произошла ошибка. Попробуйте еще раз"
}