package com.example.gym_tok.api

import com.example.gym_tok.DTO.CommentRequest
import com.example.gym_tok.model.Comment
import com.example.gym_tok.model.Gym
import com.example.gym_tok.network.LoginRequest
import com.example.gym_tok.network.RegisterRequest
import com.example.gym_tok.network.UserDTO
import com.example.gym_tok.network.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        // --- ¡IMPORTANTE! ---
        // Reemplaza esta IP con la dirección IP local de la máquina
        // donde corre tu servidor backend.
        const val BASE_URL = "http://10.0.2.2:8080"
    }

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<UserDTO>

    @POST("user/register")
    suspend fun register(@Body request: RegisterRequest): Response<Map<String, String>>

    @GET("posts")
    suspend fun getPosts(@Query("userId") userId: Int?): Response<List<PostResponse>>

    @GET("gym")
    suspend fun getGyms(): Response<List<Gym>>

    @Multipart
    @POST("posts")
    suspend fun createPost(
        @Part("userName") userName: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Unit>

    @POST("posts/{postId}/like")
    suspend fun toggleLike(
        @Path("postId") postId: Long,
        @Query("userId") userId: Int
    ): Response<Unit>

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Long): Response<List<Comment>>


    @POST("posts/{postId}/comments")
    suspend fun createComment(
        @Path("postId") postId: Long,
        @Body commentRequest: CommentRequest
    ): Response<Comment>

    @DELETE("posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): Response<Unit>

    // En ApiService.kt

    @GET("posts/{postId}")
    suspend fun getPostById(@Path("postId") postId: Long, @Query("userId") userId: Int?): Response<PostResponse>

    @FormUrlEncoded
    @PUT("posts/{postId}")
    suspend fun updatePost(
        @Path("postId") postId: Long,
        @Field("description") description: String,
        @Field("deleteImage") deleteImage: Boolean
    ): Response<Unit>
}
