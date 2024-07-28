package com.example.heroespractice.presentation.model.content_provider.mapper

import com.example.heroespractice.R
import com.example.heroespractice.domain.model.content_provider.ContactDomain
import com.example.heroespractice.presentation.model.content_provider.ContactPresentation
import com.example.heroespractice.presentation.model.content_provider.IconPresentation
import kotlin.random.Random

fun ContactDomain.domainToPresentation(): ContactPresentation =
	this.let { contactDomain ->
		ContactPresentation(
			name = contactDomain.name,
			number = additionNumber(contactDomain.number),
			icon = IconPresentation(
				color = getRandomSystemColor(),
				name = createNameIcon(contactDomain.name),
			)
		)
	}

private fun additionNumber(number: String): String {

	fun addSpaces(input: String, positions: List<Int>): String {
		val result = StringBuilder()
		for (i in input.indices) {
			result.append(input[i])
			if (i in positions) {
				result.append(" ")
			}
		}
		return result.toString()
	}

	val clearNumber = number.replace(" ", "")
	return when {
		clearNumber.first() == '0' && clearNumber.length == 10 -> "+38" + addSpaces(
			clearNumber,
			listOf(0, 2, 5, 7)
		)

		clearNumber.first() == '3' && clearNumber.length == 12 -> "+" + addSpaces(
			clearNumber,
			listOf(2, 4, 7, 9)
		)

		clearNumber.startsWith("+380") && clearNumber.length == 13 -> addSpaces(
			clearNumber,
			listOf(3, 5, 8, 10)
		)

		else -> number
	}
}

private fun getRandomSystemColor(): Int {
	val colorArray = arrayOf(
		R.color.other_colors_christalle,
		R.color.other_colors_violet_red,
		R.color.other_colors_light_sea_green,
		R.color.other_colors_denim,
		R.color.other_colors_lime_green,
		R.color.other_colors_card_dark,
		R.color.other_colors_card_light,
		R.color.other_colors_summer_sky,
		R.color.other_colors_emerald,
		R.color.other_colors_yellow_road,
		R.color.other_colors_coral,
		R.color.other_colors_purple,
		R.color.other_colors_dodger_blue,
		R.color.other_colors_dodger_turquoise,
	)
	val randomIndex = Random.nextInt(colorArray.size)
	return colorArray[randomIndex]
}

private fun createNameIcon(number: String): String {
	val words = number.split("\\s+".toRegex())
	return if (words.isEmpty()) {
		""
	} else if (words.size == 1) {
		words[0].first().toString()
	} else {
		"${words[0].first()}${words[1].first()}"
	}
}