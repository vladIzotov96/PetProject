package com.example.heroespractice.presentation.screen.character.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.heroespractice.presentation.screen.character.viewpager.comics.ComicsFragment
import com.example.heroespractice.presentation.screen.character.viewpager.events.EventsFragment

class ViewPagerAdapter(
	fragmentManager: FragmentManager,
	lifecycle: Lifecycle,
	isNeedComicsRequest: Boolean,
	isNeedEventsRequest: Boolean,
) :
	FragmentStateAdapter(fragmentManager, lifecycle) {
	private val fragments = listOf(
		ComicsFragment.newInstance(isNeedComicsRequest),
		EventsFragment.newInstance(isNeedEventsRequest)
	)

	override fun getItemCount(): Int = fragments.size

	override fun createFragment(position: Int): Fragment = fragments[position]

}