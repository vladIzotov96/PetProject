package com.example.heroespractice.presentation.screen.character.viewpager.comics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heroespractice.R
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.model.comics.PlotDomain
import com.example.heroespractice.presentation.base.BaseViewModel

class ComicsViewModel(
	private val interactor: MarvelInteractor,
	private val isNeedRequest: Boolean?,
) : BaseViewModel(
	viewModelTag = TAG
) {

	/**LiveData fields*/
	private val _comics = MutableLiveData<List<PlotDomain>>()
	val comics: LiveData<List<PlotDomain>> = _comics

	private val _comicsDescription = MutableLiveData<String>()
	val comicsDescription: LiveData<String> = _comicsDescription

	/**ViewModel fields*/
	private var isRequestInProgress = false

	/**Get data from the server*/
	fun getComicsByCharacterId(characterId: Long) {
		if (!comics.isInitialized && isNeedRequest == true && !isRequestInProgress) {
			isRequestInProgress = true
			_comicsDescription.postValue("")

			handleSingle(
				single = interactor.getComicsByCharacterId(characterId),
				onSuccess = { comicsList ->
					_comics.postValue(comicsList)
					isRequestInProgress = false
				},
				onError = { throwable ->
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
				}
			)
		} else if (!comics.isInitialized && !isRequestInProgress) {
			_comicsDescription.postValue(contextResources.getString(R.string.no_comics))
		}
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_ViewModel_ComicsViewModel"
	}
}