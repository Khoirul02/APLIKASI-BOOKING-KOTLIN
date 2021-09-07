package com.huda.booking

import android.R.attr.path
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.BitmapFactory.decodeFile
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huda.booking.model.DefaultResponse
import com.huda.booking.model.ResultResponse
import com.huda.booking.rest.RetrofitClient
import com.obsez.android.lib.filechooser.ChooserDialog
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


@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RegistrasiActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var listJenisKelamin = arrayOf("Laki-laki", "Perempuan")
    var spinner: Spinner? = null
    var urlFotoPengguna : String? = null
    var cal = Calendar.getInstance()
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
            ChooserDialog().with(this)
                .disableTitle(true)
                .withStartFile(path.toString())
                .withChosenListener { path, pathFile ->
                    Toast.makeText(this@RegistrasiActivity, "FILE: $path", Toast.LENGTH_SHORT)
                        .show()
                    urlFotoPengguna = path
                    iv_foto_pengguna.setImageBitmap(decodeFile(pathFile.toString()))
                }
                .build()
                .show()
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
