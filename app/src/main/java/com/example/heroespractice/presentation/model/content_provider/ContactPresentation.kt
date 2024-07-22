package com.example.heroespractice.presentation.model.content_provider

data class ContactPresentation(

	val name: String,

	val number: String,

	val icon: IconPresentation,
)

data class IconPresentation(

	val color: Int,

	val name: String,
)