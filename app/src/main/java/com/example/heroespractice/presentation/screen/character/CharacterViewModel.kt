package com.example.heroespractice.presentation.screen.character

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heroespractice.R
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import com.example.heroespractice.presentation.utils.handleProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterViewModel(
	private val interactor: MarvelInteractor,
	private val contextResources: ContextResources,
) : ViewModel() {

	init {
		Log.e(TAG, "Initialized")
	}

	private val _isLoadingCharacter = MutableLiveData(false)
	val isLoadingCharacter: LiveData<Boolean> = _isLoadingCharacter

	private val _character = MutableLiveData<CharacterPresentation>()
	val character: LiveData<CharacterPresentation> = _character

	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	private val compositeDisposable = CompositeDisposable()

	fun getData(characterId: Long) {
		if (!character.isInitialized) {
			interactor.getCharacterByCharacterId(characterId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.handleProgress {
					_isLoadingCharacter.postValue(it)
				}
				.subscribe({ characterPresentation ->
					_character.postValue(characterPresentation)
					_screenDescription.postValue("")
				}, { throwable ->
					when (throwable) {
						is NoSuchElementException -> {
							_screenDescription.postValue(contextResources.getString(R.string.no_character))
						}

						else -> {
							_screenDescription.postValue(
								throwable.message
									?: contextResources.getString(R.string.failed_to_load_character)
							)
						}
					}
				}).apply {
					compositeDisposable.add(this)
				}
		}
	}

	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
		Log.e(TAG, "onCleared")
	}

	companion object {
		private const val TAG = "Lifecycle_ViewModel_CharacterViewModel"
	}
}