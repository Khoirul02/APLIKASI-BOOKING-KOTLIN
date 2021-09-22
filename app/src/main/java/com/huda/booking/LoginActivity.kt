package com.huda.booking

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huda.booking.dashboard.DashboardPemilikActivity
import com.huda.booking.dashboard.DashboardUmumActivity
import com.huda.booking.helper.Config
import com.huda.booking.model.LoginResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        sharedPreferences = getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        btnLogin.setOnClickListener {
            val user: String = edtUsername.text.toString()
            val pass: String = edtPassword.text.toString()
            if (user.trim().isEmpty() || pass.trim().isEmpty()) {
                Toast.makeText(applicationContext, "Data Belum Lengkap! ", Toast.LENGTH_SHORT)
                    .show()
            }
            RetrofitClient.instance.userLogin(
                edtUsername.text.toString().trim(),
                edtPassword.text.toString().trim(),
                "login_akun"
            )
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        Log.d("res", "" + t)
                    }

                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<LoginResponse>?,
                        response: Response<LoginResponse>?
                    ) {
                        val status = response!!.body().status
                        if (status == 1) {
                            if (response.isSuccessful) {
                                val data = response.body().data
                                for (item in data!!) {
                                    val editor: SharedPreferences.Editor? = sharedPreferences.edit()
                                    editor!!.putString(Config.ID_AKUN, item!!.idAkun)
                                    editor.putString(Config.NAMA_AKUN, item.namaAkun)
                                    editor.putString(Config.TEMPAT_LAHIR_AKUN, item.tempatLahirAkun)
                                    editor.putString(Config.TGL_LAHIR_AKUN, item.tglLahirAkun)
                                    editor.putString(Config.JENIS_KELAMIN_AKUN, item.jenisKelamin)
                                    editor.putString(Config.USERNAME_AKUN, item.usernameAkun)
                                    editor.putString(Config.PASSWORD_AKUN, item.passwordAkun)
                                    editor.putString(Config.ALAMAT_AKUN, item.alamatAkun)
                                    editor.putString(Config.NO_HP_AKUN, item.noHpAkun)
                                    editor.putString(Config.FOTO_AKUN, item.fotoAkun)
                                    editor.putString(Config.RULE_AKUN, item.ruleAkun)
                                    editor.apply()
                                    if (item.ruleAkun == "pemilik") {
                                        Toast.makeText(applicationContext, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(applicationContext, DashboardPemilikActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(applicationContext, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(applicationContext, DashboardUmumActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            }else{
                                Toast.makeText(applicationContext, "Gagal Menerima Respon", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Gagal Login", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                })
        }
        txtDftr.setOnClickListener {
            val intent = Intent(this, RegistrasiActivity::class.java)
            startActivity(intent)
        }
    }
}
