package com.huda.booking.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.huda.booking.R
import com.huda.booking.adapter.BookingAdapter
import com.huda.booking.helper.Config
import com.huda.booking.helper.Config.ID_AKUN
import com.huda.booking.model.BookingItem
import com.huda.booking.model.BookingResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_riwayat_booking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNCHECKED_CAST")
class RiwayatBookingFragment : Fragment() {

    private lateinit var bookingAdapter: BookingAdapter
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat_booking, container, false)
    }
    override fun onStart() {
        super.onStart()
        sharedPreferences = requireActivity().getSharedPreferences(Config.SHARED_PRED_NAME, Context.MODE_PRIVATE)
        val idAkun = sharedPreferences.getString(ID_AKUN,"")
        setUpRecyclerView()
        getDataBookingById(idAkun)
    }

    private fun getDataBookingById(idAkun: String?) {
        RetrofitClient.instance.getBookingByIdUser(idAkun.toString(),"get_booking_id_user")
            .enqueue(object : Callback<BookingResponse>{
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
