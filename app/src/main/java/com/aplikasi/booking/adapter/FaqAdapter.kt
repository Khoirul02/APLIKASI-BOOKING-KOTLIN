package com.aplikasi.booking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.booking.R

class FaqAdapter : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    val pertanyaan = arrayListOf("Apakah saya harus memiliki akun untuk dapat menyewa kamar Diva Kos?",
    "Bagaimana cara membuat akun?", "Saya sudah membuat akun, bagaimana cara cari kamar Diva Kos?"
    ,"Bagaimana agar transaksi saya aman?")
    val jawaban = arrayListOf("Dengan memiliki akun Anda akan mendapatkan kemudahan dalam mencari dan menyewa kos di Diva Kos. Booking kos dengan fitur Booking Langsung dan bayar melalui aplikasi.",
    "Anda dapat membuat akun penyewa dengan mengikuti langkah pendaftaran akun.",
    "Jika Anda sudah memiliki akun , Anda dapat dengan mudah mencari kos di Diva Kos melalui aplikasi.",
    "Jika Anda sudah memiliki akun , Anda dapat dengan mudah mencari kos di Diva Kos melalui aplikasi.")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_faq, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqAdapter.ViewHolder, position: Int) {
        holder.tvPertanyaan.text = pertanyaan[position]
        holder.tvJawaban.text = jawaban[position]
    }

    override fun getItemCount(): Int {
        return pertanyaan.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvPertanyaan : TextView = itemView.findViewById(R.id.tv_list_pertanyaan)
        val tvJawaban : TextView = itemView.findViewById(R.id.tv_list_jawaban)
    }
}