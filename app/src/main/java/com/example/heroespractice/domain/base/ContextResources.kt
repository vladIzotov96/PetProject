package com.example.heroespractice.domain.base

import android.content.Context
import androidx.annotation.StringRes
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ContextResources : KoinComponent {

	private val context: Context by inject()

	fun getString(@StringRes stringResId: Int): String {
		return context.getString(stringResId)
	}
}