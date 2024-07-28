package com.example.heroespractice.presentation.model.character

data class CharacterAdditionalOptionPresentation(
	val icon: Int,
	val title: String,
	val description: String,
	val clickAction: () -> Unit,
)