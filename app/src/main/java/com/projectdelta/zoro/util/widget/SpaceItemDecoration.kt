package com.projectdelta.zoro.util.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.projectdelta.zoro.util.system.lang.dp

/**
 * Space [RecyclerView.ItemDecoration] for [RecyclerView], adds space of [space] dp to all sides of an item
 *
 * Usage
 * ```Kotlin
 * recyclerView.addItemDecoration(SimpleItemDecoration())
 * OR
 * recyclerView.addItemDecoration(SimpleItemDecoration(16))
 * ```
 */
class SpaceItemDecoration(private val space: Int = 10.dp) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space
        }
    }
}