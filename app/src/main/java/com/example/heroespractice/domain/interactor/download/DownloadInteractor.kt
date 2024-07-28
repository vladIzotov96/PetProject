package com.example.heroespractice.domain.interactor.download

import io.reactivex.Completable
import io.reactivex.Single

interface DownloadInteractor {

	fun downloadFile(url: String): Single<Pair<String, Completable>>

}