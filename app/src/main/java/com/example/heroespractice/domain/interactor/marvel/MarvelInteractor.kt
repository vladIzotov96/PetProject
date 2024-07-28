package com.example.heroespractice.domain.interactor.marvel

import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import io.reactivex.Single

interface MarvelInteractor {

	fun getCharacters(): Single<List<CharacterPresentation>>

	fun getCharacterByCharacterId(characterId: Long): Single<CharacterPresentation>

	fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>>

	fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>>
}