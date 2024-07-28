package com.example.heroespractice.presentation.screen.plot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.FragmentPlotBinding
import com.example.heroespractice.presentation.base.BaseFragment
import org.koin.java.KoinJavaComponent

class PlotFragment : BaseFragment<FragmentPlotBinding>(
	fragmentTag = TAG
) {

	/**Injection*/
	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentPlotBinding {
		return FragmentPlotBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.textOnPlot.text = heroesService.clickedPlot.toString()
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_PlotFragment"
	}
}