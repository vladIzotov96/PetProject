package com.example.heroespractice.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.heroespractice.data.repository.marvel.impl.DatabaseRepository
import com.example.heroespractice.data.repository.marvel.impl.NetworkRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharactersUpdateWorker(
	context: Context,
	workerParams: WorkerParameters,
	private val databaseRepository: DatabaseRepository,
	private val networkRepository: NetworkRepository,
) :
	Worker(context, workerParams) {

	private val compositeDisposable = CompositeDisposable()

	override fun doWork(): Result {
		Log.e(TAG, "networkRepository.getCharacters() starting")
		networkRepository.getCharacters()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.flatMapCompletable { characters ->
				Log.e(TAG, "networkRepository.getCharacters() completed successful: $characters")
				Log.e(TAG, "databaseRepository.insertCharacter(characters) starting")
				databaseRepository.insertCharacter(characters)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
			}
			.subscribe({
				Log.e(TAG, "databaseRepository.insertCharacter(characters) completed successful")
				Result.success()
			}, { error ->
				Log.e(TAG, "Result.failure(): ${error.message}")
				Result.failure()
			}).also { disposable ->
				compositeDisposable.add(disposable)
			}

		return Result.success()
	}

	companion object {
		private const val TAG = "CharactersUpdateWorker"
	}
}