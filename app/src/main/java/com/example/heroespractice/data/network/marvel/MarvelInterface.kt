package com.example.heroespractice.data.network.marvel

import com.example.heroespractice.data.network.marvel.dto.CharacterDto
import com.example.heroespractice.data.network.marvel.dto.PlotDto
import com.example.heroespractice.data.network.marvel.dto.ResponseDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MarvelInterface {

	@GET("characters")
	fun getCharacters(): Single<ResponseDto<CharacterDto>>

	@GET("characters/{characterId}")
	fun getCharacterByCharacterId(@Path("characterId") characterId: Long): Single<ResponseDto<CharacterDto>>

	@GET("characters/{characterId}/comics")
	fun getComicsByCharacterId(@Path("characterId") characterId: Long): Single<ResponseDto<PlotDto>>

	@GET("characters/{characterId}/events")
	fun getEventsByCharacterId(@Path("characterId") characterId: Long): Single<ResponseDto<PlotDto>>

}