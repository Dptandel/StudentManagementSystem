package com.app.studentmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.studentmanagementsystem.databinding.ActivityRegisterBinding
import com.app.studentmanagementsystem.models.StudentApi
import com.app.studentmanagementsystem.preference.PrefManager
import com.app.studentmanagementsystem.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val mobile = binding.etMobile.text.toString().trim()
            registerStudent(name, email, mobile)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerStudent(name: String, email: String, mobile: String) {
        if (name.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if (mobile.length != 10) {
            Toast.makeText(this, "Please enter a valid 10-digit mobile number.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val apiService = RetrofitClient.getStudentService(this)
        val call = apiService.registerStudent(name, email, mobile)

        call.enqueue(object : Callback<StudentApi> {
            override fun onResponse(call: Call<StudentApi>, response: Response<StudentApi>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    val message = response.body()?.message

                    if (status == "success") {
                        val user = response.body()?.user
                        val prefManager = PrefManager.getInstance(this@RegisterActivity)
                        prefManager.setString("name", name)
                        prefManager.setString("email", email)
                        prefManager.setBoolean("isLoggedIn", true)

                        Toast.makeText(
                            this@RegisterActivity,
                            "Welcome, ${user?.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed: $message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "API Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<StudentApi>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Network Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

}