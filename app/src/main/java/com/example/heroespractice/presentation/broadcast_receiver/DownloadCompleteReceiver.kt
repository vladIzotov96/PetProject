package com.example.heroespractice.presentation.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.heroespractice.presentation.utils.INTENT_FILTER_DOWNLOAD_COMPLETE_RECEIVER
import com.example.heroespractice.presentation.utils.MESSAGE_DOWNLOAD_COMPLETE_RECEIVER
import com.example.heroespractice.presentation.utils.SnackbarUtil

class DownloadCompleteReceiver(
	private val rootView: View,
	private val context: Context,
) {

	private val broadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			val message = intent?.getStringExtra(MESSAGE_DOWNLOAD_COMPLETE_RECEIVER)
			message?.let {
				showSnackbar(it)
			}
		}
	}

	fun register() {
		LocalBroadcastManager.getInstance(context)
			.registerReceiver(
				broadcastReceiver,
				IntentFilter(INTENT_FILTER_DOWNLOAD_COMPLETE_RECEIVER)
			)
	}

	fun unregister() {
		LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
	}

	private fun showSnackbar(message: String) {
		SnackbarUtil.showSnackbar(
			rootView,
			message
		)
	}
}