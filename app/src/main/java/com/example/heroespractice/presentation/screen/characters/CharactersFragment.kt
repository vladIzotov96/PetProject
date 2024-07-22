package com.example.heroespractice.presentation.screen.characters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.heroespractice.R
import com.example.heroespractice.databinding.FragmentCharactersBinding
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.presentation.utils.SnackbarUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {

	private val viewModel: CharactersViewModel by viewModel()
	private val contextResources: ContextResources by inject()

	private lateinit var binding: FragmentCharactersBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		Log.e(TAG, "onCreateView")
		binding = FragmentCharactersBinding.inflate(inflater, container, false)
		return binding.root
	}

	@SuppressLint("ClickableViewAccessibility")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.e(TAG, "onViewCreated")

		with(viewModel) {
			getData()

			isLoadingCharacters.observe(viewLifecycleOwner) { isLoadingHeroes ->
				with(binding.shimmerViewContainer) {
					visibility = if (isLoadingHeroes) {
						startShimmer()
						View.VISIBLE

					} else {
						stopShimmer()
						View.GONE
					}
				}
			}

			screenDescription.observe(viewLifecycleOwner) { screenDescription ->
				if (!screenDescription.isNullOrEmpty()) {
					SnackbarUtil.showSnackbar(
						view,
						screenDescription,
						actionText = contextResources.getString(R.string.retry)
					) {
						getData()
						with(binding) {
							textOnHeroes.text = ""
							editText.isInvisible = false
						}
					}
					with(binding) {
						textOnHeroes.text = screenDescription
						editText.isInvisible = true
					}
				}
			}

			characters.observe(viewLifecycleOwner) { listCharacterPresentation ->
				val adapter = CharactersAdapter(
					characters = listCharacterPresentation,
					context = binding.root.context,
				) {
					findNavController().navigate(
						CharactersFragmentDirections.actionCharactersToCharacter()
					)
				}
				binding.comicsRecyclerView.adapter = adapter

				with(binding.toggleButton) {
					isVisible = true
					setOnClickListener {
						adapter.toggleLayout()
						setLayout()
					}
				}
				with(binding.sortButton) {
					isVisible = true
					setOnClickListener {
						adapter.toggleSorting()
						setSorting()
					}
				}

				with(binding) {
					editText.isEnabled = true
					addButton.isInvisible = false
				}

				/**Removal marginEnd after displaying toggleButton*/
				/*run {
					val searchField = binding.editText
					val layoutParams = searchField.layoutParams as ViewGroup.MarginLayoutParams
					val marginEndInPx = dpToPx(resources, 0f)
					layoutParams.marginEnd = marginEndInPx
					searchField.layoutParams = layoutParams
				}*/
			}

			isSearchNoResult.observe(viewLifecycleOwner) { isSearchNoResult ->
				binding.searchTextOnHeroes.isInvisible = !isSearchNoResult
			}

			isAlternateLayout.observe(viewLifecycleOwner) { isAlternateLayout ->
				with(binding) {
					toggleButton.setImageResource(
						if (isAlternateLayout) R.drawable.icon_rectangle_display else R.drawable.icon_square_display
					)
					squareSkeleton.isVisible = isAlternateLayout
					rectangleSkeleton.isVisible = !isAlternateLayout
				}
			}

			isClearIconVisible.observe(viewLifecycleOwner) { isClearIconVisible ->
				setClearIconVisibility(isClearIconVisible)
			}

			isSortedByComicsQuantity.observe(viewLifecycleOwner) { isSortedByComicsQuantity ->
				binding.imageViewBadge.isInvisible = !isSortedByComicsQuantity
			}
		}

		with(binding.editText) {
			setOnTouchListener { _, event ->
				if (compoundDrawablesRelative[2] != null) {
					if (event.action == android.view.MotionEvent.ACTION_UP) {
						if (event.rawX >= (right - compoundDrawablesRelative[2].bounds.width())) {
							text?.clear()
							return@setOnTouchListener true
						}
					}
				}
				return@setOnTouchListener false
			}

			addTextChangedListener {
				viewModel.searchCharacters(it.toString().lowercase())
			}
		}

		binding.addButton.setOnClickListener {
			findNavController().navigate(
				CharactersFragmentDirections.actionCharactersToAddCharacter()
			)
		}
	}

	private fun setClearIconVisibility(visibility: Boolean) {
		with(binding.editText) {
			val searchIcon =
				compoundDrawablesRelative[0] ?: return
			val clearIcon =
				compoundDrawablesRelative[2] ?: return

			if (visibility) {
				clearIcon.alpha = 255
			} else {
				clearIcon.alpha = 0
			}

			setCompoundDrawablesRelativeWithIntrinsicBounds(
				searchIcon,
				null,
				clearIcon,
				null
			)
		}
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
		private const val TAG = "Lifecycle_Fragment_CharactersFragment"
	}
}