package com.example.gym_tok.api

import com.example.gym_tok.model.Gym
import com.example.gym_tok.model.User
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("user")
    suspend fun crearUsuario(@Body body: User)

    @GET("gym")
    suspend fun obtenerGym(): List<Gym>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("email") email: String,@Field("password") password: String): User?
}