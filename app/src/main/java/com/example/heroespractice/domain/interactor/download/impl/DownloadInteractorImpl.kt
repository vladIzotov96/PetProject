package com.example.heroespractice.domain.interactor.download.impl

import android.os.Environment
import com.example.heroespractice.data.repository.pixabay.impl.PixabayRepositoryImpl
import com.example.heroespractice.domain.base.ExternalFilesDir
import com.example.heroespractice.domain.interactor.download.DownloadInteractor
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.IOException

class DownloadInteractorImpl(
	private val repository: PixabayRepositoryImpl,
	private val externalFilesDir: ExternalFilesDir,
) : DownloadInteractor {

	override fun downloadFile(url: String): Single<Pair<String, Completable>> {
		return Single.defer {
			val file = createFile() ?: throw IOException("Failed to create file")
			Single.just(Pair(file.name, repository.downloadFile(url, file)))
		}
	}

	private fun createFile(): File? {
		val storageDir = externalFilesDir.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
		return storageDir?.let {
			val uniqueId = System.currentTimeMillis()
			val fileName = "video_$uniqueId.mp4"
			File(it, fileName)
		}
	}
}