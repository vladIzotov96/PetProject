package com.example.heroespractice.data.network.pixabay

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface PixabayInterface {

	@GET
	fun downloadFile(@Url url: String): Single<Response<ResponseBody>>

}