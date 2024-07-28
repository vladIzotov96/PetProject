package com.example.heroespractice.presentation.screen.character

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.FragmentCharacterBinding
import com.example.heroespractice.databinding.SectionDescriptionBinding
import com.example.heroespractice.presentation.base.BaseFragment
import com.example.heroespractice.presentation.model.character.CharacterAdditionalOptionPresentation
import com.example.heroespractice.presentation.screen.character.viewpager.ViewPagerAdapter
import com.example.heroespractice.presentation.utils.IS_USE_WEB_VIEW
import com.example.heroespractice.presentation.utils.SnackbarUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent

class CharacterFragment : BaseFragment<FragmentCharacterBinding>(
	fragmentTag = TAG,
) {

	/**Injection*/
	private val viewModel: CharacterViewModel by viewModel()
	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	/**Fragment fields*/
	private lateinit var bindingSectionDescription: SectionDescriptionBinding
	private lateinit var scrollView: NestedScrollView
	private var scrollPositionX: Int = 0
	private var scrollPositionY: Int = 0
	private var messageAboutCharacterForContact = ""
	private var optionsList = mutableListOf(
		CharacterAdditionalOptionPresentation(
			R.drawable.icon_share,
			contextResources.getString(R.string.share_character_information),
			contextResources.getString(R.string.with_your_contacts),
		) {
			when (
				ContextCompat.checkSelfPermission(
					requireContext(),
					Manifest.permission.READ_CONTACTS
				)
			) {
				PackageManager.PERMISSION_GRANTED -> {
					navigateToContacts()
				}

				else -> {
					requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
				}
			}
		},
	)
	private val adapter = AdditionalCharacterOptionAdapter(optionsList)

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentCharacterBinding {
		return FragmentCharacterBinding.inflate(inflater, container, false)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		super.onCreateView(inflater, container, savedInstanceState)

		with(binding) {
			layoutViewModel = viewModel
			lifecycleOwner = viewLifecycleOwner
		}

		bindingSectionDescription = DataBindingUtil.bind(binding.sectionDescription.root)!!
		with(bindingSectionDescription) {
			layoutViewModel = viewModel
			lifecycleOwner = viewLifecycleOwner
		}

		scrollView = binding.scrollView
		if (savedInstanceState != null) {
			scrollPositionX = savedInstanceState.getInt(SCROLL_POSITION_X)
			scrollPositionY = savedInstanceState.getInt(SCROLL_POSITION_Y)
		}

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.optionsRecyclerView.adapter = adapter

		viewModel.getCharacterByCharacterId(heroesService.clickedCharacter)
		viewModelObserveIsLoading()
		viewModelObserveScreenDescription()
		viewModelObserveCharacter()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		outState.putInt(SCROLL_POSITION_X, scrollView.scrollX)
		outState.putInt(SCROLL_POSITION_Y, scrollView.scrollY)
	}

	/**
	 * ViewModel observe functions
	 * */

	private fun viewModelObserveIsLoading() {
		viewModel.isLoading.observe(viewLifecycleOwner) { isLoadingHeroes ->
			bindingSectionDescription.shimmerViewContainer.apply {
				visibility = if (isLoadingHeroes) {
					View.VISIBLE
				} else {
					stopShimmer()
					View.GONE
				}
			}
		}
	}

	private fun viewModelObserveScreenDescription() {
		viewModel.screenDescription.observe(viewLifecycleOwner) { screenDescription ->
			if (!screenDescription.isNullOrEmpty()) {
				SnackbarUtil.showSnackbar(
					binding.root,
					message = screenDescription,
					actionText = contextResources.getString(R.string.retry),
				) {
					viewModel.getCharacterByCharacterId(heroesService.clickedCharacter)
					binding.textOnCharacter.text = ""
				}
			}
		}
	}

	private fun viewModelObserveCharacter() {
		viewModel.character.observe(viewLifecycleOwner) { character ->
			bindingSectionDescription.apply {
				if (character.thumbnail.isNotEmpty()) {
					val circularProgressDrawable =
						CircularProgressDrawable(requireContext()).apply {
							strokeWidth = 10f
							centerRadius = 40f
							start()
						}
					Glide.with(imageCharacter.context)
						.load(character.thumbnail)
						.placeholder(circularProgressDrawable)
						.error(R.drawable.icon_logo_avengers_grey)
						.into(imageCharacter)
				} else {
					imageCharacter.setImageResource(R.drawable.icon_logo_avengers_grey)
				}
			}

			binding.optionsRecyclerView.isVisible = true

			val viewPagerAdapter = ViewPagerAdapter(
				childFragmentManager,
				lifecycle,
				character.isNeedComicsRequest,
				character.isNeedEventsRequest
			)
			binding.sectionTabs.viewPager.adapter = viewPagerAdapter

			TabLayoutMediator(
				binding.sectionTabs.tabLayout,
				binding.sectionTabs.viewPager
			) { tab, position ->
				tab.text = when (position) {
					0 -> contextResources.getString(R.string.comics)
					1 -> contextResources.getString(R.string.events)
					else -> null
				}
			}.attach()

			messageAboutCharacterForContact =
				viewModel.createMessageAboutCharacterForContact(character)

			if (viewModel.isFirstLoadOfScreen && character.plotsUrl.isNotEmpty()) {
				addOptionToOptionList(character.plotsUrl)
				with(adapter) {
					notifyItemInserted(optionsList.size - 1)
					notifyItemChanged(optionsList.size - 2)
				}
				viewModel.isFirstLoadOfScreen = false
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
			navigateToContacts()
		} else {
			Snackbar.make(
				binding.root,
				contextResources.getString(R.string.no_permission),
				Snackbar.LENGTH_LONG
			).show()
		}
	}

	private fun navigateToContacts() {
		val action = CharacterFragmentDirections.actionCharacterToContacts(
			messageAboutCharacterForContact
		)
		findNavController().navigate(action)
	}

	private fun openInExternalBrowser(url: String) {
		val intent = Intent(
			Intent.ACTION_VIEW,
			Uri.parse(url)
		)
		startActivity(intent)
	}

	private fun addOptionToOptionList(plotsUrlCharacter: String) {
		optionsList.add(
			CharacterAdditionalOptionPresentation(
				R.drawable.icon_browser,
				contextResources.getString(R.string.view_plots),
				contextResources.getString(R.string.in_browser),
			) {
				if (IS_USE_WEB_VIEW) {
					with(binding.webView) {
						isVisible = IS_USE_WEB_VIEW
						webViewClient = WebViewClient()
						loadUrl(plotsUrlCharacter)
					}
				} else {
					openInExternalBrowser(plotsUrlCharacter)
				}
			},
		)
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_CharacterFragment"
		private const val SCROLL_POSITION_X = "scrollPositionX"
		private const val SCROLL_POSITION_Y = "scrollPositionY"
	}
}