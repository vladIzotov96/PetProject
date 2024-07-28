package com.example.heroespractice.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.heroespractice.data.repository.marvel.impl.DatabaseRepository
import com.example.heroespractice.data.repository.marvel.impl.NetworkRepository
import org.koin.core.Koin

class CharactersWorkerFactory(
	private val databaseRepository: DatabaseRepository,
	private val networkRepository: NetworkRepository,
) : WorkerFactory() {
	override fun createWorker(
		appContext: Context,
		workerClassName: String,
		workerParameters: WorkerParameters
	): ListenableWorker? {
		return when (workerClassName) {
			CharactersUpdateWorker::class.java.name -> CharactersUpdateWorker(
				appContext,
				workerParameters,
				databaseRepository,
				networkRepository,
			)

			else -> null
		}
	}
}