package com.example.heroespractice

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.heroespractice.data.worker.CharactersUpdateWorker
import com.example.heroespractice.data.worker.CharactersWorkerFactory
import com.example.heroespractice.di.appModules
import com.example.heroespractice.utils.DATA_SOURCE_NETWORK_AND_DATABASE
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class HeroesApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidContext(this@HeroesApplication)
			modules(appModules)
		}

		if (DATA_SOURCE_NETWORK_AND_DATABASE) {
			run {
				val workManagerConfiguration = Configuration.Builder()
					.setWorkerFactory(CharactersWorkerFactory(get(), get()))
					.build()
				WorkManager.initialize(this, workManagerConfiguration)

				val manager = WorkManager.getInstance(this)
				val constraints = Constraints.Builder()
					.setRequiredNetworkType(NetworkType.CONNECTED)
					.build()
				val periodicWorker = PeriodicWorkRequestBuilder<CharactersUpdateWorker>(
					30, TimeUnit.MINUTES
				)
					.setConstraints(constraints)
					.build()

				manager.enqueueUniquePeriodicWork(
					"CharactersUpdateWorker",
					ExistingPeriodicWorkPolicy.KEEP,
					periodicWorker
				)
			}
		}
	}
}