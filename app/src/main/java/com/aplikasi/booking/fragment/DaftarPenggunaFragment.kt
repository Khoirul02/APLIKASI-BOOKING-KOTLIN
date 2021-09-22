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
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.aplikasi.booking.model.ResultResponse
import com.aplikasi.booking.rest.RetrofitClient
import kotlinx.android.synthetic.main.fragment_daftar_pengguna.*
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
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DaftarPenggunaFragment : Fragment(), AdapterView.OnItemSelectedListener {
    var listJenisKelamin = arrayOf("Laki-laki", "Perempuan")
    var listRule = arrayOf("pemilik", "umum")
    var spinnerJenis: Spinner? = null
    var spinnerRule: Spinner? = null
    var urlFotoPengguna: String? = null
    var cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_pengguna, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerJenis = this.spn_jk_pengguna
        spinnerJenis!!.onItemSelectedListener = this
        val jk =
            activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item,
                    listJenisKelamin
                )
            }
        jk!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenis!!.adapter = jk

        spinnerRule = this.spn_rule_pengguna
        spinnerRule!!.onItemSelectedListener = this
        val rule =
            activity?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_item, listRule
                )
            }
        rule!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRule!!.adapter = rule
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        div_tanggal_lahir_pengguna.setOnClickListener {
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
        btn_pilih.setOnClickListener {
            if(btn_pilih.text.toString() == "SHOW"){
                edt_password_pengguna.transformationMethod = HideReturnsTransformationMethod.getInstance()
                btn_pilih.text = "HIDE"
            } else{
                edt_password_pengguna.transformationMethod = PasswordTransformationMethod.getInstance()
                btn_pilih.text = "SHOW"
            }
        }
        btn_pilih_foto_pengguna.setOnClickListener {
            pickImage()
        }
        btn_kirim_data_penggguna.setOnClickListener {
            if (edt_nama_penggguna.text.toString() == "" || edt_tl_pengguna.text.toString() == "" || edt_username_pengguna.text.toString() == "" || edt_password_pengguna.text.toString() == "" || edt_nohp_pengguna.text.toString() == "" || edt_alamat_pengguna.text.toString() == "") {
                Toast.makeText(activity, "Data Belum Lengkap", Toast.LENGTH_SHORT)
                    .show()
            } else {
                uploadImage()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tv_jk_pengguna.text = "Jenis Kelamin"
        tv_rule_pengguna.text = "Rule Pengguna"
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    private fun updateDateInView(){
        val myFormat = "yyyy-MM-dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tv_tanggal_lahir_pengguna!!.text = sdf.format(cal.getTime())
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
    @SuppressLint("UseRequireInsteadOfGet")
    private fun uriToImageFile(uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity!!.contentResolver.query(uri, filePathColumn, null, null, null)
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
    @SuppressLint("UseRequireInsteadOfGet")
    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
    }
    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }

    private fun uploadImage(){
        val f = File(urlFotoPengguna)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f)
        val part = MultipartBody.Part.createFormData("uploaded_file", f.toString(), requestFile)
        val resultCAll: Call<ResultResponse?>? = RetrofitClient.instance.postImage(part)
        resultCAll!!.enqueue(object : Callback<ResultResponse?> {
            override fun onResponse(
                call: Call<ResultResponse?>?,
                response: Response<ResultResponse?>?
            ) {
                if (response!!.isSuccessful){
                    RetrofitClient.instance.insertUser(edt_nama_penggguna.text.toString().trim(), edt_tl_pengguna.text.toString().trim(),
                        tv_tanggal_lahir_pengguna.text.toString().trim(), spn_jk_pengguna.selectedItem.toString(), edt_username_pengguna.text.toString().trim(),
                        edt_password_pengguna.text.toString().trim(),edt_alamat_pengguna.text.toString().trim(),edt_nohp_pengguna.text.toString().trim(),
                        f.getName(), spn_rule_pengguna.selectedItem.toString(),"insert_akun" )
                        .enqueue(object : Callback<DefaultResponse>{
                            override fun onResponse(
                                call: Call<DefaultResponse>?,
                                response: Response<DefaultResponse>?
                            ) {
                                edt_nama_penggguna.setText("")
                                edt_tl_pengguna.setText("")
                                edt_username_pengguna.setText("")
                                edt_password_pengguna.setText("")
                                edt_nohp_pengguna.setText("")
                                edt_alamat_pengguna.setText("")
                                Toast.makeText(activity, "Berhasil Mendaftar", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onFailure(call: Call<DefaultResponse>?, t: Throwable?) {
                                Toast.makeText(activity, "Gagal Response", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }else{
                    Toast.makeText(activity, "Tidak Ada Reponse Image", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResultResponse?>?, t: Throwable?) {
                Toast.makeText(activity, "Gagal Upload :${t}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}
