package com.projectdelta.zoro.util.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.zoro.util.system.lang.dpToPx

/**
 * Space [RecyclerView.ItemDecoration] for [RecyclerView], adds space of [spaceInDp] to all sides of an item
 *
 * Usage
 * ```Kotlin
 * recyclerView.addItemDecoration(SimpleItemDecoration())
 * OR
 * recyclerView.addItemDecoration(SimpleItemDecoration(16))
 * ```
 */
class SpaceItemDecoration(space: Int = 10) : RecyclerView.ItemDecoration() {

    private val spaceInDp = space.dpToPx.toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = spaceInDp
        outRect.right = spaceInDp
        outRect.bottom = spaceInDp
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceInDp
        }
    }
}