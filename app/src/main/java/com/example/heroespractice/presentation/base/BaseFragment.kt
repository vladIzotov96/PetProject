package com.example.heroespractice.presentation.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.heroespractice.domain.base.ContextResources
import org.koin.android.ext.android.inject

abstract class BaseFragment<VB : ViewBinding>(
	private val fragmentTag: String,
) : Fragment() {

	protected val contextResources: ContextResources by inject()

	protected lateinit var binding: VB

	abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		Log.e(fragmentTag, "onCreateView()")
		binding = inflateBinding(inflater, container)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.e(fragmentTag, "onViewCreated()")
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		Log.e(fragmentTag, "onAttach()")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.e(fragmentTag, "onCreate()")
	}

	override fun onStart() {
		super.onStart()
		Log.e(fragmentTag, "onStart()")
	}

	override fun onResume() {
		super.onResume()
		Log.e(fragmentTag, "onResume()")
	}

	override fun onPause() {
		super.onPause()
		Log.e(fragmentTag, "onPause()")
	}

	override fun onStop() {
		super.onStop()
		Log.e(fragmentTag, "onStop()")
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		Log.e(fragmentTag, "onSaveInstanceState()")
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.e(fragmentTag, "onDestroyView()")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.e(fragmentTag, "onDestroy()")
	}

	override fun onDetach() {
		super.onDetach()
		Log.e(fragmentTag, "onDetach()")
	}
}