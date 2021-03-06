package com.aplikasi.booking.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.FaqAdapter
import kotlinx.android.synthetic.main.activity_faq.*

class FaqActivity : AppCompatActivity() {
    private lateinit var faqAdapter: FaqAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)
        faqAdapter = FaqAdapter()
        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FaqActivity)
            adapter = faqAdapter
        }
    }
}
