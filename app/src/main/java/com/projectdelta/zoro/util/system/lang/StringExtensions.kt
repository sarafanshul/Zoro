/*
 * Copyright (c) 2022. Anshul Saraf
 */

@file:Suppress("unused")

package com.projectdelta.zoro.util.system.lang

import android.text.Editable
import java.util.Locale

fun String.chop(count: Int, replacement: String = "..."): String {
    return if (length > count) {
        take(count - replacement.length) + replacement
    } else {
        this
    }
}

/**
 * Replacement for Kotlin's deprecated `capitalize()` function.
 */
fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

/**
 * Returns true if String is not null | empty | blank
 */
fun String?.isOk(): Boolean {
    return !(this.isNullOrBlank() || this.isNullOrEmpty())
}

/**
 * Converts [String] to [Editable] for using in EditTexts.
 */
fun String.toEditable(): Editable =
    Editable.Factory.getInstance().newEditable(this)
