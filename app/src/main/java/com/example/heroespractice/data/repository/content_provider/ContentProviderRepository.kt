package com.example.heroespractice.data.repository.content_provider

import com.example.heroespractice.domain.model.content_provider.ContactDomain
import io.reactivex.Single

interface ContentProviderRepository {

	fun getContacts(): Single<List<ContactDomain>>

}