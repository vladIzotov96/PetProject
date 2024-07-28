package com.example.heroespractice.data.repository.marvel.impl

import com.example.heroespractice.data.repository.marvel.MarvelRepository
import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.model.comics.PlotDomain
import io.reactivex.Completable
import io.reactivex.Single

class GeneralRepository(
	private val databaseRepository: DatabaseRepository,
	private val networkRepository: NetworkRepository,
) : MarvelRepository {
	override fun getCharacters(): Single<List<CharacterDomain>> {
		return databaseRepository.getCharacters()
			.onErrorResumeNext { throwable ->
				if (throwable is NoSuchElementException) {
					networkRepository.getCharacters()
						.flatMap { listCharacterDomainFromNetwork ->
							saveCharactersToDatabase(listCharacterDomainFromNetwork).andThen(
								Single.just(listCharacterDomainFromNetwork)
							)
						}
				} else {
					Single.error(throwable)
				}
			}
	}

	override fun getCharacterByCharacterId(characterId: Long): Single<List<CharacterDomain>> {
		return databaseRepository.getCharacterByCharacterId(characterId)
			.onErrorResumeNext { throwable ->
				if (throwable is NoSuchElementException) {
					networkRepository.getCharacterByCharacterId(characterId)
						.flatMap { listCharacterDomainNetwork ->
							Single.just(listCharacterDomainNetwork)
						}
				} else {
					Single.error(throwable)
				}
			}
	}

	override fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		networkRepository.getComicsByCharacterId(characterId)

	override fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>> =
		networkRepository.getEventsByCharacterId(characterId)

	private fun saveCharactersToDatabase(characters: List<CharacterDomain>): Completable =
		databaseRepository.insertCharacter(characters)
}