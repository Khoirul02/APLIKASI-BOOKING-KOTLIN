package com.huda.booking.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.huda.booking.R
import com.huda.booking.adapter.PenggunaAdapter
import com.huda.booking.model.DataPengguna
import com.huda.booking.model.PenggunaResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_pengguna.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNCHECKED_CAST")
class PenggunaFragment : Fragment() {

    private lateinit var penggunaAdapter : PenggunaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengguna, container, false)
    }
    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        getDataPengguna()
    }

    private fun setUpRecyclerView() {
        penggunaAdapter = PenggunaAdapter(arrayListOf())
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = penggunaAdapter
        }
    }

    private fun getDataPengguna() {
        RetrofitClient.instance.getUser("get_akun")
            .enqueue(object : Callback<PenggunaResponse>{
                override fun onResponse(
                    call: Call<PenggunaResponse>?,
                    response: Response<PenggunaResponse>?
                ) {
                    if(response!!.isSuccessful){
                        showData(response.body())
                    }
                }

                override fun onFailure(call: Call<PenggunaResponse>?, t: Throwable?) {
                    Toast.makeText(activity, "Data Kosong", Toast.LENGTH_LONG).show()
                }

            })
    }
    private fun showData(data : PenggunaResponse){
        val result = data.data
        penggunaAdapter.setData(result as List<DataPengguna>)
    }

}
