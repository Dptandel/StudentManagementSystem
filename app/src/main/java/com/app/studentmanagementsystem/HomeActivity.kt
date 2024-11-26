package com.app.studentmanagementsystem

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.studentmanagementsystem.adapters.StudentAdapter
import com.app.studentmanagementsystem.databinding.ActivityHomeBinding
import com.app.studentmanagementsystem.databinding.UpdateDialogLayoutBinding
import com.app.studentmanagementsystem.models.StudentApi
import com.app.studentmanagementsystem.models.User
import com.app.studentmanagementsystem.models.Users
import com.app.studentmanagementsystem.preference.PrefManager
import com.app.studentmanagementsystem.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = RetrofitClient.getStudentService(this)
        val call = apiService.getAllStudents()

        call.enqueue(object : Callback<Users> {
            override fun onResponse(
                call: Call<Users>,
                response: Response<Users>
            ) {
                if (response.isSuccessful) {
                    val studentList = response.body()!!
                    studentAdapter = StudentAdapter(
                        this@HomeActivity,
                        studentList,
                        onUpdate = { user ->
                            showUpdateDialog(user)
                        },
                        onDelete = { id ->
                            showDeleteDialog(id)
                        }
                    )

                    binding.rvStudent.layoutManager = LinearLayoutManager(applicationContext)
                    binding.rvStudent.adapter = studentAdapter

                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "API Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                val errorMessage = t.localizedMessage ?: "Unknown Error"
                Toast.makeText(this@HomeActivity, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            }

        })

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun showUpdateDialog(user: User) {

        val updateDialogBinding = UpdateDialogLayoutBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this)

        updateDialogBinding.tvEmail.text = user.email
        updateDialogBinding.edtName.setText(user.name)
        updateDialogBinding.edtMobile.setText(user.mobile)

        builder.setView(updateDialogBinding.root)

        val dialog = builder.create()

        updateDialogBinding.btnUpdate.setOnClickListener {
            val updatedName = updateDialogBinding.edtName.text.trim().toString()
            val updatedMobile = updateDialogBinding.edtMobile.text.trim().toString()

            if (updatedName.isNotEmpty() && updatedMobile.isNotEmpty()) {
                val apiService = RetrofitClient.getStudentService(this)
                val call = apiService.updateStudent(user.id, updatedName, updatedMobile)

                call.enqueue(object : Callback<StudentApi> {
                    override fun onResponse(
                        call: Call<StudentApi>,
                        response: Response<StudentApi>
                    ) {
                        if (response.isSuccessful) {
                            val status = response.body()?.status
                            val message = response.body()?.message

                            if (status == "success") {
                                Toast.makeText(
                                    applicationContext,
                                    "$message",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                // Update the student in the adapter
                                user.name = updatedName
                                user.mobile = updatedMobile
                                val position = studentAdapter.getPosition(user.id)
                                if (position != -1) {
                                    studentAdapter.updateStudent(position, user)
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Update failed: $message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            Toast.makeText(
                                applicationContext,
                                "API Error: ${response.message()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<StudentApi>, t: Throwable) {
                        Toast.makeText(
                            applicationContext,
                            "Network Error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please fill all fields.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            dialog.dismiss()
        }

        updateDialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    private fun showDeleteDialog(id: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Delete Student")
            setMessage("Are you sure you want to delete ?")
        }.setPositiveButton("Delete") { _, _ ->
            val apiService = RetrofitClient.getStudentService(this)
            val call = apiService.deleteStudent(id)

            call.enqueue(object : Callback<StudentApi> {
                override fun onResponse(
                    call: Call<StudentApi>,
                    response: Response<StudentApi>
                ) {
                    if (response.isSuccessful) {
                        val status = response.body()?.status
                        val message = response.body()?.message

                        if (status == "success") {
                            Toast.makeText(
                                applicationContext,
                                "$message",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            // Remove the student from the adapter
                            val position = studentAdapter.getPosition(id)
                            if (position != -1) {
                                studentAdapter.removeStudent(position)
                            }
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Delete failed: $message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "API Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<StudentApi>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        "Network Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun logout() {
        val prefManager = PrefManager.getInstance(this)
        prefManager.clear()

        Toast.makeText(this, "Logout Successfully!!!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}