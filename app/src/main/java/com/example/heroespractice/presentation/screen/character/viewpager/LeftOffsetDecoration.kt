package com.example.heroespractice.presentation.screen.character.viewpager

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LeftOffsetDecoration(private val leftOffset: Int) : RecyclerView.ItemDecoration() {
	override fun getItemOffsets(
		outRect: Rect,
		view: View,
		parent: RecyclerView,
		state: RecyclerView.State
	) {
		if (parent.getChildAdapterPosition(view) == 0) {
			outRect.left = leftOffset
		} else {
			outRect.left = 0
		}
	}
}