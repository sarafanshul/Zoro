/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.system.lang

/**
 * Interface to handle crashes
 */
interface ExceptionListener {
    fun uncaughtException(thread: Thread, throwable: Throwable)
}
