package ru.iabarmin.todoapp.remote

import ru.iabarmin.todoapp.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitInterface {

    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<User>

    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void>

    @POST("/forget")
    fun executeForget(@Body map: HashMap<String, String>): Call<Void>

    @POST("/change")
    fun executeChange(@Body map: HashMap<String, String>): Call<Void>

    @POST("/syncTasks")
    fun executeSyncTasks(@Body map: HashMap<String, String>): Call<Void>

}