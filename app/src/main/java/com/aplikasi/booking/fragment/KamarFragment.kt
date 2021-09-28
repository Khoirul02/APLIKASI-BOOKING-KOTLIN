package com.aplikasi.booking.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplikasi.booking.R
import com.aplikasi.booking.adapter.KamarAdapter
import com.aplikasi.booking.model.DataItemKamar
import com.aplikasi.booking.model.KamarResponse
import com.aplikasi.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_kamar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
@Suppress("UNCHECKED_CAST")
class KamarFragment : Fragment() {

    private lateinit var kamarAdapter : KamarAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kamar, container, false)
    }

    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
        getDataKamar()
    }
    private fun setUpRecyclerView(){
        kamarAdapter = KamarAdapter(arrayListOf())
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = kamarAdapter
        }
    }
    private fun getDataKamar() {
        RetrofitClient.instance.getKamar("get_kamar")
            .enqueue(object : Callback<KamarResponse> {
                override fun onResponse(
                    call: Call<KamarResponse>?,
                    response: Response<KamarResponse>?
                ) {
                    if (response!!.isSuccessful){
                        if (response.body().data === null){
                            Toast.makeText(activity, "", Toast.LENGTH_LONG).show()
                        }else{
                            showData(response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<KamarResponse>?, t: Throwable?) {
                    Toast.makeText(activity, "Data Kosong", Toast.LENGTH_LONG).show()
                }

            })
    }
    private fun showData(data : KamarResponse) {
        val result = data.data
        kamarAdapter.setData(result as List<DataItemKamar>)
    }
}
