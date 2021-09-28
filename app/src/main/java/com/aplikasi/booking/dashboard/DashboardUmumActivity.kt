package com.aplikasi.booking.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.aplikasi.booking.LoginActivity
import com.aplikasi.booking.R
import com.aplikasi.booking.activity.BookingPenggunaActivity
import com.aplikasi.booking.activity.FaqActivity
import com.aplikasi.booking.adapter.BookingPagerPenggunaAdapter
import com.aplikasi.booking.helper.Config
import com.aplikasi.booking.helper.Config.FOTO_AKUN
import com.aplikasi.booking.helper.Config.NAMA_AKUN
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_booking_pengguna.*
import kotlinx.android.synthetic.main.activity_dashboard_umum.*
import kotlinx.android.synthetic.main.activity_dashboard_umum.tab_main
import kotlinx.android.synthetic.main.activity_dashboard_umum.viewpager_main

@Suppress("UNSAFE_CALL_ON_PARTIALLY_DEFINED_RESOURCE")
class DashboardUmumActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var btnFAQ: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_umum)
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(NAMA_AKUN,"")
        supportActionBar!!.setTitle("Hallo, $name")
        viewpager_main.adapter = BookingPagerPenggunaAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(viewpager_main)
//        supportActionBar!!.hide()
//        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
//        val name = sharedPreferences.getString(NAMA_AKUN,"")
//        val foto = sharedPreferences.getString(FOTO_AKUN,"")
//        tvNamaProfil.text = name
//        Glide.with(this).load(Config.BASE_URL_PHOTO+foto)
//            .apply(RequestOptions().override(300, 300)).into(ivImageProfile)
//        cv_menu_booking.setOnClickListener {
//            val intent = Intent(this, BookingPenggunaActivity::class.java)
//            startActivity(intent)
//        }
//        cv_menu_faq.setOnClickListener {
//            val intent = Intent(this, FaqActivity::class.java)
//            startActivity(intent)
//        }
        btnFAQ = findViewById(R.id.fab)
        btnFAQ.setOnClickListener { view ->
            val intent = Intent(this, FaqActivity::class.java)
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
                logout(this@DashboardUmumActivity)
            }
            setNegativeButton("Tidak"){_, _ ->
                // if user press no, then return the activity
                Toast.makeText(this@DashboardUmumActivity, "Batal",
                    Toast.LENGTH_LONG).show()
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
