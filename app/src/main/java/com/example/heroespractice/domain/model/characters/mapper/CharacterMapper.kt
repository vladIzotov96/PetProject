package com.example.heroespractice.domain.model.characters.mapper

import android.icu.text.SimpleDateFormat
import com.example.heroespractice.data.database.table.CharacterEntity
import com.example.heroespractice.data.network.marvel.dto.CharacterDto
import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.domain.utils.UrlType
import com.example.heroespractice.domain.utils.isContainHttps
import com.example.heroespractice.utils.PLACEHOLDER_VALUE_FOR_NUMBERS
import java.util.Date
import java.util.Locale

fun CharacterDto.networkToDomain(): CharacterDomain = this.let { characterDto ->
	val idCharacter = characterDto.id ?: System.currentTimeMillis()
	CharacterDomain(
		id = idCharacter,
		name = fillCharacterName(
			name = characterDto.name.orEmpty(),
			id = idCharacter,
		),
		isDescription = characterDto.description?.isNotEmpty() ?: false,
		description = fillCharacterDescription(
			description = characterDto.description.orEmpty()
		),
		thumbnail = "${
			isContainHttps(
				url = characterDto.thumbnail?.path.orEmpty()
			)
		}.${characterDto.thumbnail?.extension.orEmpty()}",
		comicsQuantity = characterDto.comics?.available ?: PLACEHOLDER_VALUE_FOR_NUMBERS,
		seriesQuantity = characterDto.series?.available ?: PLACEHOLDER_VALUE_FOR_NUMBERS,
		storiesQuantity = characterDto.stories?.available ?: PLACEHOLDER_VALUE_FOR_NUMBERS,
		eventsQuantity = characterDto.events?.available ?: PLACEHOLDER_VALUE_FOR_NUMBERS,
		plotUrl = isContainHttps(
			url = characterDto.urls?.firstOrNull {
				it?.type == UrlType.COMIC_LINK.type && !it.url.isNullOrEmpty()
			}?.url ?: ""
		)
	)
}

fun CharacterEntity.databaseToDomain(): CharacterDomain = this.let { characterDb ->
	CharacterDomain(
		id = characterDb.id,
		name = characterDb.name,
		isDescription = characterDb.isDescription,
		description = characterDb.description,
		thumbnail = characterDb.thumbnail,
		comicsQuantity = characterDb.comicsQuantity,
		seriesQuantity = characterDb.seriesQuantity,
		storiesQuantity = characterDb.storiesQuantity,
		eventsQuantity = characterDb.eventsQuantity,
		plotUrl = characterDb.plotsUrl,
	)
}

fun CharacterDomain.domainToDatabase(): CharacterEntity = this.let { characterDomain ->
	CharacterEntity(
		id = characterDomain.id,
		name = characterDomain.name,
		isDescription = characterDomain.isDescription,
		description = characterDomain.description,
		thumbnail = characterDomain.thumbnail,
		comicsQuantity = characterDomain.comicsQuantity,
		seriesQuantity = characterDomain.seriesQuantity,
		storiesQuantity = characterDomain.storiesQuantity,
		eventsQuantity = characterDomain.eventsQuantity,
		plotsUrl = characterDomain.plotUrl,
		dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
	)
}

private fun fillCharacterName(name: String, id: Long): String {
	return name.ifEmpty {
		if (id == PLACEHOLDER_VALUE_FOR_NUMBERS.toLong()) {
			"Character name unknown."
		} else {
			id.toString()
		}
	}
}

private fun fillCharacterDescription(description: String): String =
	description.ifEmpty { "Character does not have a short description." }