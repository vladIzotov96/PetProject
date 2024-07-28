package com.example.heroespractice.presentation.screen.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.heroespractice.R
import com.example.heroespractice.domain.interactor.content_provider.impl.ContentProviderInteractorImpl
import com.example.heroespractice.presentation.base.BaseViewModel
import com.example.heroespractice.presentation.model.content_provider.ContactPresentation

class ContactsViewModel(
	private val interactor: ContentProviderInteractorImpl,
) : BaseViewModel(
	viewModelTag = TAG
) {

	/**LiveData fields*/
	private val _contacts = MutableLiveData<List<ContactPresentation>>()
	val contacts: LiveData<List<ContactPresentation>> = _contacts

	private val _screenDescription = MutableLiveData<String>()
	val screenDescription: LiveData<String> = _screenDescription

	/**Get data from the server*/
	fun getContacts() {
		handleSingle(
			single = interactor.getContacts(),
			onSuccess = { characterPresentation ->
				_contacts.postValue(characterPresentation)
			},
			onError = { throwable ->
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
			}
		)
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_ViewModel_ContactsViewModel"
	}
}