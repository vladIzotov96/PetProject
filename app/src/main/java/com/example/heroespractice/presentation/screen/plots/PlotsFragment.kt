package com.example.heroespractice.presentation.screen.plots

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.heroespractice.databinding.FragmentPlotsBinding
import com.example.heroespractice.presentation.base.BaseFragment

class PlotsFragment : BaseFragment<FragmentPlotsBinding>(
	fragmentTag = TAG
) {

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentPlotsBinding {
		return FragmentPlotsBinding.inflate(inflater, container, false)
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_PlotsFragment"
	}
}