package com.example.heroespractice.presentation.screen.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.presentation.base.BaseViewModel
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import org.koin.java.KoinJavaComponent

class CharactersViewModel(
	private val interactor: MarvelInteractor,
) : BaseViewModel(
	viewModelTag = TAG
) {

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	/**LiveData fields*/
	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	private val _characters = MutableLiveData<List<CharacterPresentation>>()
	val characters: LiveData<List<CharacterPresentation>> = insertCharacters(_characters)

	private val _isSearchNoResult = MutableLiveData(false)
	val isSearchNoResult: LiveData<Boolean> = _isSearchNoResult

	private val _isClearIconVisible = MutableLiveData(false)
	val isClearIconVisible: LiveData<Boolean> = _isClearIconVisible

	private val _isAlternateLayout = MutableLiveData(heroesService.isAlternateLayout)
	val isAlternateLayout: LiveData<Boolean> = _isAlternateLayout

	private val _isSortedByComicsQuantity = MutableLiveData(heroesService.isSortedByComicsQuantity)
	val isSortedByComicsQuantity: LiveData<Boolean> = _isSortedByComicsQuantity

	private val _isShowSnackbarAfterRefresh =
		MutableLiveData(Pair(false, ""))
	val isShowSnackbarAfterRefresh: LiveData<Pair<Boolean, String>> = _isShowSnackbarAfterRefresh

	/**Static characters list from the server*/
	private var charactersList = listOf<CharacterPresentation>()
	var searchText = ""

	/**Get data from the server*/
	fun getCharacters(isSwipe: Boolean = false) {
		if (!characters.isInitialized || isSwipe) {
			handleSingle(
				single = interactor.getCharacters(),
				isNeedHandleProgress = !isSwipe,
				onSuccess = { listCharacterPresentation ->
					charactersList = listCharacterPresentation
					_characters.postValue(listCharacterPresentation)
					_screenDescription.postValue("")
				},
				onError = { throwable ->
					handleError(isSwipe, throwable)
				}
			)
		}
	}

	private fun handleError(isSwipe: Boolean, throwable: Throwable) {
		when (throwable) {
			is NoSuchElementException -> {
				if (!isSwipe) _screenDescription.postValue(contextResources.getString(R.string.no_characters))
				else _isShowSnackbarAfterRefresh.postValue(
					Pair(
						true,
						contextResources.getString(R.string.no_characters)
					)
				)
			}

			else -> {
				_screenDescription.postValue(
					throwable.message
						?: contextResources.getString(R.string.something_went_wrong)
				)
			}
		}
	}

	/**Some logic that should always correspond to the list of characters.*/
	private fun insertCharacters(characters: MutableLiveData<List<CharacterPresentation>>): LiveData<List<CharacterPresentation>> {
		return characters.map { listCharacterPresentation ->
			val filteredList = listCharacterPresentation.filter {
				it.name.lowercase().contains(searchText)
			}

			_isSearchNoResult.value = filteredList.isEmpty() && searchText.isNotEmpty()
			_isClearIconVisible.value = searchText.isNotEmpty()

			if (heroesService.isSortedByComicsQuantity) {
				filteredList.sortedByDescending { characterPresentation ->
					characterPresentation.comicsQuantity
				}
			} else filteredList
		}
	}

	/**Search characters by name*/
	fun searchCharacters() {
		_characters.value = charactersList
	}

	/**Set characters layout*/
	fun setLayout() {
		_isAlternateLayout.value = heroesService.isAlternateLayout
	}

	/**Set characters sorting*/
	fun setSorting() {
		_isSortedByComicsQuantity.value = heroesService.isSortedByComicsQuantity
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_ViewModel_CharactersViewModel"
	}
}