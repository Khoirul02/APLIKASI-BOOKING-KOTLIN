package com.huda.booking.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat

import com.huda.booking.R
import com.huda.booking.model.DefaultResponse
import com.huda.booking.model.ResultResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_daftar_kamar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DaftarKamarFragment : Fragment(), AdapterView.OnItemSelectedListener {
    var listJenisKategori = arrayOf("Laki-laki", "Perempuan")
    var spinner: Spinner? = null
    var urlFotoKamar : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_kamar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = this.spn_kategori_kamar
        spinner!!.onItemSelectedListener = this
        val aa =
            activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, listJenisKategori) }
        aa?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa
        btn_pilih_foto_kamar.setOnClickListener {
            pickImage()
        }
        btn_kirim_data_kamar.setOnClickListener {
            if (edt_nama_kamar.text.toString().trim() == "" || edt_fasilitas_kamar.text.toString().trim() == "" || edt_harga_kamar.text.toString().trim() == ""){
                Toast.makeText(activity, "Data Belum Lengkap", Toast.LENGTH_SHORT)
                    .show()
            }else{
                uploadImage()
            }
        }
    }
    private fun pickImage() {
        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_GRANTED) {
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
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
        val cursor = activity?.contentResolver?.query(uri, filePathColumn, null, null, null)
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
        return MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
    }
    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tv_kategori_kamar.text = "Kategori Kamar"
    }

    private fun uploadImage() {
        val f = File(urlFotoKamar)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        val part = MultipartBody.Part.createFormData("uploaded_file", f.toString(), requestFile)
        val resultCAll: Call<ResultResponse?>? = RetrofitClient.instance.postImage(part)
        resultCAll!!.enqueue(object : Callback<ResultResponse?> {
            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(activity, "Response Gagal", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<ResultResponse?>?,
                response: Response<ResultResponse?>?
            ) {
                if (response!!.isSuccessful){
                    if (response.body()!!.result.equals("success")){
                        RetrofitClient.instance.insertKamar(edt_nama_kamar.text.toString().trim(), edt_fasilitas_kamar.text.toString().trim(),
                            spn_kategori_kamar.selectedItem.toString(), edt_harga_kamar.text.toString().trim(),"KOSONG",f.getName(), "insert_kamar")
                            .enqueue(object : Callback<DefaultResponse> {
                                override fun onFailure(
                                    call: Call<DefaultResponse>?,
                                    t: Throwable?
                                ) {
                                    Toast.makeText(activity, "Respon Gagal: ${t}", Toast.LENGTH_SHORT)
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
                                        Toast.makeText(activity, "Berhasil Mendaftar", Toast.LENGTH_SHORT)
                                            .show()
                                    }else{
                                        Toast.makeText(activity, "Gagal Mendaftar", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }

                            })
                    }else{
                        Toast.makeText(activity, "Gagal Mengirim Gambar", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(activity, "Reponse Gagal", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })
    }

}
