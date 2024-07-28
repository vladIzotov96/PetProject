package com.example.heroespractice.data.network.marvel.dto

import com.google.gson.annotations.SerializedName

class ResponseDto<T>(

	@SerializedName("data") val data: DataDto<T>?,

	)

class DataDto<T>(

	@SerializedName("results") val results: List<T?>?,

	)