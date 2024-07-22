package com.example.heroespractice.presentation.screen.character.viewpager.events

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.FragmentTabPlotsBinding
import com.example.heroespractice.presentation.screen.character.CharacterFragmentDirections
import com.example.heroespractice.presentation.screen.character.viewpager.LeftOffsetDecoration
import com.example.heroespractice.presentation.screen.character.viewpager.PlotAdapter
import com.example.heroespractice.presentation.utils.MAX_NUMBER_OF_PLOTS_TO_DISPLAY
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent

class EventsFragment : Fragment() {

	private val viewModel: EventsViewModel by viewModel {
		parametersOf(arguments?.getBoolean(ARG_NEED_REQUEST))
	}
	private lateinit var binding: FragmentTabPlotsBinding

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

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

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		Log.e(TAG, "onCreateView")
		binding = FragmentTabPlotsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.e(TAG, "onViewCreated")

		with(viewModel) {
			getData(heroesService.clickedCharacter)

			events.observe(viewLifecycleOwner) { events ->
				binding.plotsRecyclerView.apply {
					adapter = PlotAdapter(
						plots = events.take(MAX_NUMBER_OF_PLOTS_TO_DISPLAY),
						context = binding.root.context,
						isNeedAllItemType = (events.size > MAX_NUMBER_OF_PLOTS_TO_DISPLAY),
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

			val shimmerFrameEventsLayout: ShimmerFrameLayout =
				binding.shimmerViewPlotsContainer
			isLoadingEvents.observe(viewLifecycleOwner) { isLoadingEvents ->
				shimmerFrameEventsLayout.apply {
					visibility = if (isLoadingEvents) {
						View.VISIBLE
					} else {
						stopShimmer()
						View.GONE
					}
				}
			}

			eventsDescription.observe(viewLifecycleOwner) { eventsDescription ->
				binding.textOnPlots.text = eventsDescription

				val isVisibleComicsDescription = !eventsDescription.isNullOrEmpty()
				binding.textOnPlots.isVisible = isVisibleComicsDescription
				binding.notFoundImage.isVisible = isVisibleComicsDescription
			}
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
}