package com.example.heroespractice.presentation.screen.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.heroespractice.R
import com.example.heroespractice.databinding.FragmentContactsBinding
import com.example.heroespractice.domain.base.ContextResources
import com.example.heroespractice.presentation.utils.SnackbarUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactsFragment : Fragment() {

	private val viewModel: ContactsViewModel by viewModel()
	private val contextResources: ContextResources by inject()

	private lateinit var binding: FragmentContactsBinding
	private var messageAboutCharacterForContact: String? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		Log.e(TAG, "onCreateView")
		messageAboutCharacterForContact = arguments?.getString(MESSAGE_ABOUT_CHARACTER_FOR_CONTACT)
		binding = FragmentContactsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.e(TAG, "onViewCreated")

		with(viewModel) {
			getContacts()

			contacts.observe(viewLifecycleOwner) { contacts ->
				val listView = binding.listViewContacts

				val adapter = ContactsAdapter(requireContext(), contacts)
				listView.adapter = adapter

				listView.setOnItemClickListener { _, _, position, _ ->
					sendSms(contacts[position].name, contacts[position].number)
				}
			}

			screenDescription.observe(viewLifecycleOwner) { screenDescription ->
				binding.textContacts.text = screenDescription

				if (!screenDescription.isNullOrEmpty()) {
					SnackbarUtil.showSnackbar(
						view,
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
	}

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

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		Log.e(TAG, "onSaveInstanceState")
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
		private const val TAG = "Lifecycle_Fragment_ContactsFragment"
		private const val MESSAGE_ABOUT_CHARACTER_FOR_CONTACT =
			"message_about_character_for_contact"
		private const val SMS_BODY = "sms_body"
	}
}