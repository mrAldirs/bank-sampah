package com.example.bank_sampah

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterRiwayat(val dataRiwayat: List<HashMap<String,String>>, val parent: RiwayatActivity) :
    RecyclerView.Adapter<AdapterRiwayat.HolderDataRiwayat>(){
    class HolderDataRiwayat(v : View) : RecyclerView.ViewHolder(v) {
        val nm = v.findViewById<TextView>(R.id.tvNama)
        val tgl = v.findViewById<TextView>(R.id.tvDate)
        val ktg = v.findViewById<TextView>(R.id.tvKategori)
        val brt = v.findViewById<TextView>(R.id.tvBerat)
        val sld = v.findViewById<TextView>(R.id.tvSaldo)
        val sts = v.findViewById<TextView>(R.id.tvStatus)
        val del = v.findViewById<ImageView>(R.id.imageDelete)
        val cd = v.findViewById<LinearLayout>(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataRiwayat {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_riwayat,parent,false)
        return HolderDataRiwayat(v)
    }

    override fun getItemCount(): Int {
        return dataRiwayat.size
    }

    override fun onBindViewHolder(holder: HolderDataRiwayat, position: Int) {
        val data = dataRiwayat.get(position)
        holder.nm.setText(data.get("nama"))
        holder.tgl.setText(data.get("tgl_penjemputan"))
        holder.ktg.setText(data.get("kategori"))
        holder.brt.setText("Berat "+data.get("berat")+" Kg")
        holder.sld.setText("Biaya Rp. "+data.get("harga"))
        holder.sts.setText(data.get("status_bayar"))

        holder.nm.textSize = parent.preferences.getInt(parent.ITM_RWT, 16).toFloat()
        holder.tgl.textSize = parent.preferences.getInt(parent.ITM_RWT, parent.DEF_ITM_RWT).toFloat()
        holder.ktg.textSize = parent.preferences.getInt(parent.ITM_RWT, 16).toFloat()
        holder.brt.textSize = parent.preferences.getInt(parent.ITM_RWT, parent.DEF_ITM_RWT).toFloat()
        holder.sts.textSize = parent.preferences.getInt(parent.ITM_RWT, 16).toFloat()
        holder.sld.textSize = parent.preferences.getInt(parent.ITM_RWT, parent.DEF_ITM_RWT).toFloat()

        holder.del.setOnClickListener {
            AlertDialog.Builder(parent)
                .setIcon(R.drawable.ic_info)
                .setTitle("Peringatan")
                .setMessage("Apakah Anda ingin menghapus data ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    parent.hapus(data.get("id_sampah").toString())
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }

        holder.cd.setOnClickListener {
            val intent = Intent(parent, DetailActivity::class.java)
            intent.putExtra("id", data.get("id_sampah").toString())
            parent.startActivity(intent)
        }
    }
}