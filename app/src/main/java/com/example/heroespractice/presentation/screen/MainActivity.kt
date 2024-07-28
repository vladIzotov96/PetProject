package com.example.heroespractice.presentation.screen

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.heroespractice.R
import com.example.heroespractice.data.broadcast_receiver.NetworkConnectionReceiver
import com.example.heroespractice.presentation.broadcast_receiver.DownloadCompleteReceiver
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

	private lateinit var downloadReceiver: DownloadCompleteReceiver
	private lateinit var networkConnectionReceiver: NetworkConnectionReceiver

	private var compositeDisposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.e(TAG, "onCreate")
		setContentView(R.layout.activity_main)

		val rootView: View = findViewById(android.R.id.content)

		/**DownloadCBroadcastReceiver*/
		downloadReceiver = DownloadCompleteReceiver(rootView, this)
		downloadReceiver.register()

		/**NetworkConnectionBroadcastReceiver*/
		networkConnectionReceiver = NetworkConnectionReceiver(rootView, compositeDisposable)
		val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
		registerReceiver(networkConnectionReceiver, filter)
	}

	override fun onStart() {
		super.onStart()
		Log.e(TAG, "onStart")
	}

	override fun onResume() {
		super.onResume()
		Log.e(TAG, "onResume")
	}

	override fun onPause() {
		super.onPause()
		Log.e(TAG, "onPause")
	}

	override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
		super.onSaveInstanceState(outState, outPersistentState)
		Log.e(TAG, "onSaveInstanceState")
	}

	override fun onStop() {
		super.onStop()
		Log.e(TAG, "onStop")
	}

	override fun onDestroy() {
		downloadReceiver.unregister()
		unregisterReceiver(networkConnectionReceiver)
		super.onDestroy()
		Log.e(TAG, "onDestroy")
	}

	companion object {
		private const val TAG = "Lifecycle_Activity_MainActivity"
	}
}