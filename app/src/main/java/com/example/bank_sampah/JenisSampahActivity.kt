package com.example.bank_sampah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_sampah.databinding.ActivityJenisSampahBinding

class JenisSampahActivity : AppCompatActivity() {
    private lateinit var b: ActivityJenisSampahBinding

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "setting"
    val JD_JNS = "judul_jns"
    val ITM_JNS = "item_jns"
    val DEF_JD_JNS = 20
    val DEF_ITM_JNS = 14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityJenisSampahBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        b.judul.textSize = preferences.getInt(JD_JNS, DEF_JD_JNS).toFloat()
        b.item1.textSize = preferences.getInt(ITM_JNS, DEF_ITM_JNS).toFloat()
        b.item2.textSize = preferences.getInt(ITM_JNS, DEF_ITM_JNS).toFloat()
        b.item3.textSize = preferences.getInt(ITM_JNS, DEF_ITM_JNS).toFloat()
        b.btnDokumentasi.textSize = preferences.getInt(ITM_JNS, DEF_ITM_JNS).toFloat()

        b.btnDokumentasi.setOnClickListener {
            startActivity(Intent(this, IklanActivity::class.java))
        }

        b.btnSampahPadat.setOnClickListener {
            val intent = Intent(this, DokumentasiSampahActivity::class.java)
            intent.putExtra("jns", "Padat")
            startActivity(intent)
        }

        b.btnSampahBasah.setOnClickListener {
            val intent = Intent(this, DokumentasiSampahActivity::class.java)
            intent.putExtra("jns", "Basah")
            startActivity(intent)
        }
    }
}