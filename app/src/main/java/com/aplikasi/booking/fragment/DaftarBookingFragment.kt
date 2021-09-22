package com.aplikasi.booking.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.aplikasi.booking.R
import com.aplikasi.booking.model.DefaultResponse
import com.aplikasi.booking.model.KamarResponse
import com.aplikasi.booking.model.PenggunaResponse
import com.aplikasi.booking.model.ResultResponse
import com.aplikasi.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_daftar_booking.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NAME_SHADOWING")
class DaftarBookingFragment : Fragment(), AdapterView.OnItemSelectedListener {

    var listCaraBayar = arrayOf("cash", "transfer")
    var spinnerCaraBayar: Spinner? = null
    var spinnerPilihPengguna: Spinner? = null
    var spinnerPilihKamar: Spinner? = null
    var urlBuktiPembayaranBooking: String? = ""
    var idPengguna: String? = null
    var idKamar: String? = null
    var cal = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_booking, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerCaraBayar = this.spn_cara_bayar_booking
        val cb =
            activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    listCaraBayar
                )
            }
        cb!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCaraBayar!!.adapter = cb
        spinnerPilihPengguna = this.spn_pilih_pengguna
        spinnerPilihPengguna!!.onItemSelectedListener = this
        getPilihPengguna()
        spinnerPilihKamar = this.spn_pilih_kamar
        spinnerPilihKamar!!.onItemSelectedListener = this
        getPilihKamar()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        val dateSetListener2 =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView2()
            }
        div_tanggal_mulai_kost_booking.setOnClickListener {
            activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
        div_tanggal_keluar_kost_booking.setOnClickListener {
            activity?.let { it1 ->
                DatePickerDialog(
                    it1,
                    dateSetListener2,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        btn_kirim_data_booking.setOnClickListener {
            if (edt_lama_sewa_booking.text.toString().trim() == ""){
                Toast.makeText(activity, "Data Lama Sewa Masih Kosong", Toast.LENGTH_LONG).show()
            }else{
                if (urlBuktiPembayaranBooking!! == ""){
                    Toast.makeText(activity, "Bukti Masih Belum DiUpload", Toast.LENGTH_LONG).show()
                }else{
                    uploadImage()
                }
            }
        }
        btn_pilih_foto_bukti_pembayaran_booking.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        if (activity?.let { ActivityCompat.checkSelfPermission(it, READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            intent.putExtra("crop", "true")
            intent.putExtra("scale", true)
            intent.putExtra("aspectX", 16)
            intent.putExtra("aspectY", 9)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                val imageFile = uriToImageFile(uri)
                val bitmapImage = BitmapFactory.decodeFile(imageFile.toString())
                iv_foto_bukti_pembayaran_booking.setImageBitmap(bitmapImage)
            }
            if (uri != null) {
                val imageBitmap = uriToBitmap(uri)
                iv_foto_bukti_pembayaran_booking.setImageBitmap(imageBitmap)
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    pickImage()
                }
            }
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val filePath = cursor.getString(columnIndex)
                urlBuktiPembayaranBooking = filePath
                cursor.close()
                return File(filePath)
            }
            cursor.close()
        }
        return null
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
    }
    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }
    private fun uploadImage() {
        val f = File(urlBuktiPembayaranBooking)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        val part = MultipartBody.Part.createFormData("uploaded_file", f.toString(), requestFile)
        val resultCAll: Call<ResultResponse?>? = RetrofitClient.instance.postImage(part)
        resultCAll!!.enqueue(object : Callback<ResultResponse?> {
            override fun onResponse(
                call: Call<ResultResponse?>?,
                response: Response<ResultResponse?>?
            ) {
                if (response!!.isSuccessful) {
                    RetrofitClient.instance.insertBooking(idPengguna.toString(),idKamar.toString(),tv_tanggal_mulai_kost_booking.text.toString().trim(),tv_tanggal_keluar_kost_booking.text.toString().trim()
                    ,edt_lama_sewa_booking.text.toString().trim(), spn_cara_bayar_booking.selectedItem.toString(),f.getName(), "belum","insert_booking")
                        .enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>?,
                                response: Response<DefaultResponse>?
                            ) {
                                if(response!!.isSuccessful){
                                    edt_lama_sewa_booking.setText("")
                                    Toast.makeText(activity, "Berhasil Daftar Booking", Toast.LENGTH_SHORT)
                                        .show()
                                }else{
                                    Toast.makeText(activity, "Gagal Response", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                                Toast.makeText(activity, "Gagal Mendapat Respon :${t}", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        })
                } else {
                    Toast.makeText(activity, "Response Gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(activity, "Gagal Upload :${t}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun updateDateInView(){
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_tanggal_mulai_kost_booking!!.text = sdf.format(cal.getTime())
    }
    private fun updateDateInView2(){
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf2 = SimpleDateFormat(myFormat, Locale.US)
        tv_tanggal_keluar_kost_booking!!.text = sdf2.format(cal.getTime())
    }

    private fun getPilihKamar() {
        RetrofitClient.instance.getKamar("get_kamar_kosong")
            .enqueue(object : Callback<KamarResponse> {
                override fun onResponse(
                    call: Call<KamarResponse>?,
                    response: Response<KamarResponse>?
                ) {
                    if (response!!.isSuccessful) {
                        val data = response.body().data!!.toTypedArray()
                        val listIdKamar: MutableList<String> = ArrayList()
                        val listSpinner: MutableList<String> = ArrayList()
                        for (i in data.indices) {
                            listIdKamar.add(data[i]!!.idKamar!!)
                            listSpinner.add(data[i]!!.namaKamar!! + " (Rp. " + data[i]!!.hargaKamar + ")")
                        }
                        val pk =
                            activity?.let {
                                ArrayAdapter(it, android.R.layout.simple_spinner_item, listSpinner)
                            }
                        pk!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerPilihKamar!!.adapter = pk
                        spinnerPilihKamar!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                                val idKamarSelected: String = listIdKamar[position]
                                idKamar = idKamarSelected
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>?) {
                                // your code here
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<KamarResponse>?, t: Throwable?) {
                    Toast.makeText(activity, "Response :${t}", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun getPilihPengguna() {
        RetrofitClient.instance.getUser("get_akun_umum")
            .enqueue(object : Callback<PenggunaResponse> {
                override fun onResponse(
                    call: Call<PenggunaResponse>?,
                    response: Response<PenggunaResponse>?
                ) {
                    if (response!!.isSuccessful) {
                        val data = response.body().data!!.toTypedArray()
                        val listIdPengguna: MutableList<String> = ArrayList()
                        val listSpinner: MutableList<String> = ArrayList()
                        for (i in data.indices) {
                            listIdPengguna.add(data[i]!!.idAkun!!)
                            listSpinner.add(data[i]!!.namaAkun!!)
                        }
                        val pp =
                            activity?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,listSpinner) }
                        pp!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerPilihPengguna!!.adapter = pp
                        spinnerPilihPengguna!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                                val idAkun: String = listIdPengguna[position]
                                idPengguna = idAkun
                            }

                            override fun onNothingSelected(parentView: AdapterView<*>?) {
                                // your code here
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<PenggunaResponse>?, t: Throwable?) {
                    Toast.makeText(activity, "Response :${t}", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tv_cara_bayar_booking.text = "Cara Bayar"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
