package com.example.heroespractice.presentation.screen.character.viewpager.comics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heroespractice.R
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.presentation.utils.handleProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ComicsViewModel(
	private val interactor: MarvelInteractor,
	private val contextResources: ContextResources,
	private val isNeedRequest: Boolean?,
) : ViewModel() {

	init {
		Log.e(TAG, "Initialized")
	}

	private val _isLoadingComics = MutableLiveData(false)
	val isLoadingComics: LiveData<Boolean> = _isLoadingComics

	private val _comics = MutableLiveData<List<PlotDomain>>()
	val comics: LiveData<List<PlotDomain>> = _comics

	private val _comicsDescription = MutableLiveData<String>()
	val comicsDescription: LiveData<String> = _comicsDescription

	private var isRequestInProgress = false
	private val compositeDisposable = CompositeDisposable()

	fun getData(characterId: Long) {
		if (!comics.isInitialized && isNeedRequest == true && !isRequestInProgress) {
			isRequestInProgress = true
			_comicsDescription.postValue("")

			interactor.getComicsByCharacterId(characterId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.handleProgress {
					_isLoadingComics.postValue(it)
				}
				.subscribe({ comicsList ->
					_comics.postValue(comicsList)
					isRequestInProgress = false
				}, { throwable ->
					when (throwable) {
						is NoSuchElementException -> {
							_comicsDescription.postValue(contextResources.getString(R.string.no_comics))
						}

						else -> {
							_comicsDescription.postValue(
								throwable.message
									?: contextResources.getString(R.string.failed_to_load_comics)
							)
						}
					}
					isRequestInProgress = false
				}).apply {
					compositeDisposable.add(this)
				}
		} else if (!comics.isInitialized && !isRequestInProgress) {
			_comicsDescription.postValue(contextResources.getString(R.string.no_comics))
		}
	}

	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
		Log.e(TAG, "onCleared")
	}

	companion object {
		private const val TAG = "Lifecycle_ViewModel_ComicsViewModel"
	}
}