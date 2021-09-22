package com.aplikasi.booking.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KamarResponse(

	@field:SerializedName("data")
	val data: List<DataItemKamar?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class DataItemKamar(

	@field:SerializedName("kategori_kamar")
	val kategoriKamar: String? = null,

	@field:SerializedName("status_kamar")
	val statusKamar: String? = null,

	@field:SerializedName("foto_kamar")
	val fotoKamar: String? = null,

	@field:SerializedName("harga_kamar")
	val hargaKamar: String? = null,

	@field:SerializedName("nama_kamar")
	val namaKamar: String? = null,

	@field:SerializedName("fasilitas_kamar")
	val fasilitasKamar: String? = null,

	@field:SerializedName("id_kamar")
	val idKamar: String? = null
) : Parcelable
