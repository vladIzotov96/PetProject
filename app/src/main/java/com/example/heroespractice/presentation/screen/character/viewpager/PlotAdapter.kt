package com.example.heroespractice.presentation.screen.character.viewpager

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
import com.example.heroespractice.databinding.ItemAllPlotBinding
import com.example.heroespractice.databinding.ItemPlotBinding
import com.example.heroespractice.domain.model.comics.PlotDomain
import org.koin.java.KoinJavaComponent

class PlotAdapter(
	private var plots: List<PlotDomain>,
	private val context: Context,
	private val onClick: () -> Unit,
	private val onShowAllClick: () -> Unit,
) : RecyclerView.Adapter<PlotAdapter.PlotViewHolder>() {

	private val heroesService: HeroesService by KoinJavaComponent.inject(clazz = HeroesService::class.java)

	private var isNeedAllItemType = false

	private companion object {
		const val ITEM_TYPE = 0
		const val TO_ALL_ITEM_TYPE = 1
	}

	override fun getItemViewType(position: Int): Int =
		if (position == plots.size && isNeedAllItemType) TO_ALL_ITEM_TYPE else ITEM_TYPE

	override fun getItemCount(): Int =
		if (isNeedAllItemType) (plots.size + 1) else plots.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlotViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = if (viewType == ITEM_TYPE) {
			ItemPlotBinding.inflate(inflater, parent, false)
		} else {
			ItemAllPlotBinding.inflate(inflater, parent, false)
		}
		return PlotViewHolder(binding, context)
	}

	override fun onBindViewHolder(holder: PlotViewHolder, position: Int) {
		if (position != plots.size) {
			val plot = plots[position]
			holder.apply {
				bind(plot)
				itemView.setOnClickListener {
					heroesService.clickedPlot["type"] = plot.type.type
					heroesService.clickedPlot["id"] = plot.id.toString()
					onClick()
				}
			}
		} else {
			holder.apply {
				itemView.setOnClickListener {
					onShowAllClick()
				}
			}
		}
	}

	fun updatePlots(plots: List<PlotDomain>, isNeedAllItemType: Boolean) {
		this.plots = plots
		this.isNeedAllItemType = isNeedAllItemType
		notifyDataSetChanged()
	}

	class PlotViewHolder(
		private val binding: ViewBinding,
		private val context: Context,
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(plot: PlotDomain) {
			val plotThumbnail: ImageView = (binding as ItemPlotBinding).plotImage
			val plotTitle: TextView = binding.plotTitle

			plotTitle.text = plot.title
			if (plot.thumbnail.isNotEmpty()) {
				val circularProgressDrawable = CircularProgressDrawable(context).apply {
					strokeWidth = 5f
					centerRadius = 20f
					start()
				}
				plotThumbnail.let {
					Glide.with(it.context)
						.load(plot.thumbnail)
						.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
						.placeholder(circularProgressDrawable)
						.error(R.drawable.icon_logo_avengers_grey)
						.into(plotThumbnail)
				}
			} else {
				plotThumbnail.setImageResource(R.drawable.icon_logo_avengers_grey)
			}
		}
	}
}