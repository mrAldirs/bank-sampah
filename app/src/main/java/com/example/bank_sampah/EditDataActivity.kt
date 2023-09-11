package com.example.bank_sampah

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bank_sampah.databinding.ActivityInputDataBinding
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.util.Calendar
import java.util.HashMap

class EditDataActivity : AppCompatActivity() {
    private lateinit var b: ActivityInputDataBinding

    lateinit var urlClass : UrlClass

    val ktgSpinner = arrayOf("Plastik", "Logam", "Kertas", "Karet")
    lateinit var strKategori: ArrayAdapter<String>

    var inputHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityInputDataBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        urlClass = UrlClass()

        strKategori = ArrayAdapter(this, android.R.layout.simple_list_item_1, ktgSpinner)
        b.spKategori.adapter = strKategori

        b.inputBerat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val berat = s?.toString()?.toIntOrNull() ?: 0
                inputHarga = berat * 1500
                b.inputHarga.setText("Rp. "+inputHarga.toString())
            }
        })

        b.inputTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        b.btnCheckout.setOnClickListener {
            AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_info)
                .setTitle("Konfirmasi!")
                .setMessage("Apakah Anda sudah yakin ingin mengedit penjemputan sampah?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    update()
                    recreate()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this,"Berhasil Membatalkan!", Toast.LENGTH_SHORT).show()
                })
                .show()
            true
        }

        detail()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val date = "$year-${month + 1}-$dayOfMonth"
                b.inputTanggal.setText(date)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
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

                b.inputNama.setText(nama)
                b.inputBerat.setText(berat)
                b.inputHarga.setText("Rp."+harga)
                b.inputTanggal.setText(tanggal)
                b.inputAlamat.setText(alamat)
                b.inputTambahan.setText(catatan)

                val pos = strKategori.getPosition(kategori)
                b.spKategori.setSelection(pos)
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

    private fun update() {
        val request = object : StringRequest(
            Method.POST,urlClass.crud,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    var paket : Bundle? = intent.extras
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("id", paket?.getString("id").toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    Toast.makeText(this, "Berhasil mengedit data penjemputan sampah!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                var paket : Bundle? = intent.extras
                hm.put("mode", "edit")
                hm.put("id_sampah", paket?.getString("id").toString())
                hm.put("nama", b.inputNama.text.toString())
                hm.put("kategori", b.spKategori.selectedItem.toString())
                hm.put("berat", b.inputBerat.text.toString())
                hm.put("harga", inputHarga.toString())
                hm.put("tgl_penjemputan", b.inputTanggal.text.toString())
                hm.put("alamat", b.inputAlamat.text.toString())
                hm.put("catatan", b.inputTambahan.text.toString())

                return hm
            }
        }
        val  queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}