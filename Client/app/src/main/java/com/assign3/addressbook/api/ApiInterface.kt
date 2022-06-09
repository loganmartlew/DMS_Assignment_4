package com.assign3.addressbook.api

import com.assign3.addressbook.models.Location
import com.assign3.addressbook.models.Request
import com.assign3.addressbook.models.User
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{name}")
    fun getUser(@Path("name") name: String): Call<User>

    @POST("locations")
    fun createLocation(@Body location: LocationDTO): Call<Void>

    @GET("users/{name}/outgoing")
    fun getOutgoingRequests(@Path("name") name: String): Call<List<Request>>

    @GET("users/{name}/incoming")
    fun getIncomingRequests(@Path("name") name: String): Call<List<Request>>

    @POST("requests")
    fun createRequest(@Body request: RequestDTO): Call<Void>

    @PATCH("requests/{id}/accept")
    fun acceptRequest(@Path("id") id: Int): Call<Void>

    @PATCH("requests/{id}/deny")
    fun denyRequest(@Path("id") id: Int): Call<Void>

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