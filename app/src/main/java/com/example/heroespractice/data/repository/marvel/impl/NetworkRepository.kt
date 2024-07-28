package com.example.heroespractice.data.repository.marvel.impl

import android.util.Log
import com.example.heroespractice.data.network.marvel.MarvelInterface
import com.example.heroespractice.data.repository.marvel.MarvelRepository
import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.model.characters.mapper.networkToDomain
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.domain.model.comics.PlotType
import com.example.heroespractice.domain.model.comics.mapper.networkToDomain
import com.example.heroespractice.domain.utils.generalNetworkMapper
import io.reactivex.Single

class NetworkRepository(
	private val apiInterface: MarvelInterface,
) : MarvelRepository {
	override fun getCharacters(): Single<List<CharacterDomain>> {
		return generalNetworkMapper(apiInterface.getCharacters()) { characterDto ->
			Log.e(
				TAG,
				"CharacterDto.networkToDomain(): CharacterDomain domain: $characterDto"
			)
			characterDto.networkToDomain()
		}.doOnSuccess { characters ->
			Log.e(TAG, "doOnSuccess: $characters")
		}.doOnError { error ->
			Log.e(TAG, ".doOnError: ${error.message}")
		}
	}

	override fun getCharacterByCharacterId(characterId: Long): Single<List<CharacterDomain>> =
		generalNetworkMapper(apiInterface.getCharacterByCharacterId(characterId)) { characterDto ->
			characterDto.networkToDomain()
		}

	override fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		generalNetworkMapper(apiInterface.getComicsByCharacterId(characterId)) { comicDto ->
			comicDto.networkToDomain(PlotType.COMIC)
		}

	override fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		generalNetworkMapper(apiInterface.getEventsByCharacterId(characterId)) { comicDto ->
			comicDto.networkToDomain(PlotType.EVENT)
		}

	companion object {
		private const val TAG = "getCharacters() in NetworkRepository"
	}
}