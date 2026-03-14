package com.houstd.app.api

import com.houstd.app.data.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Apiservice {
    @POST("register")
    fun PostRegister(@Body user: User?): Call<ResponseBody?>?

    @POST("login")
    fun PostLogin(@Body user: User?): Call<ResponseBody?>?

    @POST("logout")
    fun PostLogout(@Header("Authorization") token: String?): Call<ResponseBody?>?
}