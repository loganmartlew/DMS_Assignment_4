package com.assign3.addressbook.api

import com.assign3.addressbook.models.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{name}")
    suspend fun getUser(@Path("name") name: String): User

    companion object {
        var BASE_URL = "http://localhost:8080/addressbook/api/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}