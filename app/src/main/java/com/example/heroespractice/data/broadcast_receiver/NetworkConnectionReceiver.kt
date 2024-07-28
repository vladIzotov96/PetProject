package com.example.heroespractice.data.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import com.example.heroespractice.presentation.utils.BannerWithTwoButtonUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NetworkConnectionReceiver(
	private val rootView: View,
	private val compositeDisposable: CompositeDisposable,
) : BroadcastReceiver() {

	companion object {
		const val DELAY_TIME = 5L
	}

	override fun onReceive(context: Context?, intent: Intent?) {

		if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
			val connectivityManager =
				context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

			fun isNetworkConnected(): Boolean {
				val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
				return activeNetwork?.isConnectedOrConnecting == true
			}

			if (!isNetworkConnected()) {
				compositeDisposable.clear()
				Observable.timer(DELAY_TIME, TimeUnit.SECONDS)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe {
						if (!isNetworkConnected()) {
							BannerWithTwoButtonUtil.showBanner(
								context,
								rootView
							)
						}
					}.apply {
						compositeDisposable.add(this)
					}
			}
		}
	}
}