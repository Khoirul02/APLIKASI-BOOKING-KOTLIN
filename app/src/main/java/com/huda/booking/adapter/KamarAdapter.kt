package com.huda.booking.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.booking.R
import com.huda.booking.activity.EditKamarActivity
import com.huda.booking.activity.KamarActivity
import com.huda.booking.model.DataItemKamar
import com.huda.booking.model.DefaultResponse
import com.huda.booking.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class KamarAdapter(val result: ArrayList<DataItemKamar>) : RecyclerView.Adapter<KamarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KamarAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_kamar, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: KamarAdapter.ViewHolder, position: Int) {
        val data = result[position]
        Glide.with(holder.itemView).load("http://192.168.100.239/BACKEND-DIVA-KOST/images/"+data.fotoKamar)
            .apply(RequestOptions().override(300, 300)).into(holder.fotoKamar)
        holder.namaKamar.text = data.namaKamar
        holder.kategoriKamar.text = "Kost ${data.kategoriKamar}"
        holder.deskripsiKamar.text = data.fasilitasKamar
        holder.statusKamar.text = "Status : ${data.statusKamar}"
        holder.btnEditKamar.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditKamarActivity::class.java)
            intent.putExtra("id_kamar", data.idKamar )
            intent.putExtra("nama_kamar", data.namaKamar )
            intent.putExtra("fasilitas_kamar", data.fasilitasKamar )
            intent.putExtra("foto_kamar", data.fotoKamar )
            intent.putExtra("kategori_kamar", data.kategoriKamar )
            intent.putExtra("harga_kamar", data.hargaKamar )
            intent.putExtra("status_kamar", data.statusKamar )
            holder.itemView.context.startActivity(intent)
        }
        holder.btnHapusKamar.setOnClickListener {
            RetrofitClient.instance.deleteKamar(data.idKamar.toString(), "delete_kamar")
                .enqueue(object : retrofit2.Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>?,
                        response: Response<DefaultResponse>?
                    ) {
                        if (response!!.isSuccessful){
                            val intent = Intent(holder.itemView.context, KamarActivity::class.java)
                            holder.itemView.context.startActivity(intent)
                            (holder.itemView.context as Activity).finish()
                            Toast.makeText(holder.itemView.context, "Berhasil Hapus Data", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(holder.itemView.context, "Response Gagal", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                        Toast.makeText(holder.itemView.context, "Response Gagal", Toast.LENGTH_LONG).show()
                    }

                })
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fotoKamar : ImageView
        val namaKamar : TextView
        val kategoriKamar : TextView
        val btnEditKamar : Button
        val btnHapusKamar : Button
        val deskripsiKamar : TextView
        val statusKamar : TextView
        init {
            fotoKamar = itemView.findViewById(R.id.iv_foto_list_kamar)
            namaKamar = itemView.findViewById(R.id.tv_list_kamar)
            kategoriKamar = itemView.findViewById(R.id.tv_list_kategori_kamar)
            btnEditKamar = itemView.findViewById(R.id.btn_edit_kamar)
            btnHapusKamar = itemView.findViewById(R.id.btn_hapus_kamar)
            deskripsiKamar = itemView.findViewById(R.id.tv_list_deskripsi_kamar)
            statusKamar = itemView.findViewById(R.id.tv_list_status_kamar)
        }
    }
    fun setData(data: List<DataItemKamar>){
        result.clear()
        result.addAll(data)
        notifyDataSetChanged()
    }
}