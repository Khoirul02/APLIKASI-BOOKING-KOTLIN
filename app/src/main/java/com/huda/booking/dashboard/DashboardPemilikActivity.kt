package com.huda.booking.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.booking.LoginActivity
import com.huda.booking.R
import com.huda.booking.activity.BookingActivity
import com.huda.booking.activity.KamarActivity
import com.huda.booking.activity.PenggunaActivity
import com.huda.booking.helper.Config
import com.huda.booking.helper.Config.FOTO_AKUN
import com.huda.booking.helper.Config.NAMA_AKUN
import kotlinx.android.synthetic.main.activity_dashboard_pemilik.*

class DashboardPemilikActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_pemilik)
        supportActionBar!!.hide()
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(NAMA_AKUN, "")
        val foto = sharedPreferences.getString(FOTO_AKUN, "")
        tvNamaProfil.text = name
        Glide.with(this).load(Config.BASE_URL_PHOTO + foto)
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
            val intent = Intent(applicationContext, BookingActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Booking")
            setMessage("Apakah kamu mau logout dari aplikasi?")
            setPositiveButton("Ya") { _, _ ->
                // if user press yes, then finish the current activity
//                super.onBackPressed()
                logout(this@DashboardPemilikActivity)
            }
            setNegativeButton("Tidak"){ _, _ ->
                // if user press no, then return the activity
                Toast.makeText(
                    this@DashboardPemilikActivity, "Batal",
                    Toast.LENGTH_LONG
                ).show()
            }
            setCancelable(true)
        }.create().show()
    }
    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences(Config.SHARED_PRED_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Config.ID_AKUN, "")
        editor.putString(NAMA_AKUN, "")
        editor.putString(Config.TEMPAT_LAHIR_AKUN, "")
        editor.putString(Config.TGL_LAHIR_AKUN, "")
        editor.putString(Config.JENIS_KELAMIN_AKUN, "")
        editor.putString(Config.USERNAME_AKUN, "")
        editor.putString(Config.PASSWORD_AKUN, "")
        editor.putString(Config.ALAMAT_AKUN, "")
        editor.putString(Config.NO_HP_AKUN, "")
        editor.putString(FOTO_AKUN, "")
        editor.putString(Config.RULE_AKUN, "")
        editor.apply()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}
