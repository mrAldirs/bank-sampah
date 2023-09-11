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
import com.example.bank_sampah.databinding.ActivityQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class QrActivity : AppCompatActivity() {
    private lateinit var b: ActivityQrBinding
    lateinit var intentIntegrator: IntentIntegrator
    lateinit var urlClass : UrlClass

    var idHasil = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityQrBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        intentIntegrator = IntentIntegrator(this)

        urlClass = UrlClass()

        var paket : Bundle? = intent.extras
        val barCodeEncoder = BarcodeEncoder()
        val bitmap = barCodeEncoder.encodeBitmap(paket?.getString("id").toString(),
            BarcodeFormat.QR_CODE,400,400)
        b.imV.setImageBitmap(bitmap)

        b.btnScanQR.setOnClickListener {
            intentIntegrator.setBeepEnabled(true).initiateScan()
        }

        b.btnBayar.setOnClickListener {
            var paket : Bundle? = intent.extras
            val dialog = PembayaranQrFragment()

            val bundle = Bundle()
            bundle.putString("id", paket?.getString("id").toString())
            dialog.arguments = bundle

            dialog.show(supportFragmentManager, "KonfirmasiFragment")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if(intentResult!=null){
            if(intentResult.contents !=null){
                idHasil = intentResult.contents
                Toast.makeText(this, "Silahkan melakukan pembayaran dengan kode $idHasil", Toast.LENGTH_SHORT).show()
                detail(idHasil)
                b.btnScanQR.visibility = View.GONE
                b.imV.visibility = View.GONE
                b.btnBayar.visibility = View.VISIBLE
                b.ln.visibility = View.VISIBLE
            }else{
                Toast.makeText(this,"Dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun detail(id: String) {
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

                b.detailNama.setText(nama)
                b.detailKategori.setText(kategori)
                b.detailBerat.setText(berat+" Kg")
                b.detailHarga.setText("Rp."+harga)
                b.detailTanggal.setText(tanggal)
                b.detailAlamat.setText(alamat)
                b.detailCatatan.setText(catatan)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "detail")
                hm.put("id_sampah", id)

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}