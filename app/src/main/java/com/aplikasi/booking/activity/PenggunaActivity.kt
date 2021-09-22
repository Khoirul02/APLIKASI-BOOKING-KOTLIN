package com.aplikasi.booking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.PenggunaPagerAdapter
import kotlinx.android.synthetic.main.activity_pengguna.*

class PenggunaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengguna)
        supportActionBar!!.setTitle("Pengguna")
        viewpager_main.adapter = PenggunaPagerAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(viewpager_main)
    }
}
