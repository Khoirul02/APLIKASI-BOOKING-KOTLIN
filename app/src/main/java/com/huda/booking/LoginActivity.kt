package com.huda.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huda.booking.dashboard.DashboardPemilikActivity
import com.huda.booking.model.LoginResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        btnLogin.setOnClickListener {
            val user: String = edtUsername.text.toString()
            val pass: String = edtPassword.text.toString()
            if (user.trim().isEmpty() || pass.trim().isEmpty()){
                Toast.makeText(applicationContext, "Data Belum Lengkap! ", Toast.LENGTH_SHORT).show()
            }
            RetrofitClient.instance.userLogin(edtUsername.text.toString().trim(),edtPassword.text.toString().trim(),"login_akun")
                .enqueue(object : Callback<LoginResponse>{
                    override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                        Log.d("res", "" + t)
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>?,
                        response: Response<LoginResponse>?
                    ) {
                        val status = response!!.body().status
                        if (status == 1){
                            val data = response.body().data
                            for (item in data!!){
                                if (item!!.ruleAkun == "pemilik"){
                                    Toast.makeText(applicationContext, "Berhasil Login", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(applicationContext, DashboardPemilikActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(applicationContext, "Dashboard Umum", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            Toast.makeText(applicationContext, "Gagal Login", Toast.LENGTH_SHORT).show()
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
