package com.huda.booking

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.huda.booking.model.DefaultResponse
import com.huda.booking.model.ResultResponse
import com.huda.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.activity_registrasi.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "UNREACHABLE_CODE")
class RegistrasiActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var listJenisKelamin = arrayOf("Laki-laki", "Perempuan")
    var spinner: Spinner? = null
    var urlFotoPengguna : String? = null
    var cal = Calendar.getInstance()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)
        supportActionBar!!.hide()
        spinner = this.spn_jk_pengguna
        spinner!!.onItemSelectedListener = this
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, listJenisKelamin)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = aa
        btn_pilih_foto_pengguna.setOnClickListener {
            pickImage()
        }
        val dateSetListener =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        div_tanggal_lahir_pengguna.setOnClickListener {
            DatePickerDialog(this@RegistrasiActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        btn_kirim_data_penggguna.setOnClickListener {
            if (edt_nama_penggguna.text.toString() == "" || edt_tl_pengguna.text.toString() == "" || edt_username_pengguna.text.toString() == "" || edt_password_pengguna.text.toString() == "" || edt_nohp_pengguna.text.toString() == "" || edt_alamat_pengguna.text.toString() == "") {
                Toast.makeText(this@RegistrasiActivity, "Data Belum Lengkap", Toast.LENGTH_SHORT)
                    .show()
            } else {
                uploadImage()
            }
        }
        btn_pilih.setOnClickListener {
            if(btn_pilih.text.toString() == "SHOW"){
                edt_password_pengguna.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btn_pilih.text = "HIDE"
            } else{
                edt_password_pengguna.transformationMethod = PasswordTransformationMethod.getInstance()
                btn_pilih.text = "SHOW"
            }
        }
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
                iv_foto_pengguna.setImageBitmap(bitmapImage)
            }
            if (uri != null) {
                val imageBitmap = uriToBitmap(uri)
                iv_foto_pengguna.setImageBitmap(imageBitmap)
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
                urlFotoPengguna = filePath
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

    private fun updateDateInView(){
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_tanggal_lahir_pengguna!!.text = sdf.format(cal.getTime())
    }
    private fun uploadImage(){
        val f = File(urlFotoPengguna)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        val part = MultipartBody.Part.createFormData("uploaded_file", f.toString(), requestFile)
        val resultCAll: Call<ResultResponse?>? = RetrofitClient.instance.postImage(part)
        resultCAll!!.enqueue(object : Callback<ResultResponse?>{
            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(this@RegistrasiActivity, "Gagal Upload :${t}", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<ResultResponse?>?,
                response: Response<ResultResponse?>?
            ) {
                if (response!!.isSuccessful){
                    if (response.body()!!.result.equals("success")){
                        RetrofitClient.instance.insertUser(edt_nama_penggguna.text.toString().trim(), edt_tl_pengguna.text.toString().trim(),
                            tv_tanggal_lahir_pengguna.text.toString().trim(), spn_jk_pengguna.selectedItem.toString(), edt_username_pengguna.text.toString().trim(),
                            edt_password_pengguna.text.toString().trim(),edt_alamat_pengguna.text.toString().trim(),edt_nohp_pengguna.text.toString().trim(),
                            f.getName(),"umum","insert_akun" )
                            .enqueue(object : Callback<DefaultResponse>{
                                override fun onFailure(
                                    call: Call<DefaultResponse>?,
                                    t: Throwable?
                                ) {
                                    Toast.makeText(this@RegistrasiActivity, "Response Gagal", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                override fun onResponse(
                                    call: Call<DefaultResponse>?,
                                    response: Response<DefaultResponse>?
                                ) {
                                    if (response!!.body().status!! == 1){
                                        edt_nama_penggguna.setText("")
                                        edt_tl_pengguna.setText("")
                                        edt_username_pengguna.setText("")
                                        edt_password_pengguna.setText("")
                                        edt_nohp_pengguna.setText("")
                                        edt_alamat_pengguna.setText("")
                                        Toast.makeText(this@RegistrasiActivity, "Berhasil Mendaftar", Toast.LENGTH_SHORT)
                                            .show()
                                    }else{
                                        Toast.makeText(this@RegistrasiActivity, "Gagal Mendaftar", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            })
                    }else{
                        Toast.makeText(this@RegistrasiActivity, "Reponse Gagal", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tv_jk_pengguna.text = "Jenis Kelamin"
    }

}
