package com.huda.booking.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.booking.R
import com.huda.booking.activity.KamarActivity
import com.huda.booking.activity.PenggunaActivity
import kotlinx.android.synthetic.main.activity_dashboard_pemilik.*

class DashboardPemilikActivity : AppCompatActivity() {
    val ID_AKUN = "id_akun"
    val NAMA_AKUN = "nama_akun"
    val TEMPAT_LAHIR_AKUN = "tempat_lahir_akun"
    val TGL_LAHIR_AKUN = "tgl_lahir_akun"
    val JENIS_KELAMIN_AKUN = "jenis_kelamin_akun"
    val USERNAME_AKUN = "username_akun"
    val PASSWORD_AKUN = "password_akun"
    val ALAMAT_AKUN = "alamat_akun"
    val NO_HP_AKUN = "no_hp_akun"
    val FOTO_AKUN = "foto_akun"
    val RULE_AKUN = "rule_akun"
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_pemilik)
        supportActionBar!!.hide()
        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(NAMA_AKUN,"")
        val foto = sharedPreferences.getString(FOTO_AKUN,"")
        tvNamaProfil.text = name
        Glide.with(this).load("http://192.168.100.239/BACKEND-DIVA-KOST/images/$foto")
            .apply(RequestOptions().override(300, 300)).into(ivImageProfile)
        cv_menu_kamar.setOnClickListener {
            val intent = Intent(applicationContext, KamarActivity::class.java)
            startActivity(intent)
        }
        cv_menu_pengguna.setOnClickListener {
            val intent = Intent(applicationContext, PenggunaActivity::class.java)
            startActivity(intent)
        }
        cv_menu_booking.setOnClickListener {

        }
    }
}
