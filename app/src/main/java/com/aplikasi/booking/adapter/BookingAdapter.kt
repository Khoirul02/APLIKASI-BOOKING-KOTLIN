package com.aplikasi.booking.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import com.aplikasi.booking.R
import com.aplikasi.booking.activity.BookingActivity
import com.aplikasi.booking.activity.BookingPenggunaActivity
import com.aplikasi.booking.helper.Config
import com.aplikasi.booking.model.BookingItem
import com.aplikasi.booking.model.DefaultResponse
import com.aplikasi.booking.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class BookingAdapter(val result: ArrayList<BookingItem>) :
    RecyclerView.Adapter<BookingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_booking, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: BookingAdapter.ViewHolder, position: Int) {
        val data = result[position]
        Glide.with(holder.itemView).load(Config.BASE_URL_PHOTO + data.buktiPembayaranBooking).apply(
            RequestOptions().override(
                300,
                300
            )
        ).into(holder.fotoBooking)
        holder.namaBooking.text = data.namaAkun
        holder.kategoriBooking.text = "Cara Bayar: ${data.caraBayarBooking}"
        holder.lamaSewaBooking.text =
            "Lama : ${data.lamaSewaBooking} Bln (Rp. ${data.jumlahBayarBooking})"
        holder.deskripsiBooking.text = "(${data.namaKamar}): ${data.fasilitasKamar}"
        holder.statusBooking.text = "Status Booking : ${data.statusBooking}"
        val tglLama = data.tanggalMulaiKostBooking
        val tglLamaSelesai = data.tanggalKeluarKostBooking
        val dateFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val tglBaruMulai: String = dateFormat.format(df.parse(tglLama))
        val tglBaruSelesai: String = dateFormat.format(df.parse(tglLamaSelesai))
        holder.tglBooking.text = "Booking Sewa : $tglBaruMulai - $tglBaruSelesai"
        val dataStatus = data.statusBooking
        val rule = holder.ValueRule
        if ("pemilik" == rule) {
            holder.btnVerifikasiBooking.visibility = View.VISIBLE
            holder.btnHapusBooking.visibility = View.VISIBLE
            holder.btnBatalBooking.visibility = View.GONE
            if ("terverifikasi" == dataStatus) {
                holder.btnVerifikasiBooking.visibility = View.GONE
                holder.btnHapusBooking.visibility = View.GONE
                holder.btnBatalBooking.visibility = View.GONE
            }
        } else {
            holder.btnBatalBooking.visibility = View.VISIBLE
        }
        holder.btnVerifikasiBooking.setOnClickListener {
            RetrofitClient.instance.verifBooking(
                data.idBooking.toString(),
                "terverifikasi",
                "update_verifikasi"
            )
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>?,
                        response: Response<DefaultResponse>?
                    ) {
                        if (response!!.isSuccessful) {
                            val intent = Intent(
                                holder.itemView.context,
                                BookingActivity::class.java
                            )
                            holder.itemView.context.startActivity(intent)
                            (holder.itemView.context as Activity).finish()
                            Toast.makeText(
                                holder.itemView.context,
                                "Berhasil Verifikasi Data",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                holder.itemView.context,
                                "Response Gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Response Gagal $t",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
        holder.btnBatalBooking.setOnClickListener {
            if ("terverifikasi" == dataStatus) {
                Toast.makeText(
                    holder.itemView.context,
                    "Status Terverifikasi, Tidak Bisa Dibatalkan",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                RetrofitClient.instance.deleteBooking(data.idBooking.toString(), "delete_booking")
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(
                            call: Call<DefaultResponse>?,
                            response: Response<DefaultResponse>?
                        ) {
                            if(response!!.isSuccessful){
                                val intent = Intent(
                                    holder.itemView.context,
                                    BookingPenggunaActivity::class.java
                                )
                                holder.itemView.context.startActivity(intent)
                                (holder.itemView.context as Activity).finish()
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Berhasil Batal Booking",
                                    Toast.LENGTH_LONG
                                ).show()
                            }else{
                                Toast.makeText(
                                    holder.itemView.context,
                                    "Response Gagal",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Response Gagal $t",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
            }
        }
        holder.btnHapusBooking.setOnClickListener {
            RetrofitClient.instance.deleteBooking(data.idBooking.toString(), "delete_booking")
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>?,
                        response: Response<DefaultResponse>?
                    ) {
                        if (response!!.isSuccessful) {
                            val intent = Intent(
                                holder.itemView.context,
                                BookingActivity::class.java
                            )
                            holder.itemView.context.startActivity(intent)
                            (holder.itemView.context as Activity).finish()
                            Toast.makeText(
                                holder.itemView.context,
                                "Berhasil Hapus Data",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                holder.itemView.context,
                                "Response Gagal",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                        Toast.makeText(
                            holder.itemView.context,
                            "Response Gagal $t",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }

    inner class ViewHolder(iteView: View) : RecyclerView.ViewHolder(iteView) {
        val fotoBooking: ImageView = itemView.findViewById(R.id.iv_foto_list_booking)
        val namaBooking: TextView = itemView.findViewById(R.id.tv_list_booking)
        val kategoriBooking: TextView = itemView.findViewById(R.id.tv_list_kategori_booking)
        val lamaSewaBooking: TextView = itemView.findViewById(R.id.tv_list_lama_sewa)
        val btnVerifikasiBooking: Button = itemView.findViewById(R.id.btn_verif_booking)
        val btnHapusBooking: Button = itemView.findViewById(R.id.btn_hapus_booking)
        val btnBatalBooking: Button = itemView.findViewById(R.id.btn_batal_booking_kamar)
        val deskripsiBooking: TextView = itemView.findViewById(R.id.tv_list_deskripsi_booking)
        val statusBooking: TextView = itemView.findViewById(R.id.tv_list_status_booking)
        val tglBooking: TextView = itemView.findViewById(R.id.tv_title)
        val sharedPreferences =
            itemView.context.getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val ValueRule = sharedPreferences.getString(Config.RULE_AKUN, "")
    }

    fun setData(data: List<BookingItem>) {
        result.clear()
        result.addAll(data)
        notifyDataSetChanged()
    }
}