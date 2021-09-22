@file:Suppress("DEPRECATION")

package com.aplikasi.booking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aplikasi.booking.fragment.KamarFragment
import com.aplikasi.booking.fragment.RiwayatBookingFragment

@Suppress("DEPRECATION")
class BookingPagerPenggunaAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val pages = listOf(
        KamarFragment(),
        RiwayatBookingFragment()
    )
    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            0 -> "Kamar"
            else -> "Riwayat"
        }
    }
}