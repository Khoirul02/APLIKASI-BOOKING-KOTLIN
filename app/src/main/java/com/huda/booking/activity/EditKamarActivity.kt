package com.huda.booking.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.booking.R
import com.huda.booking.helper.Config
import com.huda.booking.model.DefaultResponse
import com.huda.booking.model.ResultResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.activity_edit_kamar.*
import kotlinx.android.synthetic.main.activity_edit_kamar.edt_fasilitas_kamar
import kotlinx.android.synthetic.main.activity_edit_kamar.edt_harga_kamar
import kotlinx.android.synthetic.main.activity_edit_kamar.edt_nama_kamar
import kotlinx.android.synthetic.main.activity_edit_kamar.tv_kategori_kamar
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION", "SENSELESS_COMPARISON")
class EditKamarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var listJenisKategori = arrayOf("Laki-laki", "Perempuan")
    var spinner: Spinner? = null
    var urlFotoKamar : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kamar)
        supportActionBar!!.setTitle("Edit Kamar")
        spinner = this.spn_kategori_kamar
        spinner!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listJenisKategori)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa
        edt_nama_kamar.setText (intent.getStringExtra("nama_kamar"))
        edt_fasilitas_kamar.setText (intent.getStringExtra("fasilitas_kamar"))
        edt_harga_kamar.setText (intent.getStringExtra("harga_kamar"))
        Glide.with(this).load(Config.BASE_URL_PHOTO+intent.getStringExtra("foto_kamar"))
            .apply(RequestOptions().override(300, 300)).into(iv_foto_kamar)
        btn_pilih_foto_kamar.setOnClickListener {
            pickImage()
        }
        btn_edit_data_kamar.setOnClickListener {
            val id = intent.getStringExtra("id_kamar")
            val foto = intent.getStringExtra("foto_kamar")
            val status = intent.getStringExtra("status_kamar")
            if (urlFotoKamar!! === ""){
                RetrofitClient.instance.updateKamar(id,edt_nama_kamar.text.toString().trim(), edt_fasilitas_kamar.text.toString().trim(),
                    spn_kategori_kamar.selectedItem.toString(), edt_harga_kamar.text.toString().trim(),status,foto, "update_kamar")
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(
                            call: Call<DefaultResponse>?,
                            response: Response<DefaultResponse>?
                        ) {
                            if (response!!.isSuccessful){
                                if (response.body().status == 1){
                                    edt_nama_kamar.setText("")
                                    edt_fasilitas_kamar.setText("")
                                    edt_harga_kamar.setText("")
                                    Toast.makeText(this@EditKamarActivity,"Berhasil Edit Data Kamar", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this@EditKamarActivity,"Gagal Edit Data Kamar", Toast.LENGTH_LONG).show()
                                }
                            }else{
                                Toast.makeText(this@EditKamarActivity,"Response Tidak Ada", Toast.LENGTH_LONG).show()
                            }
                        }
                        override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                            Toast.makeText(this@EditKamarActivity,"Response Gagal : ${t}", Toast.LENGTH_LONG).show()
                        }
                    })
            }else{
                uploadImage(id,status)
            }
        }
    }

    private fun pickImage() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
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
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
                iv_foto_kamar.setImageBitmap(bitmapImage)
            }
            if (uri != null) {
                val imageBitmap = uriToBitmap(uri)
                iv_foto_kamar.setImageBitmap(imageBitmap)
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
                urlFotoKamar = filePath
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

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tv_kategori_kamar.text = "Kategori Kamar"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun uploadImage(id : String, status : String ) {
        val f = File(urlFotoKamar)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        val part = MultipartBody.Part.createFormData("uploaded_file", f.toString(), requestFile)
        val resultCAll: Call<ResultResponse?>? = RetrofitClient.instance.postImage(part)
        resultCAll!!.enqueue(object : Callback<ResultResponse?> {
            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(this@EditKamarActivity, "Response Gagal", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<ResultResponse?>?,
                response: Response<ResultResponse?>?
            ) {
                if (response!!.isSuccessful){
                    if (response.body()!!.result.equals("success")){
                        RetrofitClient.instance.updateKamar(id,edt_nama_kamar.text.toString().trim(), edt_fasilitas_kamar.text.toString().trim(),
                            spn_kategori_kamar.selectedItem.toString(), edt_harga_kamar.text.toString().trim(),status,f.getName(), "update_kamar")
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onFailure(
                                    call: Call<DefaultResponse>?,
                                    t: Throwable?
                                ) {
                                    Toast.makeText(this@EditKamarActivity, "Respon Gagal: ${t}", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                override fun onResponse(
                                    call: Call<DefaultResponse>?,
                                    response: Response<DefaultResponse>?
                                ) {
                                    if (response?.body()?.status == 1){
                                        edt_nama_kamar.setText("")
                                        edt_fasilitas_kamar.setText("")
                                        edt_harga_kamar.setText("")
                                        Toast.makeText(this@EditKamarActivity, "Berhasil Edit Data", Toast.LENGTH_SHORT)
                                            .show()
                                    }else{
                                        Toast.makeText(this@EditKamarActivity, "Gagal Edit Data", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                            })
                    }else{
                        Toast.makeText(this@EditKamarActivity, "Gagal Mengirim Gambar", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(this@EditKamarActivity, "Reponse Gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })
    }
}
