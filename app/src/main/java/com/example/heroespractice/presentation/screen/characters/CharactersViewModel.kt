package com.example.heroespractice.presentation.screen.characters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import com.example.heroespractice.presentation.utils.handleProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.java.KoinJavaComponent

class CharactersViewModel(
	private val interactor: MarvelInteractor,
	private val contextResources: ContextResources,
) : ViewModel() {

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	init {
		Log.e(TAG, "Initialized")
	}

	private val _isLoadingCharacters = MutableLiveData(false)
	val isLoadingCharacters: LiveData<Boolean> = _isLoadingCharacters

	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	private val _characters = MutableLiveData<List<CharacterPresentation>>()
	val characters: LiveData<List<CharacterPresentation>> =
		_characters.map { listCharacterPresentation ->
			if (heroesService.isSortedByComicsQuantity) {
				listCharacterPresentation.sortedByDescending { characterPresentation ->
					characterPresentation.comicsQuantity
				}
			} else listCharacterPresentation
		}

	private val _isSearchNoResult = MutableLiveData(false)
	val isSearchNoResult: LiveData<Boolean> = _isSearchNoResult

	private val _isClearIconVisible = MutableLiveData(false)
	val isClearIconVisible: LiveData<Boolean> = _isClearIconVisible

	private val _isAlternateLayout = MutableLiveData(heroesService.isAlternateLayout)
	val isAlternateLayout: LiveData<Boolean> = _isAlternateLayout

	private val _isSortedByComicsQuantity = MutableLiveData(heroesService.isSortedByComicsQuantity)
	val isSortedByComicsQuantity: LiveData<Boolean> = _isSortedByComicsQuantity

	private val compositeDisposable = CompositeDisposable()

	private var charactersListForSearching = listOf<CharacterPresentation>()

	fun searchCharacters(searchText: String) {
		val searchedList = charactersListForSearching.let { charactersList ->
			charactersList.filter { it.name.lowercase().contains(searchText) }
		}
		_characters.value = searchedList
		_isClearIconVisible.value = searchText.isNotEmpty()
		_isSearchNoResult.value = searchedList.isEmpty() && searchText.isNotEmpty()
	}

	fun setLayout() {
		_isAlternateLayout.value = heroesService.isAlternateLayout
	}

	fun setSorting() {
		_isSortedByComicsQuantity.value = heroesService.isSortedByComicsQuantity
	}

	fun getData() {
		if (!characters.isInitialized) {
			interactor.getCharacters()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.handleProgress {
					_isLoadingCharacters.postValue(it)
				}
				.subscribe({ listCharacterPresentation ->
					charactersListForSearching = listCharacterPresentation
					_characters.postValue(listCharacterPresentation)
					_screenDescription.postValue("")
				}, { throwable ->
					when (throwable) {
						is NoSuchElementException -> {
							_screenDescription.postValue(contextResources.getString(R.string.no_characters))
						}

						else -> {
							_screenDescription.postValue(
								throwable.message
									?: contextResources.getString(R.string.something_went_wrong)
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
		private const val TAG = "Lifecycle_ViewModel_CharactersViewModel"
	}
}