package com.example.bank_sampah

import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_sampah.databinding.ActivityIklanBinding

class IklanActivity : AppCompatActivity() {
    private lateinit var b: ActivityIklanBinding

    private lateinit var mediaController: MediaController
    private lateinit var sharedPreferences: SharedPreferences
    private var currentPosition = 0
    private var isFullscreen = false

    private val PREF_VIDEO_POSITION = "pref_video_position"
    private val PREF_FULLSCREEN = "pref_fullscreen"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityIklanBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mediaController = MediaController(this)
        b.videoView.setMediaController(mediaController)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        currentPosition = sharedPreferences.getInt(PREF_VIDEO_POSITION, 0)

        isFullscreen = sharedPreferences.getBoolean(PREF_FULLSCREEN, false)
        if (isFullscreen) {
            supportActionBar?.hide()
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        }

        val videoPath = "android.resource://" + packageName + "/" + R.raw.iklan
        b.videoView.setVideoURI(Uri.parse(videoPath))

        b.videoView.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        b.videoView.seekTo(currentPosition)
        b.videoView.start()
    }

    override fun onPause() {
        super.onPause()
        b.videoView.pause()
        currentPosition = b.videoView.currentPosition

        val editor = sharedPreferences.edit()
        editor.putInt(PREF_VIDEO_POSITION, currentPosition)
        editor.apply()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullscreen = true
            supportActionBar?.hide()
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isFullscreen = false
            supportActionBar?.show()
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }

        val editor = sharedPreferences.edit()
        editor.putBoolean(PREF_FULLSCREEN, isFullscreen)
        editor.apply()
    }

}