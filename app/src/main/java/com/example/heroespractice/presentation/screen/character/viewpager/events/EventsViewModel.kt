package com.example.heroespractice.presentation.screen.character.viewpager.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heroespractice.R
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.presentation.base.BaseViewModel

class EventsViewModel(
	private val interactor: MarvelInteractor,
	private val isNeedRequest: Boolean?,
) : BaseViewModel(
	viewModelTag = TAG
) {

	/**LiveData fields*/
	private val _events = MutableLiveData<List<PlotDomain>>()
	val events: LiveData<List<PlotDomain>> = _events

	private val _eventsDescription = MutableLiveData<String>()
	val eventsDescription: LiveData<String> = _eventsDescription

	/**ViewModel fields*/
	private var isRequestInProgress = false

	/**Get data from the server*/
	fun getEventsByCharacterId(characterId: Long) {
		if (!events.isInitialized && isNeedRequest == true && !isRequestInProgress) {
			isRequestInProgress = true
			_eventsDescription.postValue("")

			handleSingle(
				interactor.getEventsByCharacterId(characterId),
				onSuccess = { comicsList ->
					_events.postValue(comicsList)
					isRequestInProgress = false
				},
				onError = { throwable ->
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
				}
			)
		} else if (!events.isInitialized && !isRequestInProgress) {
			_eventsDescription.postValue(contextResources.getString(R.string.no_events))
		}
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_ViewModel_EventsViewModel"
	}
}