package com.example.heroespractice.presentation.screen.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.heroespractice.R
import com.example.heroespractice.databinding.FragmentContactsBinding
import com.example.heroespractice.presentation.base.BaseFragment
import com.example.heroespractice.presentation.utils.SnackbarUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : BaseFragment<FragmentContactsBinding>(
	fragmentTag = TAG
) {

	/**Injection*/
	private val viewModel: ContactsViewModel by viewModel()

	/**Fragment fields*/
	private var messageAboutCharacterForContact: String? = null
	private lateinit var adapter: ContactsAdapter

	/**Auxiliary fun for onCreateView() BaseFragment*/
	override fun inflateBinding(
		inflater: LayoutInflater,
		container: ViewGroup?
	): FragmentContactsBinding {
		return FragmentContactsBinding.inflate(inflater, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		messageAboutCharacterForContact = arguments?.getString(MESSAGE_ABOUT_CHARACTER_FOR_CONTACT)

		adapter = ContactsAdapter(requireContext(), emptyList())
		binding.listViewContacts.adapter = adapter

		viewModel.getContacts()
		viewModelObserveContacts()
		viewModelObserveScreenDescription()
		viewModelObserveIsLoading()
	}

	/**
	 * ViewModel observe functions
	 * */

	private fun viewModelObserveContacts() {
		viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
			val listView = binding.listViewContacts

			adapter.updateContacts(contacts)

			listView.setOnItemClickListener { _, _, position, _ ->
				sendSms(contacts[position].name, contacts[position].number)
			}
		}
	}

	private fun viewModelObserveScreenDescription() {
		viewModel.screenDescription.observe(viewLifecycleOwner) { screenDescription ->
			binding.textContacts.text = screenDescription

			if (!screenDescription.isNullOrEmpty()) {
				SnackbarUtil.showSnackbar(
					binding.root,
					screenDescription,
					actionText = contextResources.getString(R.string.ok)
				)
				with(binding) {
					textContacts.text = screenDescription
					textContacts.isVisible = true
					imageContacts.isVisible = true
				}
			}
		}
	}

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

	/**
	 * Auxiliaries functions
	 * */

	private fun sendSms(name: String, phoneNumber: String) {
		val fullMessageWithAddedName =
			"Hi, " +
					name +
					"!\n\n" +
					messageAboutCharacterForContact

		val smsIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phoneNumber"))
		smsIntent.putExtra(SMS_BODY, fullMessageWithAddedName)
		startActivity(smsIntent)
	}

	/**Companion object*/
	companion object {
		private const val TAG = "Lifecycle_Fragment_ContactsFragment"
		private const val MESSAGE_ABOUT_CHARACTER_FOR_CONTACT =
			"message_about_character_for_contact"
		private const val SMS_BODY = "sms_body"
	}
}