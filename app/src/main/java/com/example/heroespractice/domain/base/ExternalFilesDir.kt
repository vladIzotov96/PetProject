package com.example.heroespractice.domain.base

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class ExternalFilesDir : KoinComponent {

	private val context: Context by inject()

	fun getExternalFilesDir(type: String?): File? {
		return context.getExternalFilesDir(type)
	}
}