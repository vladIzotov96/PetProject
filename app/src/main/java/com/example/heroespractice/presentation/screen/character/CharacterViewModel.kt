package com.example.heroespractice.presentation.screen.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heroespractice.R
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.presentation.base.BaseViewModel
import com.example.heroespractice.presentation.model.character.CharacterPresentation

class CharacterViewModel(
	private val interactor: MarvelInteractor,
) : BaseViewModel(
	viewModelTag = TAG
) {

	/**LiveData fields*/
	private val _character = MutableLiveData<CharacterPresentation>()
	val character: LiveData<CharacterPresentation> = _character

	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	/**ViewModel fields*/
	var isFirstLoadOfScreen = true

	/**Get data from the server*/
	fun getCharacterByCharacterId(characterId: Long) {
		if (!character.isInitialized) {
			handleSingle(
				single = interactor.getCharacterByCharacterId(characterId),
				onSuccess = { characterPresentation ->
					_character.postValue(characterPresentation)
					_screenDescription.postValue("")
				},
				onError = { throwable ->
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
				}
			)
		}
	}

	fun createMessageAboutCharacterForContact(character: CharacterPresentation): String =
		contextResources.getString(R.string.want_to_introduce_character) +
				" " +
				character.name +
				"!\n\n" +
				if (character.isDescription) {
					character.description +
							"\n\n"
				} else {
					""
				} +
				contextResources.getString(R.string.learn_more_about_character) +
				" " +
				character.name +
				" " +
				contextResources.getString(R.string.via_link) +
				" " +
				character.plotsUrl

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_ViewModel_CharacterViewModel"
	}
}