package com.example.bank_sampah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bank_sampah.databinding.ActivityFontBinding

class FontActivity : AppCompatActivity() {
    private lateinit var b: ActivityFontBinding

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "setting"
    val JD_DSH = "judul_dsh"
    val ITM_DSH = "item_dsh"
    val DEF_JD_DSH = 24
    val DEF_ITM_DSH = 12

    val JD_RWT = "judul_rwt"
    val ITM_RWT = "item_rwt"
    val DEF_JD_RWT = 24
    val DEF_ITM_RWT = 12

    val JD_JNS = "judul_jns"
    val ITM_JNS = "item_jns"
    val DEF_JD_JNS = 24
    val DEF_ITM_JNS = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFontBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val onSeek = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (p0 == b.sbUkuranTitle) {
                    b.contohTitle.textSize = progress.toFloat()
                } else if (p0 == b.sbUkuranTitleRwt) {
                    b.contohTitleRwt.textSize = progress.toFloat()
                } else if (p0 == b.sbUkuranTitleJenis) {
                    b.contohTitleJenis.textSize = progress.toFloat()
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        }

        val onSeekItem = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (p0 == b.sbUkuranDetail) {
                    b.contohItem.textSize = progress.toFloat()
                } else if (p0 == b.sbUkuranDetailRwt) {
                    b.contohItemRwt.textSize = progress.toFloat()
                } else if (p0 == b.sbUkuranDetailJenis) {
                    b.contohItemJenis.textSize = progress.toFloat()
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        }

        // dashboard
        b.contohTitle.textSize = preferences.getInt(JD_DSH, DEF_JD_DSH).toFloat()
        b.sbUkuranTitle.setOnSeekBarChangeListener(onSeek)
        b.contohItem.textSize = preferences.getInt(ITM_DSH, DEF_ITM_DSH).toFloat()
        b.sbUkuranDetail.setOnSeekBarChangeListener(onSeekItem)

        // riwayat
        b.contohTitleRwt.textSize = preferences.getInt(JD_RWT, DEF_JD_RWT).toFloat()
        b.sbUkuranTitleRwt.setOnSeekBarChangeListener(onSeek)
        b.contohItemRwt.textSize = preferences.getInt(ITM_RWT, DEF_ITM_RWT).toFloat()
        b.sbUkuranDetailRwt.setOnSeekBarChangeListener(onSeekItem)

        // dashboard
        b.contohTitleJenis.textSize = preferences.getInt(JD_JNS, DEF_JD_JNS).toFloat()
        b.sbUkuranTitleJenis.setOnSeekBarChangeListener(onSeek)
        b.contohItemJenis.textSize = preferences.getInt(ITM_JNS, DEF_ITM_JNS).toFloat()
        b.sbUkuranDetailJenis.setOnSeekBarChangeListener(onSeekItem)

        b.btnSetting.setOnClickListener {
            val prefEditor = preferences.edit()
            prefEditor.putInt(JD_DSH, b.sbUkuranTitle.progress)
            prefEditor.putInt(ITM_DSH, b.sbUkuranDetail.progress)
            prefEditor.putInt(JD_RWT, b.sbUkuranTitleRwt.progress)
            prefEditor.putInt(ITM_RWT, b.sbUkuranDetailRwt.progress)
            prefEditor.putInt(JD_JNS, b.sbUkuranTitleJenis.progress)
            prefEditor.putInt(ITM_JNS, b.sbUkuranDetailJenis.progress)
            prefEditor.commit()

            val intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "Perubahan Telah Disimpan", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

        b.btnDefault.setOnClickListener {
            val prefEditor = preferences.edit()
            prefEditor.remove(JD_DSH)
            prefEditor.remove(ITM_DSH)
            prefEditor.remove(JD_RWT)
            prefEditor.remove(ITM_RWT)
            prefEditor.remove(JD_JNS)
            prefEditor.remove(ITM_JNS)
            prefEditor.commit()

            val intent = Intent(this, MainActivity::class.java)
            Toast.makeText(this, "Perubahan Telah Disimpan", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}