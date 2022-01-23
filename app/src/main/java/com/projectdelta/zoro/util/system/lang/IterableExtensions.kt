/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.system.lang

import com.projectdelta.zoro.data.model.BaseDataModel

@Suppress("UNCHECKED_CAST")
fun <T : BaseDataModel> List<T>.copy(): List<T> {
    return map { it.copy() as T }
}
