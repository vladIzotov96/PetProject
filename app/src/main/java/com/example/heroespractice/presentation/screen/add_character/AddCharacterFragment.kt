package com.example.heroespractice.presentation.screen.add_character

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.heroespractice.R
import com.example.heroespractice.databinding.FragmentAddCharacterBinding
import com.example.heroespractice.presentation.base.BaseFragment
import com.example.heroespractice.presentation.utils.SnackbarUtil

class AddCharacterFragment : BaseFragment<FragmentAddCharacterBinding>(
	fragmentTag = TAG
) {

	/**Fragment fields*/
	private var pendingAction: (() -> Unit)? = null

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentAddCharacterBinding {
		return FragmentAddCharacterBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		with(binding) {
			pickCharacterImageButton.setOnClickListener {
				checkPermissionAndPerformAction(
					Manifest.permission.READ_EXTERNAL_STORAGE
				) {
					openGallery()
				}
			}

			downloadVideoButton.setOnClickListener {
				checkPermissionAndPerformAction(
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					::initService
				)
			}
		}

	}

	/**
	 * Auxiliaries functions
	 * */

	private val requestPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { isGranted ->
		if (isGranted) {
			pendingAction?.invoke()
		} else {
			SnackbarUtil.showSnackbar(
				binding.root,
				message = contextResources.getString(R.string.no_permission),
				actionText = contextResources.getString(R.string.go_to_permissions),
			) {
				val uri = Uri.fromParts("package", context?.packageName, null)
				val settingsIntent = Intent().apply {
					action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
					data = uri
				}
				context?.startActivity(settingsIntent)
			}
		}
	}

	private fun checkPermissionAndPerformAction(permission: String, action: () -> Unit) {
		when (ContextCompat.checkSelfPermission(requireContext(), permission)) {
			PackageManager.PERMISSION_GRANTED -> {
				action()
			}

			else -> {
				pendingAction = action
				requestPermissionLauncher.launch(permission)
			}
		}
	}

	private fun initService() {
		val urls = listOf(
			"video/2019/04/06/22634-328940142_tiny.mp4",
			"video/2024/03/29/206131_large.mp4",
			"video/2024/01/28/198358-907598215_large.mp4",
		)
		context?.let {
			VideoDownloadService.startService(it, urls[0])
			Log.e("VideoDownloadService", "Go to VideoDownloadService")
		}
	}

	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK).apply {
			type = "image/*"
		}
		pickImageLauncher.launch(intent)
	}

	private val pickImageLauncher = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == Activity.RESULT_OK) {
			result.data?.data?.apply {
				binding.characterImage.setImageURI(this)
			}
		}
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_AddCharacterFragment"
	}
}