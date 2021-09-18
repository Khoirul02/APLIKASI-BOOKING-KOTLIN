package com.huda.booking.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.huda.booking.fragment.DaftarPenggunaFragment
import com.huda.booking.fragment.PenggunaFragment

@Suppress("DEPRECATION")
class PenggunaPagerAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val pages = listOf(
        PenggunaFragment(),
        DaftarPenggunaFragment()
    )

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Pengguna"
            else -> "Daftar Pengguna"
        }
    }

}