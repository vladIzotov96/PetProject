package com.example.heroespractice.presentation.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.presentation.utils.handleProgress
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

open class BaseViewModel(
	private val viewModelTag: String,
) : ViewModel(), KoinComponent {

	/**Injection*/
	protected val contextResources: ContextResources by inject()

	/**Log initializing ViewModel*/
	init {
		Log.e(viewModelTag, "Initialized")
	}

	/**Monitoring the data loading status*/
	private val _isLoading = MutableLiveData(false)
	val isLoading: LiveData<Boolean> = _isLoading

	/**CompositeDisposable*/
	private val compositeDisposable = CompositeDisposable()

	/**Observable type Single*/
	protected fun <T> handleSingle(
		single: Single<T>,
		isNeedHandleProgress: Boolean = true,
		onSuccess: (T) -> Unit,
		onError: (Throwable) -> Unit
	) {
		single
			.delay(5, TimeUnit.SECONDS)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.handleProgress {
				if (isNeedHandleProgress) {
					_isLoading.postValue(it)
				}
			}
			.subscribe({ result ->
				onSuccess(result)
			}, { throwable ->
				onError(throwable)
			}).apply {
				Log.e(viewModelTag, "Before add(), size ${compositeDisposable.size()}")
				compositeDisposable.add(this)
				Log.e(viewModelTag, "After add(), size ${compositeDisposable.size()}")
			}
	}

	/**Clear CompositeDisposable and log clearing ViewModel*/
	override fun onCleared() {
		Log.e(viewModelTag, "Before clear(), size ${compositeDisposable.size()}")
		compositeDisposable.clear()
		Log.e(viewModelTag, "After clear, size ${compositeDisposable.size()}")
		super.onCleared()
		Log.e(viewModelTag, "onCleared()")
	}
}