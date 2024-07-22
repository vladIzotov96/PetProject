package com.example.heroespractice.domain.interactor.content_provider.impl

import com.example.heroespractice.data.repository.content_provider.impl.ContentProviderRepositoryImpl
import com.example.heroespractice.domain.interactor.content_provider.ContentProviderInteractor
import com.example.heroespractice.domain.utils.filteredListByLengthParameter
import com.example.heroespractice.presentation.model.content_provider.ContactPresentation
import com.example.heroespractice.presentation.model.content_provider.mapper.domainToPresentation
import io.reactivex.Single

class ContentProviderInteractorImpl(
	private val repository: ContentProviderRepositoryImpl,
) : ContentProviderInteractor {

	private companion object {
		const val LENGTH_NAME = 0
	}

	override fun getContacts(): Single<List<ContactPresentation>> =
		repository.getContacts()
			.map { listContactDomain ->
				filteredListByLengthParameter(
					listContactDomain,
					LENGTH_NAME
				) { contactDomain -> contactDomain.name.length }.map { contactDomain ->
					contactDomain.domainToPresentation()
				}
			}
}