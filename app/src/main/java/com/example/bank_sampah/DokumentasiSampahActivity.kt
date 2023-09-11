package com.example.bank_sampah

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.apk_pn.Helper.MediaHelper
import com.example.bank_sampah.databinding.ActivityDokumentasiSampahBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DokumentasiSampahActivity : AppCompatActivity() {
    private lateinit var b: ActivityDokumentasiSampahBinding

    val data = mutableListOf<HashMap<String,String>>()
    lateinit var adp : AdapterFoto

    lateinit var urlClass: UrlClass

    lateinit var mediaHealper: MediaHelper
    var imStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDokumentasiSampahBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mediaHealper = MediaHelper(this)
        urlClass = UrlClass()

        adp = AdapterFoto(data, this)
        b.rvDokumentasi.layoutManager = GridLayoutManager(this, 2)

        val spacingInPixels =
            resources.getDimensionPixelSize(R.dimen.spacing) // Mengambil nilai spacing dari dimens.xml

        val includeEdge = true // Atur true jika Anda ingin padding pada tepi luar grid
        val itemDecoration = GridSpacingItemDecoration(2, spacingInPixels, includeEdge)
        b.rvDokumentasi.addItemDecoration(itemDecoration)

        b.rvDokumentasi.adapter = adp

        var paket: Bundle? = intent.extras
        val jns = paket?.getString("jns").toString()
        if (jns.equals("Padat")) {
            b.judul.setText("Dokumentasi Sampah Padat")
            showData("padat")
        } else if (jns.equals("Basah")) {
            b.judul.setText("Dokumentasi Sampah Basah")
            showData("basah")
        }

        b.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, mediaHealper.RcGallery())
        }

        b.btnTambah.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Tambah Dokumentasi")
                .setMessage("Apakah Anda ingin menambah dokumentasi sampah baru?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    var paket: Bundle? = intent.extras
                    val jns = paket?.getString("jns").toString()
                    if (jns.equals("Padat")) {
                        crud("padat", "")
                    } else if (jns.equals("Basah")) {
                        crud("basah", "")
                    }
                    recreate()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .show()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == mediaHealper.RcGallery()){
                imStr = mediaHealper.getBitmapToString(data!!.data,b.insFoto)
            }
        }
    }

    fun crud(mode: String, id:String) {
        val request = object : StringRequest(
            Method.POST,urlClass.foto,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menambahkan foto dokumentasi!", Toast.LENGTH_SHORT).show()
                } else if (respon.equals("2")) {
                    Toast.makeText(this, "Berhasil menghapus foto dokumentasi!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                val nmFile ="IMG"+ SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(
                    Date()
                )+".jpg"
                when(mode){
                    "padat" -> {
                        hm.put("mode","insert")
                        hm.put("jenis", "Sampah Padat")
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                    "basah" -> {
                        hm.put("mode","insert")
                        hm.put("jenis", "Sampah Basah")
                        hm.put("image",imStr)
                        hm.put("file",nmFile)
                    }
                    "delete" -> {
                        hm.put("mode","delete")
                        hm.put("id", id)
                    }
                }

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun showData(mode: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.foto,
            Response.Listener { response ->
                data.clear()
                if (response.equals(0)) {
                    Toast.makeText(this,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                } else {
                    val jsonArray = JSONArray(response)
                    for (x in 0..(jsonArray.length()-1)){
                        val jsonObject = jsonArray.getJSONObject(x)
                        var  frm = HashMap<String,String>()
                        frm.put("foto",jsonObject.getString("foto"))
                        frm.put("id",jsonObject.getString("id"))
                        frm.put("jenis",jsonObject.getString("jenis"))

                        data.add(frm)
                    }
                    adp.notifyDataSetChanged()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "padat" -> {
                        hm.put("mode","show_data_sampah")
                        hm.put("jenis", "Sampah Padat")
                    }
                    "basah" -> {
                        hm.put("mode","show_data_sampah")
                        hm.put("jenis", "Sampah Basah")
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Mendapatkan posisi item
            val column = position % spanCount // Mendapatkan kolom saat ini

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 2) * spacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 2) * spacing / spanCount

                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}