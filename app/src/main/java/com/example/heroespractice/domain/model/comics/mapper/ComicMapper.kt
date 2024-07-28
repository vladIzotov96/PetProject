package com.example.heroespractice.domain.model.comics.mapper

import com.example.heroespractice.data.network.marvel.dto.PlotDto
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.domain.model.comics.PlotType
import com.example.heroespractice.domain.utils.isContainHttps
import com.example.heroespractice.utils.PLACEHOLDER_VALUE_FOR_NUMBERS

fun PlotDto.networkToDomain(type: PlotType): PlotDomain = this.let { plotDto ->
	PlotDomain(
		id = plotDto.id ?: PLACEHOLDER_VALUE_FOR_NUMBERS,
		type = type,
		title = plotDto.title.orEmpty(),
		thumbnail = "${
			isContainHttps(
				url = plotDto.thumbnail?.path.orEmpty()
			)
		}.${plotDto.thumbnail?.extension.orEmpty()}",
	)
}