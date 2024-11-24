package com.app.studentmanagementsystem.retrofit

import com.app.studentmanagementsystem.models.StudentApi
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("student.php")
    @FormUrlEncoded
    fun registerStudent(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("mobile") contact: String,
        @Field("flag") flag: Int = 1
    ): Call<StudentApi>

    @POST("student.php")
    @FormUrlEncoded
    fun loginStudent(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("flag") flag: Int = 6
    ): Call<StudentApi>
}