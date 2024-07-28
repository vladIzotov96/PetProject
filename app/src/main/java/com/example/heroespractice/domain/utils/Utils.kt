package com.example.heroespractice.domain.utils

import com.example.heroespractice.data.database.table.CharacterEntity
import com.example.heroespractice.data.network.marvel.dto.ResponseDto
import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.model.characters.mapper.databaseToDomain
import io.reactivex.Single

fun <T, R> generalNetworkMapper(
	response: Single<ResponseDto<T>>,
	networkToDomain: (T) -> R
): Single<List<R>> {
	return response.map { responseDto ->
		responseDto.data?.results?.mapNotNull {
			it?.let {
				networkToDomain(it)
			}
		} ?: emptyList()
	}.flatMap {
		if (it.isNotEmpty()) Single.just(it) else Single.error(NoSuchElementException())
	}
}

fun generalDatabaseMapper(
	response: Single<List<CharacterEntity>>
): Single<List<CharacterDomain>> {
	return response.flatMap { listCharacterDb ->
		if (listCharacterDb.isNotEmpty()) {
			Single.just(listCharacterDb.map { characterDb ->
				characterDb.databaseToDomain()
			})
		} else {
			Single.error(NoSuchElementException())
		}
	}
}

fun <T> filteredListByLengthParameter(
	list: List<T>,
	minLength: Int,
	lengthSelector: (T) -> Int
): List<T> {
	return list.filter { item ->
		lengthSelector(item) > minLength
	}
}

fun isContainHttps(url: String): String {
	return if (url.startsWith("https")) {
		url
	} else {
		if (url.startsWith("http")) {
			url.replaceFirst("http", "https")
		} else {
			""
		}
	}
}

enum class UrlType(val type: String) {
	COMIC_LINK("comiclink"),
}