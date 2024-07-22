package com.example.heroespractice.presentation.screen.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.heroespractice.R
import com.example.heroespractice.presentation.model.content_provider.ContactPresentation

class ContactsAdapter(
	private val context: Context,
	private val contacts: List<ContactPresentation>,
) :
	BaseAdapter() {

	override fun getCount(): Int = contacts.size

	override fun getItem(position: Int) = contacts[position]

	override fun getItemId(position: Int) = position.toLong()

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val view: View = convertView ?: LayoutInflater.from(context)
			.inflate(R.layout.item_contact, parent, false)

		val contactNameTextView: TextView = view.findViewById(R.id.titleOption)
		val contactNumberTextView: TextView = view.findViewById(R.id.descriptionOption)
		val iconNameOption: TextView = view.findViewById(R.id.iconNameOption)
		val iconBackgroundOption: View = view.findViewById(R.id.iconBackgroundColorOption)

		contacts[position].apply {
			contactNameTextView.text = name
			contactNumberTextView.text = number
			iconNameOption.text = icon.name
			iconBackgroundOption.setBackgroundColor(ContextCompat.getColor(context, icon.color))
		}

		return view
	}
}