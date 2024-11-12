package com.example.uff.network

import com.example.uff.models.Subject
import com.example.uff.models.Unit
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("subjects/")
    suspend fun getSubjects(): List<Subject>

    @GET("subjects/{id}/units/")
    suspend fun getUnitsBySubject(@Path("id") subjectId: Int): List<Unit>
}
