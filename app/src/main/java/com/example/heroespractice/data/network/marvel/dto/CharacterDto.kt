package com.example.heroespractice.data.network.marvel.dto

import com.google.gson.annotations.SerializedName

class CharacterDto(

	@SerializedName("id") val id: Long?,

	@SerializedName("name") val name: String?,

	@SerializedName("description") val description: String?,

	@SerializedName("thumbnail") val thumbnail: ThumbnailDto?,

	@SerializedName("comics") val comics: StorylinesDto?,

	@SerializedName("series") val series: StorylinesDto?,

	@SerializedName("stories") val stories: StorylinesDto?,

	@SerializedName("events") val events: StorylinesDto?,

	@SerializedName("urls") val urls: List<UrlDto?>?,

	)

class ThumbnailDto(

	@SerializedName("path") val path: String?,

	@SerializedName("extension") val extension: String?,

	)

/**Created classes for comics, series, stories, and events fields.*/
class StorylinesDto(

	@SerializedName("available") val available: Int?,

	)

class UrlDto(

	@SerializedName("type") val type: String?,

	@SerializedName("url") val url: String?,

	)