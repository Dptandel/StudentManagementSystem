package com.app.studentmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.studentmanagementsystem.databinding.ActivityLoginBinding
import com.app.studentmanagementsystem.models.StudentApi
import com.app.studentmanagementsystem.preference.PrefManager
import com.app.studentmanagementsystem.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginStudent(email, password)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginStudent(email: String, password: String) {
        val apiService = RetrofitClient.getStudentService(this)
        val call = apiService.loginStudent(email, password)

        call.enqueue(object : Callback<StudentApi> {
            override fun onResponse(call: Call<StudentApi>, response: Response<StudentApi>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message
                    if (status == "success") {
                        val user = response.body()?.user
                        val prefManager = PrefManager.getInstance(this@LoginActivity)
                        prefManager.setBoolean("isLoggedIn", true)
                        prefManager.setString("email", email)

                        Toast.makeText(
                            this@LoginActivity,
                            "Welcome, ${user?.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "API Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<StudentApi>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}