package com.example.bank_sampah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.bank_sampah.databinding.ActivityMainBinding
import com.example.bank_sampah.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private lateinit var hb: ContentMainBinding

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "setting"
    val JD_DSH = "judul_dsh"
    val ITM_DSH = "item_dsh"
    val DEF_JD_DSH = 24
    val DEF_ITM_DSH = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        hb = ContentMainBinding.bind(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        b.judul.textSize = preferences.getInt(JD_DSH, 26).toFloat()
        b.item.textSize = preferences.getInt(ITM_DSH, 16).toFloat()
        b.judul2.textSize = preferences.getInt(JD_DSH, 18).toFloat()
        b.item2.textSize = preferences.getInt(ITM_DSH, 12).toFloat()
        b.judul3.textSize = preferences.getInt(JD_DSH, 20).toFloat()
        hb.item3.textSize = preferences.getInt(ITM_DSH, 14).toFloat()
        hb.item4.textSize = preferences.getInt(ITM_DSH, 14).toFloat()
        hb.item5.textSize = preferences.getInt(ITM_DSH, 14).toFloat()

        val mediaController = MediaController(this)
        mediaController.setAnchorView(b.videoView)

        val videoPath = "android.resource://" + packageName + "/" + R.raw.iklan
        b.videoView.setVideoURI(Uri.parse(videoPath))
        b.videoView.start()

        b.videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.setVolume(0f, 0f)
            mediaPlayer.isLooping = true
        }

        hb.cvInput.setOnClickListener {
            startActivity(Intent(this, InputDataActivity::class.java))
        }

        hb.cvHistory.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }

        hb.cvKategori.setOnClickListener {
            startActivity(Intent(this, JenisSampahActivity::class.java))
        }

        b.btnFont.setOnClickListener {
            startActivity(Intent(this, FontActivity::class.java))
        }
    }
}