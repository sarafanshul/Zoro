@file:Suppress("unused")

package com.projectdelta.zoro.util.system.lang

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.*
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

fun View.changeColor(newColor: Int) {
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            newColor
        )
    )
}

fun EditText.disableContentInteraction() {
    keyListener = null
    isFocusable = false
    isFocusableInTouchMode = false
    isCursorVisible = false
    setBackgroundResource(android.R.color.transparent)
    clearFocus()
}

fun EditText.enableContentInteraction() {
    keyListener = EditText(context).keyListener
    isFocusable = true
    isFocusableInTouchMode = true
    isCursorVisible = true
    setBackgroundResource(android.R.color.white)
    requestFocus()
    if (text != null) {
        setSelection(text.length)
    }
}

/**
 * Use only from Activities, don't use from Fragment (with getActivity) or from Dialog/DialogFragment
 * [Author](https://github.com/sanogueralorenzo/Android-Kotlin-Clean-Architecture)
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * Use only from Activities, don't use from Fragment (with getActivity) or from Dialog/DialogFragment
 */
fun Activity.showKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

}

/**
 * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
 */
@Suppress("DEPRECATION")
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

/**
 * Use everywhere except from Activity (Custom View, Fragment, Dialogs, DialogFragments).
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Removes all item decorations from [RecyclerView]
 *
 * [See More](https://stackoverflow.com/a/59092408/11718077)
 */
fun <T : RecyclerView> T.removeItemDecorations() {
    while (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

/**
 * Extension function for setting left drawable in [TextView]
 * uses : `setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom)`
 *
 * **set 0 where you don't want images**
 *
 * [Source](https://stackoverflow.com/a/59652513/11718077)
 */
@Suppress("DEPRECATION")
fun TextView.leftDrawable(@DrawableRes id: Int = 0, @DimenRes sizeRes: Int = 0, @ColorInt color: Int = 0, @ColorRes colorRes: Int = 0) {
    val drawable = ContextCompat.getDrawable(context, id)
    if (sizeRes != 0) {
        val size = resources.getDimensionPixelSize(sizeRes)
        drawable?.setBounds(0, 0, size, size)
    }
    if (color != 0) {
        drawable?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    } else if (colorRes != 0) {
        val colorInt = ContextCompat.getColor(context, colorRes)
        drawable?.setColorFilter(colorInt, PorterDuff.Mode.SRC_ATOP)
    }
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

/**
 * Slide Up and Down for bottom nav with animations ,[slideUp] and [slideDown] uses [translationObjectY]
 * to get interpolator object animator.
 * Sourced from [here](https://stackoverflow.com/a/65251040/11718077)
 */
fun View.translationObjectY(
    startY: Float,
    endY: Float,
    duration: Long = 200L
) : ObjectAnimator {
    return ObjectAnimator.ofFloat(this, "translationY", startY, endY).apply {
        this.duration = duration
        interpolator = LinearOutSlowInInterpolator()
        start()
    }
}

fun BottomNavigationView.slideDown(todoCallback: (() -> Unit)? = null){
    if (translationY == 0f) {
        translationObjectY(0f, height.toFloat() + marginBottom.toFloat()).apply {
            doOnEnd{
                todoCallback?.invoke()
            }
        }
    }
}
fun BottomNavigationView.slideUp(todoCallback: (() -> Unit)? = null){
    if( translationY == height.toFloat() + marginBottom.toFloat() ){
        translationObjectY(height.toFloat() + marginBottom.toFloat(), 0f).apply {
            doOnEnd {
                todoCallback?.invoke()
            }
        }
    }
}

/**
 * Extension method to inflate a view directly from its parent.
 * @param layout the layout to inflate.
 * @param attachToRoot whether to attach the view to the root or not. Defaults to false.
 */
fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

/**
 * Update the [RecyclerView]'s [ListAdapter] with the provided list of items.
 *
 * Originally, [ListAdapter] will not update the view if the provided list is the same as
 * currently loaded one. This is by design as otherwise the provided DiffUtil.ItemCallback<T>
 * could never work - the [ListAdapter] must have the previous list if items to compare new
 * ones to using provided diff callback.
 * However, it's very convenient to call [ListAdapter.submitList] with the same list and expect
 * the view to be updated. This extension function handles this case by making a copy of the
 * list if the provided list is the same instance as currently loaded one.
 *
 * For more info see 'RJFares' and 'insa_c' answers
 * [here](https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-reyclerview)
 */
fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.updateList(list: List<T>?) {
    // ListAdapter<>.submitList() contains (stripped):
    //  if (newList == mList) {
    //      // nothing to do
    //      return;
    //  }
    this.submitList(if (list == this.currentList) list.toList() else list)
}