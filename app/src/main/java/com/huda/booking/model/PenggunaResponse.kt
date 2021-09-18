package com.huda.booking.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PenggunaResponse(

	@field:SerializedName("data")
	val data: List<DataPengguna?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataPengguna(

	@field:SerializedName("alamat_akun")
	val alamatAkun: String? = null,

	@field:SerializedName("tempat_lahir_akun")
	val tempatLahirAkun: String? = null,

	@field:SerializedName("rule_akun")
	val ruleAkun: String? = null,

	@field:SerializedName("nama_akun")
	val namaAkun: String? = null,

	@field:SerializedName("password_akun")
	val passwordAkun: String? = null,

	@field:SerializedName("no_hp_akun")
	val noHpAkun: String? = null,

	@field:SerializedName("foto_akun")
	val fotoAkun: String? = null,

	@field:SerializedName("tgl_lahir_akun")
	val tglLahirAkun: String? = null,

	@field:SerializedName("id_akun")
	val idAkun: String? = null,

	@field:SerializedName("username_akun")
	val usernameAkun: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null
) : Parcelable
