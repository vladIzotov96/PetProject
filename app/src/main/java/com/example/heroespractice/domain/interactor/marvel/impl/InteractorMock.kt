package com.example.heroespractice.domain.interactor.marvel.impl

import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.domain.model.comics.PlotType
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import io.reactivex.Single

class InteractorMock : MarvelInteractor {
	override fun getCharacters(): Single<List<CharacterPresentation>> {
		return Single.just(severalCharacter)
		//return Single.error(NoSuchElementException())
	}

	override fun getCharacterByCharacterId(characterId: Long): Single<CharacterPresentation> {
		return Single.just(oneCharacter)
	}

	override fun getComicsByCharacterId(characterId: Long): Single<List<PlotDomain>> {
		return Single.just(comic)
	}

	override fun getEventsByCharacterId(characterId: Long): Single<List<PlotDomain>> {
		return Single.just(event)
	}

	private val emptyList = emptyList<CharacterPresentation>()

	private val oneCharacter = CharacterPresentation(
		id = 1009149,
		name = "Abys",
		isDescription = false,
		description = "Character does not have a short description.",
		thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64.jpg",
		comicsQuantity = 24,
		comicsDescriptionQuantity = "24 comics",
		eventsQuantity = 3,
		isNeedComicsRequest = true,
		isNeedEventsRequest = true,
		plotsUrl = "https://www.marvel.com/comics/characters/1009148/absorbing_man?utm_campaign=apiRef&utm_source=fb24ab3a6ab52691753e62c57445116f",
	)

	private val severalCharacter = listOf(
		CharacterPresentation(
			id = 1009149,
			name = "Abys",
			isDescription = false,
			description = "Character does not have a short description.",
			thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64.jpg",
			comicsQuantity = 39,
			comicsDescriptionQuantity = "39 comics",
			eventsQuantity = 14,
			isNeedComicsRequest = true,
			isNeedEventsRequest = true,
			plotsUrl = "https://www.marvel.com/comics/characters/1009148/absorbing_man?utm_campaign=apiRef&utm_source=fb24ab3a6ab52691753e62c57445116f",
		),
		CharacterPresentation(
			id = 1016823,
			name = "Abomination (Ultimate)",
			isDescription = false,
			description = "Character does not have a short description.",
			thumbnail = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg",
			comicsQuantity = 12,
			comicsDescriptionQuantity = "12 comics",
			eventsQuantity = 1,
			isNeedComicsRequest = true,
			isNeedEventsRequest = true,
			plotsUrl = "https://www.marvel.com/comics/characters/1009148/absorbing_man?utm_campaign=apiRef&utm_source=fb24ab3a6ab52691753e62c57445116f",
		),
	)

	private val comic = listOf(
		PlotDomain(
			id = 103120,
			type = PlotType.COMIC,
			title = "Comic: #1",
			thumbnail = ""
		),
		PlotDomain(
			id = 103121,
			type = PlotType.COMIC,
			title = "Comic: #2",
			thumbnail = ""
		),
		PlotDomain(
			id = 103121,
			type = PlotType.COMIC,
			title = "Comic: #3",
			thumbnail = ""
		),
		PlotDomain(
			id = 103121,
			type = PlotType.COMIC,
			title = "Comic: #4",
			thumbnail = ""
		),
		PlotDomain(
			id = 103121,
			type = PlotType.COMIC,
			title = "Comic: #5",
			thumbnail = ""
		),
		PlotDomain(
			id = 103121,
			type = PlotType.COMIC,
			title = "Comic: #6",
			thumbnail = ""
		),
	)

	private val event = listOf(
		PlotDomain(
			id = 103122,
			type = PlotType.EVENT,
			title = "Event: #1",
			thumbnail = ""
		),
		PlotDomain(
			id = 103123,
			type = PlotType.EVENT,
			title = "Event: #2",
			thumbnail = ""
		),
		PlotDomain(
			id = 103120,
			type = PlotType.EVENT,
			title = "Event: #3",
			thumbnail = ""
		),
		PlotDomain(
			id = 103120,
			type = PlotType.EVENT,
			title = "Event: #4",
			thumbnail = ""
		),
		PlotDomain(
			id = 103120,
			type = PlotType.EVENT,
			title = "Event: #5",
			thumbnail = ""
		),
		PlotDomain(
			id = 103120,
			type = PlotType.EVENT,
			title = "Event: #6",
			thumbnail = ""
		),
	)
}