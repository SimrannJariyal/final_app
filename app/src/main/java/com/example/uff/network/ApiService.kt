package com.example.uff.network

import com.example.uff.models.Subject
import com.example.uff.models.Unit
import com.example.uff.models.LoginRequest
import com.example.uff.models.LoginResponse
import com.example.uff.models.RegisterRequest
import com.example.uff.models.User
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

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
    @POST("users/{id}/update_profile_photo/")
    suspend fun updateProfilePhoto(
        @Path("id") userId: Int,
        @Part profilePhoto: MultipartBody.Part
    ): User

}
