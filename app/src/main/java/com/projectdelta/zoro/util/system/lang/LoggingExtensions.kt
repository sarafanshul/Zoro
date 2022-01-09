package com.projectdelta.zoro.util.system.lang

import timber.log.Timber

/**
 * Logs any kind of object by just calling Log on it
 * Inline so Timber shows the line number where it was called instead of this class
 */
fun Any?.log(prefix: String = "object:") = Timber.d("$prefix${toString()}")