package com.example.heroespractice.presentation.screen.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.heroespractice.R
import com.example.heroespractice.databinding.FragmentCharactersBinding
import com.example.heroespractice.presentation.base.BaseFragment
import com.example.heroespractice.presentation.utils.SnackbarUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharactersFragment : BaseFragment<FragmentCharactersBinding>(
	fragmentTag = TAG,
) {

	/**Injection*/
	private val viewModel: CharactersViewModel by viewModel()

	/**Fragment fields*/
	private lateinit var adapter: CharactersAdapter

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentCharactersBinding {
		return FragmentCharactersBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		adapter = CharactersAdapter(
			characters = emptyList(),
			context = binding.root.context,
		) {
			findNavController().navigate(
				CharactersFragmentDirections.actionCharactersToCharacter()
			)
		}
		binding.comicsRecyclerView.adapter = adapter

		viewModel.getCharacters()
		viewModelObserveIsLoading()
		viewModelObserveScreenDescription()
		viewModelObserveCharacters()
		viewModelObserveIsSearchNoResult()
		viewModelObserveIsAlternateLayout()
		viewModelObserveIsClearIconVisible()
		viewModelObserveIsShowSnackbarAfterRefresh()

		with(binding.editText) {
			setOnTouchListener(fun(_: View, event: MotionEvent): Boolean {
				if (compoundDrawablesRelative[2] != null) {
					if (event.action == MotionEvent.ACTION_UP) {
						if (event.rawX >= (right - compoundDrawablesRelative[2].bounds.width())) {
							text?.clear()
							return true
						}
					}
				}
				return false
			})

			addTextChangedListener {
				with(viewModel) {
					searchText = it.toString().lowercase()
					searchCharacters()
				}

			}
		}

		with(binding.addButton) {
			setOnClickListener {
				findNavController().navigate(
					CharactersFragmentDirections.actionCharactersToAddCharacter()
				)
			}
		}

		binding.swipeRefreshLayout.setOnRefreshListener {
			viewModel.getCharacters(true)
		}
	}

	/**
	 * ViewModel observe functions
	 * */

	private fun viewModelObserveIsLoading() {
		viewModel.isLoading.observe(viewLifecycleOwner) { isLoadingHeroes ->
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
	}

	private fun viewModelObserveScreenDescription() {
		with(viewModel) {
			screenDescription.observe(viewLifecycleOwner) { screenDescription ->
				if (!screenDescription.isNullOrEmpty()) {
					view?.let {
						SnackbarUtil.showSnackbar(
							it,
							screenDescription,
							actionText = contextResources.getString(R.string.retry)
						) {
							getCharacters()
							with(binding) {
								textOnHeroes.text = ""
								editText.isInvisible = false
							}
						}
					}
					with(binding) {
						textOnHeroes.text = screenDescription
						editText.isInvisible = true
					}
				}
			}
		}
	}

	private fun viewModelObserveCharacters() {
		with(viewModel) {
			characters.observe(viewLifecycleOwner) { characters ->
				adapter.updateCharacters(characters)

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
				with(binding.swipeRefreshLayout) {
					isRefreshing = false
					isEnabled = true
				}
				with(binding) {
					editText.isEnabled = true
					addButton.isInvisible = false
				}

				viewModelObserveIsSortedByComicsQuantity()

				/**Removal marginEnd after displaying toggleButton*/
				/*run {
					val searchField = binding.editText
					val layoutParams = searchField.layoutParams as ViewGroup.MarginLayoutParams
					val marginEndInPx = dpToPx(resources, 0f)
					layoutParams.marginEnd = marginEndInPx
					searchField.layoutParams = layoutParams
				}*/
			}
		}
	}

	private fun viewModelObserveIsSearchNoResult() {
		viewModel.isSearchNoResult.observe(viewLifecycleOwner) { isSearchNoResult ->
			binding.searchTextOnHeroes.isInvisible = !isSearchNoResult
		}
	}

	private fun viewModelObserveIsAlternateLayout() {
		viewModel.isAlternateLayout.observe(viewLifecycleOwner) { isAlternateLayout ->
			with(binding) {
				toggleButton.setImageResource(
					if (isAlternateLayout) R.drawable.icon_rectangle_display else R.drawable.icon_square_display
				)
				squareSkeleton.isVisible = isAlternateLayout
				rectangleSkeleton.isVisible = !isAlternateLayout
			}
		}
	}

	private fun viewModelObserveIsClearIconVisible() {
		viewModel.isClearIconVisible.observe(viewLifecycleOwner) { isClearIconVisible ->
			setClearIconVisibility(isClearIconVisible)
		}
	}

	private fun viewModelObserveIsSortedByComicsQuantity() {
		viewModel.isSortedByComicsQuantity.observe(viewLifecycleOwner) { isSortedByComicsQuantity ->
			binding.imageViewBadge.isVisible = isSortedByComicsQuantity
		}
	}

	private fun viewModelObserveIsShowSnackbarAfterRefresh() {
		viewModel.isShowSnackbarAfterRefresh.observe(viewLifecycleOwner) { pair ->
			if (pair.first) {
				SnackbarUtil.showSnackbar(
					binding.root,
					pair.second
				)
			}
		}
	}

	/**
	 * Auxiliaries functions
	 * */

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

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_CharactersFragment"
	}
}