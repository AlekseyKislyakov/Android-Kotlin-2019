package com.example.lesson8kislyakov.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredItemDecoration(private val space: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams

        addSpaceToView(outRect, parent.getChildAdapterPosition(view), lp.spanIndex, parent)
    }

    private fun addSpaceToView(outRect: Rect?, position: Int?, state: Int?, parent: RecyclerView?) {
        if (position == null || parent == null)
            return
        val grid = parent.layoutManager as StaggeredGridLayoutManager

        // add spaces to top holders
        if (position < grid.spanCount) {
            outRect?.top = space
        }
        // add spaces to right holders
        if (state == grid.spanCount - 1) {
            outRect?.right = space
        }

        // add left&bottom spaces to all holders
        outRect?.left = space
        outRect?.bottom = space


    }
}