package com.huda.booking.rest

import com.huda.booking.model.DefaultResponse
import com.huda.booking.model.LoginResponse
import com.huda.booking.model.ResultResponse
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

    @Multipart
    @POST("images/upload.php")
    fun postImage(@Part image: MultipartBody.Part
    ): Call<ResultResponse?>

}