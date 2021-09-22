package com.huda.booking.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookingResponse(

	@field:SerializedName("data")
	val data: List<BookingItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable

@Parcelize
data class BookingItem(

	@field:SerializedName("kategori_kamar")
	val kategoriKamar: String? = null,

	@field:SerializedName("cara_bayar_booking")
	val caraBayarBooking: String? = null,

	@field:SerializedName("bukti_pembayaran_booking")
	val buktiPembayaranBooking: String? = null,

	@field:SerializedName("password_akun")
	val passwordAkun: String? = null,

	@field:SerializedName("no_hp_akun")
	val noHpAkun: String? = null,

	@field:SerializedName("harga_kamar")
	val hargaKamar: String? = null,

	@field:SerializedName("tgl_lahir_akun")
	val tglLahirAkun: String? = null,

	@field:SerializedName("tanggal_mulai_kost_booking")
	val tanggalMulaiKostBooking: String? = null,

	@field:SerializedName("jumlah_bayar_booking")
	val jumlahBayarBooking: String? = null,

	@field:SerializedName("alamat_akun")
	val alamatAkun: String? = null,

	@field:SerializedName("nama_akun")
	val namaAkun: String? = null,

	@field:SerializedName("foto_akun")
	val fotoAkun: String? = null,

	@field:SerializedName("nama_kamar")
	val namaKamar: String? = null,

	@field:SerializedName("fasilitas_kamar")
	val fasilitasKamar: String? = null,

	@field:SerializedName("id_akun")
	val idAkun: String? = null,

	@field:SerializedName("id_kamar")
	val idKamar: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("tempat_lahir_akun")
	val tempatLahirAkun: String? = null,

	@field:SerializedName("rule_akun")
	val ruleAkun: String? = null,

	@field:SerializedName("status_booking")
	val statusBooking: String? = null,

	@field:SerializedName("lama_sewa_booking")
	val lamaSewaBooking: String? = null,

	@field:SerializedName("username_akun")
	val usernameAkun: String? = null,

	@field:SerializedName("id_booking")
	val idBooking: String? = null,

	@field:SerializedName("status_kamar")
	val statusKamar: String? = null,

	@field:SerializedName("foto_kamar")
	val fotoKamar: String? = null,

	@field:SerializedName("tanggal_keluar_kost_booking")
	val tanggalKeluarKostBooking: String? = null
) : Parcelable
