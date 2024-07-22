package com.example.heroespractice.presentation.screen.character.viewpager.events

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

class EventsViewModel(
	private val interactor: MarvelInteractor,
	private val contextResources: ContextResources,
	private val isNeedRequest: Boolean?,
) : ViewModel() {

	init {
		Log.e(TAG, "Initialized")
	}

	private val _isLoadingEvents = MutableLiveData(false)
	val isLoadingEvents: LiveData<Boolean> = _isLoadingEvents

	private val _events = MutableLiveData<List<PlotDomain>>()
	val events: LiveData<List<PlotDomain>> = _events

	private val _eventsDescription = MutableLiveData<String>()
	val eventsDescription: LiveData<String> = _eventsDescription

	private var isRequestInProgress = false
	private val compositeDisposable = CompositeDisposable()

	fun getData(characterId: Long) {
		if (!events.isInitialized && isNeedRequest == true && !isRequestInProgress) {
			isRequestInProgress = true
			_eventsDescription.postValue("")

			interactor.getEventsByCharacterId(characterId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.handleProgress {
					_isLoadingEvents.postValue(it)
				}
				.subscribe({ comicsList ->
					_events.postValue(comicsList)
					isRequestInProgress = false
				}, { throwable ->
					when (throwable) {
						is NoSuchElementException -> {
							_eventsDescription.postValue(contextResources.getString(R.string.no_events))
						}

						else -> {
							_eventsDescription.postValue(
								throwable.message
									?: contextResources.getString(R.string.failed_to_load_events)
							)
						}
					}
					isRequestInProgress = false
				}).apply {
					compositeDisposable.add(this)
				}
		} else if (!events.isInitialized && !isRequestInProgress) {
			_eventsDescription.postValue(contextResources.getString(R.string.no_events))
		}
	}

	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
		Log.e(TAG, "onCleared")
	}

	companion object {
		private const val TAG = "Lifecycle_ViewModel_EventsViewModel"
	}
}