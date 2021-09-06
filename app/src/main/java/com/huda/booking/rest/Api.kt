package com.huda.booking.rest

import com.huda.booking.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @FormUrlEncoded
    @POST("restapi_booking_kost.php")
    fun userLogin(
        @Field("username_akun") username_akun:String,
        @Field("password_akun") password_akun: String,
        @Query("function") function:String
    ): Call<LoginResponse>
}