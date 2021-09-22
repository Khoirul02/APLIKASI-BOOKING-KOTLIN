package com.huda.booking.rest

import com.huda.booking.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun userLogin(
        @Field("username_akun") username_akun:String,
        @Field("password_akun") password_akun: String,
        @Query("function") function:String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun insertBooking(
        @Field("id_akun") id_akun:String,
        @Field("id_kamar") id_kamar:String,
        @Field("tanggal_mulai_kost_booking") tanggal_mulai_kost_booking:String,
        @Field("tanggal_keluar_kost_booking") tanggal_keluar_kost_booking:String,
        @Field("lama_sewa_booking") lama_sewa_booking:String,
        @Field("cara_bayar_booking") cara_bayar_booking: String,
        @Field("bukti_pembayaran_booking") bukti_pembayaran_booking: String,
        @Field("status_booking") status_booking: String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun insertUser(
        @Field("nama_akun") nama_akun:String,
        @Field("tempat_lahir_akun") tempat_lahir_akun:String,
        @Field("tgl_lahir_akun") tgl_lahir_akun:String,
        @Field("jenis_kelamin") jenis_kelamin:String,
        @Field("username_akun") username_akun:String,
        @Field("password_akun") password_akun: String,
        @Field("alamat_akun") alamat_akun: String,
        @Field("no_hp_akun") no_hp_akun: String,
        @Field("foto_akun") foto_akun: String,
        @Field("rule_akun") rule_akun: String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun updateUser(
        @Field("id_akun") id_akun:String,
        @Field("nama_akun") nama_akun:String,
        @Field("tempat_lahir_akun") tempat_lahir_akun:String,
        @Field("tgl_lahir_akun") tgl_lahir_akun:String,
        @Field("jenis_kelamin") jenis_kelamin:String,
        @Field("username_akun") username_akun:String,
        @Field("password_akun") password_akun: String,
        @Field("alamat_akun") alamat_akun: String,
        @Field("no_hp_akun") no_hp_akun: String,
        @Field("foto_akun") foto_akun: String,
        @Field("rule_akun") rule_akun: String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun insertKamar(
        @Field("nama_kamar") nama_kamar:String,
        @Field("fasilitas_kamar") fasilitas_kamar:String,
        @Field("kategori_kamar") kategori_kamar:String,
        @Field("harga_kamar") harga_kamar:String,
        @Field("status_kamar") status_kamar: String,
        @Field("foto_kamar") foto_kamar:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun updateKamar(
        @Field("id_kamar") id_kamar:String,
        @Field("nama_kamar") nama_kamar:String,
        @Field("fasilitas_kamar") fasilitas_kamar:String,
        @Field("kategori_kamar") kategori_kamar:String,
        @Field("harga_kamar") harga_kamar:String,
        @Field("status_kamar") status_kamar: String,
        @Field("foto_kamar") foto_kamar:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun deleteKamar(
        @Field("id_kamar") id_kamar:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun deleteUser(
        @Field("id_akun") id_akun:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun deleteBooking(
        @Field("id_booking") id_booking:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun verifBooking(
        @Field("id_booking") id_booking:String,
        @Field("status_booking") status_booking:String,
        @Query("function") function:String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun getBookingByIdUser(
        @Field("id_akun") id_akun:String,
        @Query("function") function:String
    ): Call<BookingResponse>

    @Multipart
    @POST("images/upload.php")
    fun postImage(@Part image: MultipartBody.Part
    ): Call<ResultResponse?>

    @GET("restapi_booking_kost.php")
    fun getKamar(
        @Query("function") function: String
    ): Call<KamarResponse>

    @GET("restapi_booking_kost.php")
    fun getBooking(
        @Query("function") function: String
    ): Call<BookingResponse>

    @GET("restapi_booking_kost.php")
    fun getUser(
        @Query("function") function: String
    ): Call<PenggunaResponse>

}