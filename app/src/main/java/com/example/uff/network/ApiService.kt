package com.example.uff.network

import com.example.uff.models.Subject
import com.example.uff.models.Unit
import com.example.uff.models.LoginRequest
import com.example.uff.models.LoginResponse
import com.example.uff.models.RegisterRequest
import com.example.uff.models.Task
import com.example.uff.models.User
import okhttp3.MultipartBody

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("register/")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse

    @POST("login/")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("subjects/")
    suspend fun getSubjects(): List<Subject>

    @GET("subjects/{id}/units/")
    suspend fun getUnitsBySubject(@Path("id") subjectId: Int): List<Unit>

    @GET("users/{id}/")
    suspend fun getUserProfile(@Path("id") userId: Int): User

    @Multipart
    @PUT("users/{id}/update-profile-photo/")
    suspend fun updateProfilePhoto(
        @Path("id") userId: Int,
        @Part profilePhoto: MultipartBody.Part
    ): Response<Map<String, String>>  // Corrected line


    //    task
    @GET("tasks/")
    suspend fun getTasksByUser(@Query("user_id") userId: Int): Response<List<Task>>

    @POST("tasks/")
    suspend fun createTaskForUser(@Body task: Task): Response<Task>

    @PUT("tasks/{id}/")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body updatedFields: Task
    ): Response<Task>

    @DELETE("tasks/{id}/")
    suspend fun deleteTask(@Path("id") id: Int): Response<Void>

    }
