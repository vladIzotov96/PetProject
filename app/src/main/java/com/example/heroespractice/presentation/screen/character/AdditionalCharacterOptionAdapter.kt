package com.example.heroespractice.presentation.screen.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.heroespractice.databinding.ItemAdditionalCharacterOptionBinding
import com.example.heroespractice.presentation.model.character.CharacterAdditionalOptionPresentation

class AdditionalCharacterOptionAdapter(
	private val optionsList: List<CharacterAdditionalOptionPresentation>,
) : RecyclerView.Adapter<OptionViewHolder>() {

	override fun getItemCount(): Int = optionsList.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemAdditionalCharacterOptionBinding.inflate(inflater, parent, false)
		return OptionViewHolder(binding)
	}

	override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
		val option = optionsList[position]

		with(holder.binding) {
			iconBackgroundOption.setImageResource(option.icon)
			titleOption.text = option.title
			descriptionOption.text = option.description

			if (optionsList.size - 1 == position) {
				dividerOption.isVisible = false
			} else {
				dividerOption.isVisible = true
			}

			root.setOnClickListener {
				option.clickAction()
			}
		}
	}
}

class OptionViewHolder(
	val binding: ItemAdditionalCharacterOptionBinding
) : RecyclerView.ViewHolder(binding.root)