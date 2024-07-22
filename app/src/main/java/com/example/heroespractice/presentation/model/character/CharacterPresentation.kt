package com.example.heroespractice.presentation.model.character

data class CharacterPresentation(
	val id: Long,
	val name: String,
	val isDescription: Boolean,
	val description: String,
	val thumbnail: String,
	val comicsQuantity: Int,
	val comicsDescriptionQuantity: String,
	val eventsQuantity: Int,
	val isNeedComicsRequest: Boolean,
	val isNeedEventsRequest: Boolean,
	val plotsUrl: String,
)