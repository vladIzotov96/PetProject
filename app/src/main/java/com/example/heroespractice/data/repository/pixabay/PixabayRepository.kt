package com.example.heroespractice.data.repository.pixabay

import io.reactivex.Completable
import java.io.File

interface PixabayRepository {

	fun downloadFile(url: String, file: File): Completable

}