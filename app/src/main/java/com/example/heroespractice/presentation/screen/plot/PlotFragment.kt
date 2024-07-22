package com.example.heroespractice.presentation.screen.plot

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.FragmentPlotBinding
import org.koin.java.KoinJavaComponent

class PlotFragment : Fragment() {

	private lateinit var binding: FragmentPlotBinding

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		Log.e(TAG, "onCreateView")
		binding = FragmentPlotBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.e(TAG, "onViewCreated")

		binding.textOnPlot.text = heroesService.clickedPlot.toString()
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		Log.e(TAG, "onAttach")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.e(TAG, "onCreate")
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

	override fun onStop() {
		super.onStop()
		Log.e(TAG, "onStop")
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.e(TAG, "onDestroyView")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.e(TAG, "onDestroy")
	}

	override fun onDetach() {
		super.onDetach()
		Log.e(TAG, "onDetach")
	}

	companion object {
		private const val TAG = "Lifecycle_Fragment_PlotFragment"
	}
}