package com.app.studentmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.studentmanagementsystem.databinding.ActivityHomeBinding
import com.app.studentmanagementsystem.preference.PrefManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val prefManager = PrefManager.getInstance(this)
        prefManager.clear()

        Toast.makeText(this, "Logout Successfully!!!", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}