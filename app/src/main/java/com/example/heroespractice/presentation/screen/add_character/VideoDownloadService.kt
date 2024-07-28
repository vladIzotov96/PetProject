package com.example.heroespractice.presentation.screen.add_character

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.heroespractice.R
import com.example.heroespractice.domain.interactor.download.impl.DownloadInteractorImpl
import com.example.heroespractice.presentation.screen.MainActivity
import com.example.heroespractice.presentation.utils.INTENT_FILTER_DOWNLOAD_COMPLETE_RECEIVER
import com.example.heroespractice.presentation.utils.MESSAGE_DOWNLOAD_COMPLETE_RECEIVER
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class VideoDownloadService : Service() {

	/**Injection*/
	private val interactor: DownloadInteractorImpl by inject()

	/**Service fields*/
	private var notificationManager: NotificationManager? = null
	private var compositeDisposable = CompositeDisposable()

	override fun onCreate() {
		super.onCreate()
		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		val url = intent?.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY

		createNotificationChannel()
		val notification = startNotification(startId)
		startForeground(startId, notification)

		interactor.downloadFile(url)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ pair ->
				val fileName = pair.first
				val completable = pair.second

				completable
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe({
						sendBroadcastMessage(fileName)
						Log.d(TAG, "File $fileName downloaded successful")
						finalNotification(
							startId,
							"Download complete",
							"File $fileName downloaded successful"
						)
						stopSelf(startId)
					}, { error ->
						Log.e(TAG, "Download error: ${error.message}")
						finalNotification(startId, "Download error", "Error: ${error.message}")
						stopSelf(startId)
					}).apply {
						compositeDisposable.add(this)
					}
			}, { error ->
				Log.e(TAG, "Download error: ${error.message}")
				finalNotification(startId, "Download error", "Error: ${error.message}")
				stopSelf(startId)
			}).apply {
				compositeDisposable.add(this)
			}

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}

	override fun onDestroy() {
		compositeDisposable.clear()
		super.onDestroy()
	}

	/**
	 * Auxiliaries functions
	 * */

	private fun startNotification(startId: Int): Notification {
		/**Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified when creating a PendingIntent.
		Strongly consider using FLAG_IMMUTABLE, only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.*/
		val pendingIntent: PendingIntent = PendingIntent.getActivity(
			this,
			startId,
			Intent(this, MainActivity::class.java),
			PendingIntent.FLAG_IMMUTABLE
		)

		return NotificationCompat.Builder(this, CHANNEL_ID)
			.setContentTitle("Downloading video")
			.setTicker("Downloading video")
			.setSmallIcon(R.drawable.icon_logo_avengers_grey)
			.setProgress(1, 0, true)
			.setContentIntent(pendingIntent)
			.build()
	}

	private fun finalNotification(startId: Int, title: String, message: String) {
		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setContentTitle(title)
			.setContentText(message)
			.setSmallIcon(R.drawable.icon_logo_avengers_grey)
			.build()
		notificationManager?.notify(
			startId + START_AND_FINAL_NOTIFICATIONS_WITH_DIFFERENT_IDS,
			notification
		)
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			CHANNEL_ID,
			CHANNEL_NAME,
			NotificationManager.IMPORTANCE_DEFAULT
		)
		notificationManager?.createNotificationChannel(channel)
	}

	private fun sendBroadcastMessage(fileName: String) {
		val intent = Intent(INTENT_FILTER_DOWNLOAD_COMPLETE_RECEIVER)
		intent.putExtra(MESSAGE_DOWNLOAD_COMPLETE_RECEIVER, "File $fileName downloaded")
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
	}

	companion object {
		/**Logs*/
		private const val TAG = "VideoDownloadService"

		/**Intent*/
		private const val EXTRA_URL = "url"

		/**Notifications*/
		private const val CHANNEL_ID = "download_channel"
		private const val CHANNEL_NAME = "Download Service Channel"
		private const val START_AND_FINAL_NOTIFICATIONS_WITH_DIFFERENT_IDS = 100

		fun startService(context: Context, url: String) {
			val intent = Intent(context, VideoDownloadService::class.java).apply {
				putExtra(EXTRA_URL, url)
			}
			context.startForegroundService(intent)
		}
	}
}