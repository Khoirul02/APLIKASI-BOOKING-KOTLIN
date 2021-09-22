package com.aplikasi.booking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.BookingPagerPenggunaAdapter
import kotlinx.android.synthetic.main.activity_booking_pengguna.*

class BookingPenggunaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_pengguna)
        supportActionBar!!.setTitle("Data Booking")
        viewpager_main.adapter = BookingPagerPenggunaAdapter(supportFragmentManager)
        tab_main.setupWithViewPager(viewpager_main)
    }
}
