package com.huda.booking.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.huda.booking.R

class DashboardUmumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_umum)
        supportActionBar!!.hide()
    }
}