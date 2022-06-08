package com.assign3.addressbook.api

import com.assign3.addressbook.models.Location
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{name}")
    fun getUser(@Path("name") name: String): Call<User>

    @POST("locations")
    fun createLocation(@Body location: LocationDTO): Call<Void>

    companion object {
        var BASE_URL = "http://192.168.1.220:8080/addressbook/api/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}