package com.example.heroespractice.domain.interactor.marvel.impl

import com.example.heroespractice.data.repository.marvel.MarvelRepository
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.domain.utils.filteredListByLengthParameter
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import com.example.heroespractice.presentation.model.character.mapper.domainToPresentation
import io.reactivex.Single

class InteractorImpl(
	private val repository: MarvelRepository,
) : MarvelInteractor {
	private companion object {
		const val LENGTH_NAME = 0
	}

	override fun getCharacters(): Single<List<CharacterPresentation>> {
		return repository.getCharacters()
			.map { listCharacterDomain ->
				filteredListByLengthParameter(
					listCharacterDomain,
					LENGTH_NAME
				) { characterDomain -> characterDomain.name.length }.map { characterDomain ->
					characterDomain.domainToPresentation()
				}
			}
			.flatMap {
				if (it.isNotEmpty()) Single.just(it) else Single.error(NoSuchElementException())
			}
	}

	override fun getCharacterByCharacterId(characterId: Long): Single<CharacterPresentation> =
		repository.getCharacterByCharacterId(characterId)
			.map { listCharacterDomain ->
				listCharacterDomain.first().domainToPresentation()
			}

	override fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		repository.getComicsByCharacterId(characterId)
			.map { listComicDomain ->
				listComicDomain
			}

	override fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		repository.getEventsByCharacterId(characterId)
			.map { listEventsDomain ->
				listEventsDomain
			}
}