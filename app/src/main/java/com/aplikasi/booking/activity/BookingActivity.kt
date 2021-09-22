package com.aplikasi.booking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.BookingPagerAdapter
import kotlinx.android.synthetic.main.activity_booking.*

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        supportActionBar!!.setTitle("Booking")
        viewpager_main.adapter = BookingPagerAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(viewpager_main)
    }
}
