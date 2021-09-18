package com.huda.booking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.huda.booking.R
import com.huda.booking.adapter.KamarPagerAdapter
import kotlinx.android.synthetic.main.activity_kamar.*

class KamarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kamar)
        supportActionBar!!.setTitle("Kamar")
        viewpager_main.adapter = KamarPagerAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(viewpager_main)
    }
}
