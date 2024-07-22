package com.example.heroespractice.domain.model.comics

/**Created data class for comics, series, stories, and events APIs.*/
data class PlotDomain(
	val id: Int,
	val type: PlotType,
	val title: String,
	val thumbnail: String,
)

enum class PlotType(val type: String) {
	COMIC("comic"),
	EVENT("event"),
}