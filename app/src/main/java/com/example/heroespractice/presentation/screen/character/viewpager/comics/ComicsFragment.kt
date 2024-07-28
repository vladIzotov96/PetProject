package com.example.heroespractice.presentation.screen.character.viewpager.comics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.FragmentTabPlotsBinding
import com.example.heroespractice.presentation.base.BaseFragment
import com.example.heroespractice.presentation.screen.character.CharacterFragmentDirections
import com.example.heroespractice.presentation.screen.character.viewpager.LeftOffsetDecoration
import com.example.heroespractice.presentation.screen.character.viewpager.PlotAdapter
import com.example.heroespractice.presentation.utils.MAX_NUMBER_OF_PLOTS_TO_DISPLAY
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

class ComicsFragment : BaseFragment<FragmentTabPlotsBinding>(
	fragmentTag = TAG
) {

	/**Injection*/
	private val viewModel: ComicsViewModel by viewModel {
		parametersOf(arguments?.getBoolean(IS_NEED_REQUEST))
	}
	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	/**Fragment fields*/
	private lateinit var adapter: PlotAdapter

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentTabPlotsBinding {
		return FragmentTabPlotsBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		adapter = PlotAdapter(
			plots = emptyList(),
			context = binding.root.context,
			onClick = {
				findNavController().navigate(
					CharacterFragmentDirections.actionCharacterToPlot()
				)
			},
			onShowAllClick = {
				findNavController().navigate(
					CharacterFragmentDirections.actionCharacterToPlots()
				)
			}
		)
		binding.plotsRecyclerView.adapter = adapter

		viewModel.getComicsByCharacterId(heroesService.clickedCharacter)
		viewModelObserveIsLoading()
		viewModelObserveComicsDescription()
		viewModelObserveComics()

		with(binding.plotsRecyclerView) {
			val leftOffsetDecoration =
				LeftOffsetDecoration(resources.getDimensionPixelSize(R.dimen.sixteen_dp))
			addItemDecoration(leftOffsetDecoration)

			addOnScrollListener(object :
				RecyclerView.OnScrollListener() {
				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)
					if (recyclerView.canScrollHorizontally(-1)) {
						if (recyclerView.itemDecorationCount > 0) {
							recyclerView.removeItemDecoration(leftOffsetDecoration)
						}
					} else {
						if (recyclerView.itemDecorationCount == 0) {
							recyclerView.addItemDecoration(leftOffsetDecoration)
						}
					}
				}
			})
		}
	}

	/**
	 * ViewModel observe functions
	 * */

	private fun viewModelObserveIsLoading() {
		viewModel.isLoading.observe(viewLifecycleOwner) { isLoadingComics ->
			binding.shimmerViewPlotsContainer.apply {
				visibility = if (isLoadingComics) {
					View.VISIBLE
				} else {
					stopShimmer()
					View.GONE
				}
			}
		}
	}

	private fun viewModelObserveComicsDescription() {
		viewModel.comicsDescription.observe(viewLifecycleOwner) { comicsDescription ->
			binding.textOnPlots.text = comicsDescription

			val isVisibleComicsDescription = !comicsDescription.isNullOrEmpty()
			binding.textOnPlots.isVisible = isVisibleComicsDescription
			binding.notFoundImage.isVisible = isVisibleComicsDescription
		}
	}

	private fun viewModelObserveComics() {
		viewModel.comics.observe(viewLifecycleOwner) { comics ->
			adapter.updatePlots(
				plots = comics.take(MAX_NUMBER_OF_PLOTS_TO_DISPLAY),
				isNeedAllItemType = (comics.size > MAX_NUMBER_OF_PLOTS_TO_DISPLAY)
			)
		}
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_ComicsFragment"
		private const val IS_NEED_REQUEST = "isNeedRequest"
		fun newInstance(isNeedRequest: Boolean): ComicsFragment {
			val fragment = ComicsFragment()
			val args = Bundle()
			args.putBoolean(IS_NEED_REQUEST, isNeedRequest)
			fragment.arguments = args
			return fragment
		}
	}
}