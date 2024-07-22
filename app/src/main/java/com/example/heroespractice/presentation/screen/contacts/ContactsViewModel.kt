package com.example.heroespractice.presentation.screen.contacts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heroespractice.R
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.domain.interactor.content_provider.impl.ContentProviderInteractorImpl
import com.example.heroespractice.presentation.model.content_provider.ContactPresentation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContactsViewModel(
	private val interactor: ContentProviderInteractorImpl,
	private val contextResources: ContextResources,
) : ViewModel() {

	init {
		Log.e(TAG, "Initialized")
	}

	private val _contacts = MutableLiveData<List<ContactPresentation>>()
	val contacts: LiveData<List<ContactPresentation>> = _contacts

	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	private val compositeDisposable = CompositeDisposable()

	fun getContacts() {
		interactor.getContacts()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ characterPresentation ->
				_contacts.postValue(characterPresentation)
			}, { throwable ->
				when (throwable) {
					is NoSuchElementException -> {
						_screenDescription.postValue(contextResources.getString(R.string.no_contacts))
					}

					else -> {
						_screenDescription.postValue(
							throwable.message
								?: contextResources.getString(R.string.failed_to_load_contacts)
						)
					}
				}
			}).apply {
				compositeDisposable.add(this)
			}
	}

	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
		Log.e(TAG, "onCleared")
	}

	companion object {
		private const val TAG = "Lifecycle_ViewModel_ContactsViewModel"
	}
}