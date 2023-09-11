package com.example.bank_sampah

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterFoto(val dataFoto: List<HashMap<String,String>>, val parent: DokumentasiSampahActivity)
    : RecyclerView.Adapter<AdapterFoto.HolderDataFoto>(){
    class HolderDataFoto(v : View) : RecyclerView.ViewHolder(v) {
        val ft = v.findViewById<ImageView>(R.id.itemFoto)
        val jns = v.findViewById<TextView>(R.id.itemJenis)
        val del = v.findViewById<ImageView>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDataFoto {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item_foto,parent,false)
        return HolderDataFoto(v)
    }

    override fun getItemCount(): Int {
        return dataFoto.size
    }

    override fun onBindViewHolder(holder: HolderDataFoto, position: Int) {
        val data = dataFoto.get(position)
        Picasso.get().load(data.get("foto")).into(holder.ft)
        holder.jns.setText(data.get("jenis"))
        holder.del.setOnClickListener {
            AlertDialog.Builder(parent)
                .setIcon(R.drawable.ic_info)
                .setTitle("Peringatan")
                .setMessage("Apakah Anda ingin menghapus data ini?")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    parent.crud("delete", data.get("id").toString())
                    parent.recreate()
                })
                .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                .show()
            true
        }
    }
}