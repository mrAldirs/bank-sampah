package com.example.bank_sampah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_sampah.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var b: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        b.btnLogin.setOnClickListener {
            if (b.edtUsername.text.toString().equals("ananda") && b.edtPassword.text.toString().equals("ananda")) {
                startActivity(Intent(this, MainActivity::class.java))
                Toast.makeText(this, "Berhasil Login!", Toast.LENGTH_SHORT).show()
            } else if (b.edtUsername.text.toString().equals("") || b.edtPassword.text.toString().equals("")) {
                Toast.makeText(this, "Username atau Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Username & Password Anda Salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}