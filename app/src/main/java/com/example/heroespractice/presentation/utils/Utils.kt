package com.example.heroespractice.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.heroespractice.R
import com.example.heroespractice.domain.base.ContextResources
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Single

private val contextResources: ContextResources = ContextResources()

fun <T> Single<T>.handleProgress(isProgress: (Boolean) -> Unit): Single<T> {

	return this.doOnSubscribe { isProgress(true) }
		.doOnSuccess { isProgress(false) }
		.doOnError { isProgress(false) }
		.doOnDispose { isProgress(false) }
}

object SnackbarUtil {
	fun showSnackbar(
		view: View,
		message: String,
		duration: Int = Snackbar.LENGTH_LONG,
		actionText: String = contextResources.getString(R.string.ok),
		action: (View) -> Unit = {}
	) {
		Snackbar
			.make(view, message, duration)
			.setAction(actionText.uppercase(), action)
			.setTextMaxLines(2)
			.show()
	}
}

object BannerWithOneButtonUtil {
	fun showBanner(
		context: Context,
		view: View,
		message: String = "You have lost connection to the internet. This app is offline.",
		duration: Int = Snackbar.LENGTH_LONG,
		actionText: String = "OK",
		action: (View) -> Unit = {},

		) {
		val snackbar = Snackbar
			.make(view, message, duration)
			.setAction(actionText.uppercase(), action)
			.setTextMaxLines(2)
			.setMaxInlineActionWidth(1)

		val icon = ContextCompat.getDrawable(context, R.drawable.icon_internet_issue)
		val textView: TextView =
			snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)

		val padding = context.resources.getDimensionPixelSize(R.dimen.sixteen_dp)
		icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
		textView.setCompoundDrawables(icon, null, null, null)
		textView.compoundDrawablePadding = padding

		snackbar.show()
	}
}

object BannerWithTwoButtonUtil {
	fun showBanner(
		context: Context,
		view: View,
		message: String = contextResources.getString(R.string.lost_connection_to_the_internet),
		duration: Int = Snackbar.LENGTH_INDEFINITE,
		actionTextOne: String = contextResources.getString(R.string.turn_on_wifi),
		actionOne: (View) -> Unit = {
			val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
			context.startActivity(intent)
		},
		actionTextTwo: String = contextResources.getString(R.string.close),
		actionTwo: (View) -> Unit = {},
	) {
		val inflater = LayoutInflater.from(context)
		val customView = inflater.inflate(R.layout.banner_two_button, null)

		val snackbar = Snackbar
			.make(view, "", duration)

		val snackbarLayout = snackbar.view as ViewGroup
		with(snackbarLayout) {
			this.removeAllViews()
			this.addView(customView)
		}

		val params = snackbarLayout.layoutParams as (FrameLayout.LayoutParams)
		params.gravity = Gravity.TOP

		with(customView) {
			findViewById<TextView>(R.id.snackbar_text).text = message
			findViewById<Button>(R.id.snackbar_action_one).apply {
				text = actionTextOne.uppercase()
				setOnClickListener {
					actionOne(it)
					snackbar.dismiss()
				}
			}
			findViewById<Button>(R.id.snackbar_action_two).apply {
				text = actionTextTwo.uppercase()
				setOnClickListener {
					actionTwo(it)
					snackbar.dismiss()
				}
			}
		}

		snackbar.show()
	}
}

fun dpToPx(resources: Resources, value: Float): Int {
	return TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		value,
		resources.displayMetrics
	).toInt()
}

fun setListViewHeightBasedOnItems(listView: ListView) {
	val listAdapter = listView.adapter ?: return

	val widthMeasureSpec =
		View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.UNSPECIFIED)
	val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

	var totalHeight = 0

	for (i in 0 until listAdapter.count) {
		val listItem = listAdapter.getView(i, null, listView)
		listItem.measure(widthMeasureSpec, heightMeasureSpec)
		totalHeight += listItem.measuredHeight
	}
	listView.layoutParams.apply {
		height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
		listView.layoutParams = this
	}
}

const val NUMBER_OF_PLOTS_TO_REQUEST = 0
const val MAX_NUMBER_OF_PLOTS_TO_DISPLAY = 5
const val SINGULAR_NUMBER = 1
const val IS_USE_WEB_VIEW = false
const val INTENT_FILTER_DOWNLOAD_COMPLETE_RECEIVER = "com.example.DOWNLOAD_COMPLETE"
const val MESSAGE_DOWNLOAD_COMPLETE_RECEIVER = "message"