package com.example.heroespractice.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.data.database.AppDatabase
import com.example.heroespractice.data.database.AppDatabase.Companion.MIGRATION_6_7
import com.example.heroespractice.data.network.marvel.MarvelInterface
import com.example.heroespractice.data.network.pixabay.PixabayInterface
import com.example.heroespractice.data.repository.content_provider.impl.ContentProviderRepositoryImpl
import com.example.heroespractice.data.repository.marvel.MarvelRepository
import com.example.heroespractice.data.repository.marvel.impl.DatabaseRepository
import com.example.heroespractice.data.repository.marvel.impl.GeneralRepository
import com.example.heroespractice.data.repository.marvel.impl.NetworkRepository
import com.example.heroespractice.data.repository.pixabay.impl.PixabayRepositoryImpl
import com.example.heroespractice.data.worker.CharactersWorkerFactory
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.domain.base.ExternalFilesDir
import com.example.heroespractice.domain.interactor.content_provider.impl.ContentProviderInteractorImpl
import com.example.heroespractice.domain.interactor.download.impl.DownloadInteractorImpl
import com.example.heroespractice.domain.interactor.marvel.MarvelInteractor
import com.example.heroespractice.domain.interactor.marvel.impl.InteractorImpl
import com.example.heroespractice.domain.interactor.marvel.impl.InteractorMock
import com.example.heroespractice.presentation.screen.character.CharacterViewModel
import com.example.heroespractice.presentation.screen.character.viewpager.comics.ComicsViewModel
import com.example.heroespractice.presentation.screen.character.viewpager.events.EventsViewModel
import com.example.heroespractice.presentation.screen.characters.CharactersViewModel
import com.example.heroespractice.presentation.screen.contacts.ContactsViewModel
import com.example.heroespractice.utils.BASE_MARVEL_URL
import com.example.heroespractice.utils.BASE_PIXABAY_URL
import com.example.heroespractice.utils.DATA_SOURCE_NETWORK
import com.example.heroespractice.utils.DATA_SOURCE_NETWORK_AND_DATABASE
import com.example.heroespractice.utils.MARVEL_API
import com.example.heroespractice.utils.PIXABAY_API
import com.example.heroespractice.utils.PRIVATE_KEY_MARVEL_API
import com.example.heroespractice.utils.PUBLIC_KEY_MARVEL_API
import com.example.heroespractice.utils.USE_MOCK_DATA
import com.example.heroespractice.utils.md5
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val networkModule = module {

	/**General*/
	factory {
		HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
	}

	/**Marvel*/
	single {
		OkHttpClient.Builder()
	}

	single(named(MARVEL_API)) {
		Retrofit.Builder()
			.baseUrl(BASE_MARVEL_URL)
			.client(get<OkHttpClient.Builder>()
				.connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.addInterceptor(get<HttpLoggingInterceptor>())
				.addInterceptor { chain -> createParametersDefault(chain) }
				.build())
			.addConverterFactory(GsonConverterFactory.create(Gson()))
			.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
			.build()
	}

	single {
		get<Retrofit>(named(MARVEL_API))
			.create(MarvelInterface::class.java)
	}

	/**Pixabay*/
	single<OkHttpClient> {
		OkHttpClient.Builder()
			.addInterceptor(get<HttpLoggingInterceptor>())
			.build()
	}

	single(named(PIXABAY_API)) {
		Retrofit.Builder()
			.baseUrl(BASE_PIXABAY_URL)
			.client(get())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	single {
		get<Retrofit>(named(PIXABAY_API))
			.create(PixabayInterface::class.java)
	}

}

private val databaseModule = module {

	single {
		Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
			.addMigrations(MIGRATION_6_7)
			.build()
	}

	single {
		get<AppDatabase>().characterDao()
	}

}

private val dataModule = module {

	if (DATA_SOURCE_NETWORK_AND_DATABASE) {
		single {
			DatabaseRepository(get())
		}
		single {
			NetworkRepository(get())
		}
		single<MarvelRepository> {
			GeneralRepository(get(), get())
		}
	} else {
		if (DATA_SOURCE_NETWORK) {
			single<MarvelRepository> {
				NetworkRepository(get())
			}
		} else {
			single {
				DatabaseRepository(get())
			}
		}
	}

	single {
		PixabayRepositoryImpl(get())
	}

	single {
		ContentProviderRepositoryImpl(get())
	}

}

private val serviceModule = module {

	single {
		HeroesService()
	}

	single {
		ContextResources()
	}

	single {
		ExternalFilesDir()
	}

	single {
		provideContentResolver(androidContext())
	}

}

private val interactorModule = module {

	factory<MarvelInteractor> {
		if (USE_MOCK_DATA) {
			InteractorMock()
		} else {
			InteractorImpl(get())
		}
	}

	single {
		DownloadInteractorImpl(get(), get())
	}


	single {
		ContentProviderInteractorImpl(get())
	}
}

private val viewModelModule = module {

	viewModel {
		CharactersViewModel(get())
	}

	viewModel {
		CharacterViewModel(get())
	}

	viewModel { (isNeedRequest: Boolean?) ->
		ComicsViewModel(get(), isNeedRequest)
	}

	viewModel { (isNeedRequest: Boolean?) ->
		EventsViewModel(get(), isNeedRequest)
	}

	viewModel {
		ContactsViewModel(get())
	}

}

private val workerModule = module {

	factory {
		CharactersWorkerFactory(get(), get())
	}

}

val appModules = listOf(
	networkModule,
	databaseModule,
	serviceModule,
	dataModule,
	interactorModule,
	viewModelModule,
	workerModule,
)

private fun createParametersDefault(chain: Interceptor.Chain): Response {
	val timeStamp = System.currentTimeMillis()
	var request = chain.request()
	val builder = request.url.newBuilder()

	builder
		//.addQueryParameter("name", "Batman") /**For testing scenarios with empty results.*/
		.addQueryParameter("limit", "100")
		.addQueryParameter("apikey", PUBLIC_KEY_MARVEL_API)
		.addQueryParameter(
			"hash",
			"$timeStamp$PRIVATE_KEY_MARVEL_API$PUBLIC_KEY_MARVEL_API".md5()
		)
		.addQueryParameter("ts", timeStamp.toString())

	request = request.newBuilder().url(builder.build()).build()
	return chain.proceed(request)
}

private fun provideContentResolver(context: Context): ContentResolver {
	return context.contentResolver
}