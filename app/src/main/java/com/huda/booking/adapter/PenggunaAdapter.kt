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
import com.huda.booking.activity.EditPenggunaActivity
import com.huda.booking.activity.PenggunaActivity
import com.huda.booking.helper.Config
import com.huda.booking.model.DataPengguna
import com.huda.booking.model.DefaultResponse
import com.huda.booking.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PenggunaAdapter(val result : ArrayList<DataPengguna>) : RecyclerView.Adapter<PenggunaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenggunaAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_pengguna,parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("CheckResult", "SetTextI18n")
    override fun onBindViewHolder(holder: PenggunaAdapter.ViewHolder, position: Int) {
        val data = result[position]
        Glide.with(holder.itemView).load(Config.BASE_URL_PHOTO+data.fotoAkun)
            .apply(RequestOptions().override(300,300)).into(holder.fotoPengguna)
        holder.namaPengguna.text = data.namaAkun
        holder.rulePengguna.text = "Status : "+data.ruleAkun
        holder.alamatPengguna.text = data.alamatAkun
        holder.nohpPengguna.text = "No Hp : "+data.noHpAkun
        holder.btnEditPengguna.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditPenggunaActivity::class.java)
            intent.putExtra("id_pengguna", data.idAkun)
            intent.putExtra("foto_pengguna", data.fotoAkun)
            intent.putExtra("nama_pengguna", data.namaAkun)
            intent.putExtra("username", data.usernameAkun)
            intent.putExtra("tempat_lahir_pengguna", data.tempatLahirAkun)
            intent.putExtra("rule_pengguna", data.ruleAkun)
            intent.putExtra("alamat_pengguna", data.alamatAkun)
            intent.putExtra("nohp_pengguna", data.noHpAkun)
            holder.itemView.context.startActivity(intent)
        }
        holder.btnHapusPengguna.setOnClickListener {
            RetrofitClient.instance.deleteUser(data.idAkun.toString(),"delete_akun")
                .enqueue(object : Callback<DefaultResponse>{
                    override fun onResponse(
                        call: Call<DefaultResponse>?,
                        response: Response<DefaultResponse>?
                    ) {
                        if (response!!.isSuccessful){
                            val intent = Intent(holder.itemView.context, PenggunaActivity::class.java)
                            holder.itemView.context.startActivity(intent)
                            (holder.itemView.context as Activity).finish()
                            Toast.makeText(holder.itemView.context, "Berhasil Hapus Data", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(holder.itemView.context,"Response Gagal", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                       Toast.makeText(holder.itemView.context,"Tidak Terkoneksi Ke Response", Toast.LENGTH_LONG).show()
                    }

                })
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val fotoPengguna : ImageView = itemView.findViewById(R.id.iv_foto_list_pengguna)
        val namaPengguna : TextView = itemView.findViewById(R.id.tv_list_pengguna)
        val rulePengguna : TextView = itemView.findViewById(R.id.tv_list_rule_pengguna)
        val alamatPengguna : TextView = itemView.findViewById(R.id.tv_list_alamat_pengguna)
        val btnEditPengguna : Button = itemView.findViewById(R.id.btn_edit_pengguna)
        val btnHapusPengguna : Button = itemView.findViewById(R.id.btn_hapus_pengguna)
        val nohpPengguna : TextView = itemView.findViewById(R.id.tv_list_nohp_pengguna)
    }
    fun setData(data: List<DataPengguna>){
        result.clear()
        result.addAll(data)
        notifyDataSetChanged()
    }
}