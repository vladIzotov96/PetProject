package com.example.heroespractice.presentation.screen.character.viewpager.events

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

class EventsFragment : BaseFragment<FragmentTabPlotsBinding>(
	fragmentTag = TAG
) {

	/**Injection*/
	private val viewModel: EventsViewModel by viewModel {
		parametersOf(arguments?.getBoolean(ARG_NEED_REQUEST))
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

		viewModel.getEventsByCharacterId(heroesService.clickedCharacter)
		viewModelObserveIsLoading()
		viewModelObserveEventsDescription()
		viewModelObserveEvents()

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
		viewModel.isLoading.observe(viewLifecycleOwner) { isLoadingEvents ->
			binding.shimmerViewPlotsContainer.apply {
				visibility = if (isLoadingEvents) {
					View.VISIBLE
				} else {
					stopShimmer()
					View.GONE
				}
			}
		}
	}

	private fun viewModelObserveEventsDescription() {
		viewModel.eventsDescription.observe(viewLifecycleOwner) { eventsDescription ->
			binding.textOnPlots.text = eventsDescription

			val isVisibleComicsDescription = !eventsDescription.isNullOrEmpty()
			binding.textOnPlots.isVisible = isVisibleComicsDescription
			binding.notFoundImage.isVisible = isVisibleComicsDescription
		}
	}

	private fun viewModelObserveEvents() {
		viewModel.events.observe(viewLifecycleOwner) { events ->
			adapter.updatePlots(
				plots = events.take(MAX_NUMBER_OF_PLOTS_TO_DISPLAY),
				isNeedAllItemType = (events.size > MAX_NUMBER_OF_PLOTS_TO_DISPLAY)
			)
		}
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_EventsFragment"
		private const val ARG_NEED_REQUEST = "isNeedRequest"
		fun newInstance(isNeedRequest: Boolean): EventsFragment {
			val fragment = EventsFragment()
			val args = Bundle()
			args.putBoolean("isNeedRequest", isNeedRequest)
			fragment.arguments = args
			return fragment
		}
	}
}