package com.aplikasi.booking.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.aplikasi.booking.R
import com.aplikasi.booking.model.DefaultResponse
import com.aplikasi.booking.model.ResultResponse
import com.aplikasi.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.activity_daftar_booking_pengguna.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DaftarBookingPenggunaActivity : AppCompatActivity() {
    var listCaraBayar = arrayOf("cash", "transfer")
    var spinnerCaraBayar: Spinner? = null
    var urlBuktiPembayaranBooking: String? = ""
    var idPengguna: String? = null
    var idKamar: String? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_booking_pengguna)
        idKamar = intent.getStringExtra("ID_KAMAR")
        idPengguna = intent.getStringExtra("ID_AKUN")
        edt_pengguna.setText (intent.getStringExtra("NAMA_AKUN"))
        edt_kamar.setText (intent.getStringExtra("NAMA_KAMAR"))
        spinnerCaraBayar = this.spn_cara_bayar_booking
        val cb = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCaraBayar)
        cb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCaraBayar!!.adapter = cb
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
                DatePickerDialog(
                    this,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
        }
        div_tanggal_keluar_kost_booking.setOnClickListener {
                DatePickerDialog(
                    this,
                    dateSetListener2,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
        }
        btn_pilih_foto_bukti_pembayaran_booking.setOnClickListener {
            pickImage()
        }
        btn_kirim_data_booking.setOnClickListener {
            if (edt_lama_sewa_booking.text.toString().trim() == ""){
                Toast.makeText(this, "Data Lama Sewa Masih Kosong", Toast.LENGTH_LONG).show()
            }else{
                if (urlBuktiPembayaranBooking!! == ""){
                    Toast.makeText(this, "Bukti Masih Belum DiUpload", Toast.LENGTH_LONG).show()
                }else{
                    uploadImage()
                }
            }
        }
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
                if(response!!.isSuccessful){
                    RetrofitClient.instance.insertBooking(idPengguna.toString(),idKamar.toString(),tv_tanggal_mulai_kost_booking.text.toString().trim(),tv_tanggal_keluar_kost_booking.text.toString().trim()
                        ,edt_lama_sewa_booking.text.toString().trim(), spn_cara_bayar_booking.selectedItem.toString(),f.getName(), "belum","insert_booking")
                        .enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>?,
                                response: Response<DefaultResponse>?
                            ) {
                                if(response!!.isSuccessful){
                                    edt_lama_sewa_booking.setText("")
                                    Toast.makeText(this@DaftarBookingPenggunaActivity, "Berhasil Daftar Booking", Toast.LENGTH_SHORT)
                                        .show()
                                }else{
                                    Toast.makeText(this@DaftarBookingPenggunaActivity, "Respon Gagal", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                                Toast.makeText(this@DaftarBookingPenggunaActivity, "Respon Gagal Tambah Data Booking :${t}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }else{
                    Toast.makeText(this@DaftarBookingPenggunaActivity, "Respon Gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(this@DaftarBookingPenggunaActivity, "Gagal Upload :${t}", Toast.LENGTH_SHORT)
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
    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_REQUEST_CODE
            )
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
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
    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
    }
    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }
}
