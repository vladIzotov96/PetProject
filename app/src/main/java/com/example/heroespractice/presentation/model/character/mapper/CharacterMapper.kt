package com.example.heroespractice.presentation.model.character.mapper

import com.example.heroespractice.domain.model.characters.CharacterDomain
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import com.example.heroespractice.presentation.utils.NUMBER_OF_PLOTS_TO_REQUEST
import com.example.heroespractice.presentation.utils.SINGULAR_NUMBER
import com.example.heroespractice.utils.PLACEHOLDER_VALUE_FOR_NUMBERS

fun CharacterDomain.domainToPresentation(): CharacterPresentation = this.let { characterDomain ->
	val comicsQuantityCharacter = characterDomain.comicsQuantity
	val eventsQuantityCharacter = characterDomain.eventsQuantity
	CharacterPresentation(
		id = characterDomain.id,
		name = characterDomain.name,
		isDescription = characterDomain.isDescription,
		description = characterDomain.description,
		thumbnail = characterDomain.thumbnail,
		comicsQuantity = comicsQuantityCharacter,
		comicsDescriptionQuantity = fillCharacterComicsQuantity(comicsQuantityCharacter),
		eventsQuantity = eventsQuantityCharacter,
		isNeedComicsRequest = fillCharacterIsNeedComicsRequest(comicsQuantityCharacter),
		isNeedEventsRequest = fillCharacterIsNeedEventsRequest(eventsQuantityCharacter),
		plotsUrl = characterDomain.plotUrl,
	)
}

private fun fillCharacterComicsQuantity(comicsQuantity: Int): String {
	return when (comicsQuantity) {
		PLACEHOLDER_VALUE_FOR_NUMBERS -> "No comics"
		SINGULAR_NUMBER -> "$SINGULAR_NUMBER comic"
		else -> "$comicsQuantity comics"
	}
}

private fun fillCharacterIsNeedComicsRequest(comicsQuantity: Int): Boolean {
	return comicsQuantity > NUMBER_OF_PLOTS_TO_REQUEST
}

private fun fillCharacterIsNeedEventsRequest(eventsQuantity: Int): Boolean {
	return eventsQuantity > NUMBER_OF_PLOTS_TO_REQUEST
}