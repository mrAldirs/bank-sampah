package com.example.bank_sampah

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bank_sampah.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityDetailBinding
    lateinit var urlClass : UrlClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()

        b.btnBayar.setOnClickListener {
            var paket : Bundle? = intent.extras
            val dialog = PembayaranFragment()

            val bundle = Bundle()
            bundle.putString("id", paket?.getString("id").toString())
            dialog.arguments = bundle

            dialog.show(supportFragmentManager, "KonfirmasiFragment")
        }

        detail()

        b.btnEdit.setOnClickListener {
            var paket : Bundle? = intent.extras
            val intent = Intent(this, EditDataActivity::class.java)
            intent.putExtra("id", paket?.getString("id").toString())
            startActivity(intent)
        }

        b.btnLokasi.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("alm", b.detailAlamat.text.toString())
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        detail()
    }

    fun detail() {
        val request = object : StringRequest(
            Method.POST,urlClass.show,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val nama = jsonObject.getString("nama")
                val kategori = jsonObject.getString("kategori")
                val berat = jsonObject.getString("berat")
                val harga = jsonObject.getString("harga")
                val tanggal = jsonObject.getString("tgl_penjemputan")
                val alamat = jsonObject.getString("alamat")
                val catatan = jsonObject.getString("catatan")
                val status = jsonObject.getString("status_bayar")
                val bukti = jsonObject.getString("bukti")

                b.detailNama.setText(nama)
                b.detailKategori.setText(kategori)
                b.detailBerat.setText(berat+" Kg")
                b.detailHarga.setText("Rp."+harga)
                b.detailTanggal.setText(tanggal)
                b.detailAlamat.setText(alamat)
                b.detailCatatan.setText(catatan)
                b.detailStatus.setText(status)

                if (status.equals("Belum bayar")){
                    b.detailStatus.setTextColor(Color.RED)
                    b.detailBayar.visibility = View.GONE
                } else {
                    b.detailStatus.setTextColor(Color.GREEN)
                    Picasso.get().load(bukti).into(b.detailBayar)
                    b.btnBayar.visibility = View.GONE
                    b.btnEdit.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                var paket : Bundle? = intent.extras
                hm.put("mode", "detail")
                hm.put("id_sampah", paket?.getString("id").toString())

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}