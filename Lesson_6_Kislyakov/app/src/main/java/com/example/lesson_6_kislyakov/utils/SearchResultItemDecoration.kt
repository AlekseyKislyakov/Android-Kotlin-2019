package com.example.lesson_6_kislyakov.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SearchResultItemDecoration(private val space: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        addSpaceToView(outRect, parent.getChildAdapterPosition(view), parent)

    }

    private fun addSpaceToView(outRect: Rect?, position: Int?, parent: RecyclerView?) {
        if (position == null || parent == null)
            return
        if (position == 0) {
            outRect?.top = space
            outRect?.left = space
            outRect?.right = space
            outRect?.bottom = space
        } else {
            outRect?.left = space
            outRect?.right = space
            outRect?.bottom = space
        }
    }
}