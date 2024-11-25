package com.app.studentmanagementsystem.models

data class User(
    val id: String,
    val email: String,
    var mobile: String,
    var name: String
)