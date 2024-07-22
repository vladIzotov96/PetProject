package com.example.heroespractice.data.repository.marvel

import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.model.comics.PlotDomain
import io.reactivex.Single

interface MarvelRepository {

	fun getCharacters(): Single<List<CharacterDomain>>

	fun getCharacterByCharacterId(characterId: Long): Single<List<CharacterDomain>>

	fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>>

	fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>>

}