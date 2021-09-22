package com.aplikasi.booking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.BookingAdapter
import com.aplikasi.booking.model.BookingItem
import com.aplikasi.booking.model.BookingResponse
import com.aplikasi.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_booking.*
import retrofit2.Call
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNCHECKED_CAST")
class BookingFragment : Fragment() {

    private lateinit var bookingAdapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)
    }
    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        getDataKamar()
    }

    private fun getDataKamar() {
        RetrofitClient.instance.getBooking("get_booking")
            .enqueue(object : retrofit2.Callback<BookingResponse> {
                override fun onResponse(
                    call: Call<BookingResponse>?,
                    response: Response<BookingResponse>?
                ) {
                    if (response!!.isSuccessful){
                        showData( response.body())
                    }else{
                        Toast.makeText(activity, "Response Tidak Ada", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BookingResponse>?, t: Throwable?) {
                    Toast.makeText(activity, "Data Kosong", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun setUpRecyclerView() {
        bookingAdapter = BookingAdapter(arrayListOf())
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = bookingAdapter
        }
    }

    private fun showData(data: BookingResponse){
        val result = data.data
        bookingAdapter.setData(result as List<BookingItem>)
    }

}
