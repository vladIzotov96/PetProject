package com.example.heroespractice.data.repository.pixabay.impl

import com.example.heroespractice.data.network.pixabay.PixabayInterface
import com.example.heroespractice.data.repository.pixabay.PixabayRepository
import io.reactivex.Completable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PixabayRepositoryImpl(
	private val downloadInterface: PixabayInterface
) : PixabayRepository {

	override fun downloadFile(url: String, file: File): Completable {
		return downloadInterface.downloadFile(url)
			.flatMapCompletable { response ->
				if (response.isSuccessful) {
					response.body()?.let { body ->
						Completable.fromAction {
							writeToFile(body.byteStream(), file)
						}
					} ?: Completable.error(IOException("Response body is null"))
				} else {
					Completable.error(IOException("Request failed: ${response.message()}"))
				}
			}
	}

	private fun writeToFile(inputStream: InputStream, file: File) {
		var outputStream: FileOutputStream? = null
		try {
			outputStream = FileOutputStream(file)
			val buffer = ByteArray(1024)
			var length: Int

			while (inputStream.read(buffer).also { length = it } > 0) {
				outputStream.write(buffer, 0, length)
			}
		} finally {
			inputStream.close()
			outputStream?.close()
		}
	}

}