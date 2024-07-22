package com.example.heroespractice.domain.interactor.content_provider

import com.example.heroespractice.presentation.model.content_provider.ContactPresentation
import io.reactivex.Single

interface ContentProviderInteractor {

	fun getContacts(): Single<List<ContactPresentation>>

}