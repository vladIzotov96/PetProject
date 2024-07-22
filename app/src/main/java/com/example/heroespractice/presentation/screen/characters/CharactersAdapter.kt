package com.example.heroespractice.presentation.screen.characters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.heroespractice.R
import com.example.heroespractice.data.HeroesService
import com.example.heroespractice.databinding.ItemCharacterRectangleBinding
import com.example.heroespractice.databinding.ItemCharacterSquareBinding
import com.example.heroespractice.presentation.model.character.CharacterPresentation
import org.koin.java.KoinJavaComponent

class CharactersAdapter(
	private var characters: List<CharacterPresentation>,
	private val context: Context,
	private val onClick: () -> Unit,
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	private companion object {
		const val RECTANGLE_LAYOUT_TYPE = 1
		const val SQUARE_LAYOUT_TYPE = 2
	}

	override fun getItemViewType(position: Int): Int =
		if (heroesService.isAlternateLayout) SQUARE_LAYOUT_TYPE else RECTANGLE_LAYOUT_TYPE

	override fun getItemCount(): Int = characters.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = if (viewType == RECTANGLE_LAYOUT_TYPE) {
			ItemCharacterRectangleBinding.inflate(inflater, parent, false)
		} else {
			ItemCharacterSquareBinding.inflate(inflater, parent, false)
		}
		return CharacterViewHolder(binding, context)
	}

	override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
		val character = characters[position]
		val viewType = getItemViewType(position)
		holder.apply {
			bind(character, viewType)
			itemView.setOnClickListener {
				heroesService.clickedCharacter = character.id
				onClick()
			}
		}
	}

	fun toggleLayout() {
		heroesService.isAlternateLayout = !heroesService.isAlternateLayout
		notifyDataSetChanged()
	}

	fun toggleSorting() {
		heroesService.isSortedByComicsQuantity = !heroesService.isSortedByComicsQuantity
		characters = if (heroesService.isSortedByComicsQuantity) {
			characters.sortedByDescending { it.comicsQuantity }
		} else {
			characters.sortedBy { it.name }
		}
		notifyDataSetChanged()
	}

	class CharacterViewHolder(
		private val binding: ViewBinding,
		private val context: Context,
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(character: CharacterPresentation, viewType: Int) {
			val characterThumbnail: ImageView
			val characterName: TextView
			val characterDescription: TextView
			val characterComicsDescriptionQuantity: TextView
			val strokeWidthValue: Float
			val centerRadiusValue: Float

			if (viewType == RECTANGLE_LAYOUT_TYPE) {
				characterThumbnail = (binding as ItemCharacterRectangleBinding).characterThumbnail
				characterName = binding.characterName
				characterDescription = binding.characterDescription
				characterComicsDescriptionQuantity = binding.comicQuantity
				strokeWidthValue = 5f
				centerRadiusValue = 20f
			} else {
				characterThumbnail = (binding as ItemCharacterSquareBinding).characterThumbnail
				characterName = binding.characterName
				characterDescription = binding.characterDescription
				characterComicsDescriptionQuantity = binding.comicQuantity
				strokeWidthValue = 10f
				centerRadiusValue = 40f
			}
			characterName.text = character.name
			characterDescription.text = character.description
			characterComicsDescriptionQuantity.text = character.comicsDescriptionQuantity
			if (character.thumbnail.isNotEmpty()) {
				val circularProgressDrawable = CircularProgressDrawable(context).apply {
					strokeWidth = strokeWidthValue
					centerRadius = centerRadiusValue
					start()
				}
				characterThumbnail.let {
					Glide.with(it.context)
						.load(character.thumbnail)
						.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
						.placeholder(circularProgressDrawable)
						.error(R.drawable.icon_logo_avengers_grey)
						.into(characterThumbnail)
				}
			} else {
				characterThumbnail.setImageResource(R.drawable.icon_logo_avengers_grey)
			}
		}
	}
}