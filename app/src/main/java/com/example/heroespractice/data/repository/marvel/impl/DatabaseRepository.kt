package com.example.heroespractice.data.repository.marvel.impl

import android.util.Log
import com.example.heroespractice.data.database.table.CharacterDao
import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.model.characters.mapper.domainToDatabase
import com.example.heroespractice.domain.utils.generalDatabaseMapper
import io.reactivex.Completable
import io.reactivex.Single

class DatabaseRepository(
	private val characterDao: CharacterDao,
) {
	fun getCharacters(): Single<List<CharacterDomain>> =
		generalDatabaseMapper(characterDao.getCharacters())

	fun getCharacterByCharacterId(characterId: Long): Single<List<CharacterDomain>> =
		generalDatabaseMapper(characterDao.getCharacterByCharacterId(characterId))

	fun insertCharacter(characters: List<CharacterDomain>): Completable {
		return characterDao.insertCharacter(characters.map { character ->
			Log.e(TAG, "CharacterDomain.domainToDatabase(): CharacterDb: $character")
			character.domainToDatabase()
		}).doOnComplete {
			Log.e(TAG, "doOnComplete")
		}.doOnError { error ->
			Log.e(
				TAG,
				"doOnError: ${error.message}"
			)
		}
	}

	companion object {
		private const val TAG =
			"insertCharacter(characters: List<CharacterDomain>) in DatabaseRepository"
	}
}