package com.app.studentmanagementsystem.retrofit

import com.app.studentmanagementsystem.models.StudentApi
import com.app.studentmanagementsystem.models.User
import com.app.studentmanagementsystem.models.Users
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

    @POST("student.php")
    @FormUrlEncoded
    fun getAllStudents(
        @Field("flag") flag: Int = 4
    ): Call<Users>

    @POST("student.php")
    @FormUrlEncoded
    fun updateStudent(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("mobile") contact: String,
        @Field("flag") flag: Int = 2
    ): Call<StudentApi>

    @POST("student.php")
    @FormUrlEncoded
    fun deleteStudent(
        @Field("id") id: String,
        @Field("flag") flag: Int = 3
    ): Call<StudentApi>
}