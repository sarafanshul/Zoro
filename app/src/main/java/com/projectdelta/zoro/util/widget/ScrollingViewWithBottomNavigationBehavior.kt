package com.projectdelta.zoro.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Layout behaviour for [CoordinatorLayout] view when using [BottomNavigationView] as contents of view
 * overlaps with the [BottomNavigationView].
 *
 * **Thinking :**
 * I subclassed [AppBarLayout.ScrollingViewBehavior] to adjust the bottom margin of the content view
 * based on the height of the [BottomNavigationView] (if present). This way it should be future proof
 * (hopefully) if the height of the [BottomNavigationView] changes for any reason.
 *
 * *Usage*
 * ```XML
 * app:layout_behavior=".ScrollingViewWithBottomNavigationBehavior"
 * ```
 */
@Suppress("unused")
class ScrollingViewWithBottomNavigationBehavior(context: Context, attrs: AttributeSet) :
    AppBarLayout.ScrollingViewBehavior(context, attrs) {
    // We add a bottom margin to avoid the bottom navigation bar
    private var bottomMargin = 0

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(
            parent,
            child,
            dependency
        ) || dependency is BottomNavigationView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val result = super.onDependentViewChanged(parent, child, dependency)

        return if (dependency is BottomNavigationView && dependency.height != bottomMargin) {
            bottomMargin = dependency.height
            val layout = child.layoutParams as CoordinatorLayout.LayoutParams
            layout.bottomMargin = bottomMargin
            child.requestLayout()
            true
        } else {
            result
        }
    }
}