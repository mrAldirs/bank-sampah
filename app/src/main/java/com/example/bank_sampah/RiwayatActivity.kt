package com.example.bank_sampah

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bank_sampah.databinding.ActivityRiwayatBinding
import org.json.JSONArray
import org.json.JSONObject

class RiwayatActivity : AppCompatActivity() {
    private lateinit var b: ActivityRiwayatBinding
    lateinit var urlClass: UrlClass

    val data = mutableListOf<HashMap<String,String>>()
    lateinit var adapter: AdapterRiwayat

    lateinit var preferences: SharedPreferences
    val PREF_NAME = "setting"
    val JD_RWT = "judul_rwt"
    val ITM_RWT = "item_rwt"
    val DEF_JD_RWT = 20
    val DEF_ITM_RWT = 14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()
        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        b.judul.textSize = preferences.getInt(JD_RWT, DEF_JD_RWT).toFloat()
        b.judul2.textSize = preferences.getInt(JD_RWT, DEF_JD_RWT).toFloat()
        b.tvSaldo.textSize = preferences.getInt(ITM_RWT, DEF_ITM_RWT).toFloat()
        b.textCari.textSize = preferences.getInt(ITM_RWT, DEF_ITM_RWT).toFloat()

        adapter = AdapterRiwayat(data, this)
        b.rvHistory.layoutManager = LinearLayoutManager(this)
        b.rvHistory.adapter = adapter

        b.btnCari.setOnClickListener {
            showData("show_data_riwayat", b.textCari.text.toString().trim())
        }

        showData("show_data_riwayat", "")

        saldo()
    }

    private fun showData(mode: String, nama: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.show,
            Response.Listener { response ->
                data.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    var  frm = HashMap<String,String>()
                    frm.put("id_sampah",jsonObject.getString("id_sampah"))
                    frm.put("nama",jsonObject.getString("nama"))
                    frm.put("harga",jsonObject.getString("harga"))
                    frm.put("kategori",jsonObject.getString("kategori"))
                    frm.put("berat",jsonObject.getString("berat"))
                    frm.put("tgl_penjemputan",jsonObject.getString("tgl_penjemputan"))
                    frm.put("status_bayar",jsonObject.getString("status_bayar"))

                    data.add(frm)
                }
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                when(mode){
                    "show_data_riwayat" -> {
                        hm.put("mode","show_data_riwayat")
                        hm.put("nama", nama)
                    }
                }

                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun saldo() {
        val request = object : StringRequest(
            Method.POST,urlClass.show,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val st1 = jsonObject.getString("saldo")

                b.tvSaldo.setText("Rp. "+st1)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "saldo")

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    fun hapus(id: String) {
        val request = object : StringRequest(
            Method.POST,urlClass.crud,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    Toast.makeText(this, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show()
                    recreate()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = java.util.HashMap<String, String>()
                hm.put("mode", "delete")
                hm.put("id_sampah", id)

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}