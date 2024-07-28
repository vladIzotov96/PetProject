package com.example.heroespractice.domain.model.characters

data class CharacterDomain(
	val id: Long,
	val name: String,
	val isDescription: Boolean,
	val description: String,
	val thumbnail: String,
	val comicsQuantity: Int,
	val seriesQuantity: Int,
	val storiesQuantity: Int,
	val eventsQuantity: Int,
	val plotUrl: String,
)