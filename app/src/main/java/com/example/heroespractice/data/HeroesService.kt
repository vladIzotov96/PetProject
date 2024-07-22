package com.example.heroespractice.data

class HeroesService {
	var clickedCharacter: Long = -1
	var clickedPlot = mutableMapOf(
		"type" to "any",
		"id" to "-1",
	)
	var isAlternateLayout: Boolean = false
	var isSortedByComicsQuantity: Boolean = false
}