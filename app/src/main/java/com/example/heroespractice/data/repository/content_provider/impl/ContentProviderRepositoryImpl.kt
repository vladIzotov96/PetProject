package com.example.heroespractice.data.repository.content_provider.impl

import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.heroespractice.data.repository.content_provider.ContentProviderRepository
import com.example.heroespractice.domain.model.content_provider.ContactDomain
import io.reactivex.Single

class ContentProviderRepositoryImpl(
	private val contentResolver: ContentResolver
) : ContentProviderRepository {

	override fun getContacts(): Single<List<ContactDomain>> {
		return Single.create { emitter ->
			val contacts = mutableListOf<ContactDomain>()

			contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				arrayOf(
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER
				),
				null,
				null,
				"${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC",
			).apply {
				/**Automatic close() after execution*/
				this?.use { notNullCursor ->
					val indexDisplayName =
						notNullCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
					val indexNumber =
						notNullCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

					if (indexDisplayName != -1 && indexNumber != -1) {
						while (notNullCursor.moveToNext()) {
							contacts.add(
								ContactDomain(
									notNullCursor.getString(indexDisplayName),
									notNullCursor.getString(indexNumber),
								)
							)
						}
					}
				}
			}

			if (contacts.isNotEmpty()) {
				emitter.onSuccess(contacts)
			} else {
				emitter.onError((NoSuchElementException()))
			}
		}
	}
}