package com.huda.booking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.huda.booking.fragment.BookingFragment
import com.huda.booking.fragment.DaftarBookingFragment

@Suppress("DEPRECATION")
class BookingPagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val pages = listOf(
        BookingFragment(),
        DaftarBookingFragment()
    )
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Booking"
            else -> "Daftar Booking"
        }
    }
}