package com.example.lesson_4_kislyakov

import androidx.recyclerview.widget.RecyclerView
import android.graphics.Rect
import android.view.View

/*
    that class contains a lot of bicycles
    it sets padding between recyclerview items (only inner ones) and only for the 3x2 and then 4x1 grid
    would be nice to find some better solution
*/

class ItemDecorationAlbumColumns(private val mSizeGridSpacingPx: Int, private var mGridSize: Int) :
    RecyclerView.ItemDecoration() {

    private var mNeedLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - mSizeGridSpacingPx.toFloat() * (mGridSize - 1)) / mGridSize).toInt()
        val padding = parent.width / mGridSize - frameWidth
        val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < mGridSize) {
            outRect.top = 0 //top padding
        } else {
            outRect.top = mSizeGridSpacingPx
        }
        if(itemPosition > 5){ // there I explain that grid will contain only 1 column though (after 5 item)
            mGridSize = 1
        }else mGridSize = 2

        if(mGridSize == 1){
            outRect.left = 0 // left padding and right are 0 if column is alone
            outRect.right = 0
        }else if (itemPosition % mGridSize == 0) {
            outRect.left = 0 // left
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0 // right
            outRect.left = padding
        }
        outRect.bottom = 0 // bottom
    }
}
