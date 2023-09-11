package com.example.bank_sampah

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.bank_sampah.databinding.ActivityInputDataBinding
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.HashMap

class InputDataActivity : AppCompatActivity() {
    private lateinit var b: ActivityInputDataBinding

    lateinit var urlClass : UrlClass

    val ktgSpinner = mutableListOf<String>()
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
                .setMessage("Apakah Anda sudah yakin ingin melakukan penjemputan sampah sekarang?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    create()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this,"Berhasil Membatalkan!", Toast.LENGTH_SHORT).show()
                })
                .show()
            true
        }
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

    override fun onStart() {
        super.onStart()
        getKategori()
    }

    fun create() {
        val request = object : StringRequest(
            Method.POST,urlClass.crud,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val respon = jsonObject.getString("respon")
                if (respon.equals("1")) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    Toast.makeText(this, "Berhasil mengirim data penjemputan sampah!", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Tidak dapat terhubung ke server", Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String, String>()
                hm.put("mode", "insert")
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

    fun getKategori() {
        val request = object : StringRequest(
            Method.POST,urlClass.show,
            Response.Listener { response ->
                ktgSpinner.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length()-1)){
                    val jsonObject = jsonArray.getJSONObject(x)
                    ktgSpinner.add(jsonObject.getString("kategori"))
                }
                strKategori.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->

            }){
            override fun getParams(): MutableMap<String, String>? {
                val hm = HashMap<String,String>()
                hm.put("mode", "get_kategori")
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}