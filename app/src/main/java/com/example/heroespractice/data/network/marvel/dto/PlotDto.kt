package com.example.heroespractice.data.network.marvel.dto

import com.google.gson.annotations.SerializedName

/**Created class for comics, series, stories, and events APIs.*/
class PlotDto(

	@SerializedName("id") val id: Int?,

	@SerializedName("title") val title: String?,

	@SerializedName("thumbnail") val thumbnail: ThumbnailDto?,

	)