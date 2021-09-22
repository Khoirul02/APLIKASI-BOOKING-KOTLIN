package com.aplikasi.booking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aplikasi.booking.fragment.DaftarKamarFragment
import com.aplikasi.booking.fragment.KamarFragment

@Suppress("DEPRECATION")
class KamarPagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val pages = listOf(
        KamarFragment(),
        DaftarKamarFragment()
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Kamar"
            else -> "Daftar Kamar"
        }
    }
}