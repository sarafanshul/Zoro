/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.widget

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.projectdelta.zoro.R
import com.projectdelta.zoro.util.system.lang.getResourceColor

/**
 * It is what it says...
 */
class ThemedSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    init {
        // Background
        setProgressBackgroundColorSchemeColor(context.getResourceColor(R.attr.colorPrimary))
        // This updates the progress arrow color
        setColorSchemeColors(context.getResourceColor(R.attr.colorOnPrimary))
    }
}
