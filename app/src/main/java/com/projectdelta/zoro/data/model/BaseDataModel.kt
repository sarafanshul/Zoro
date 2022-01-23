/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.model

import java.io.Serializable

abstract class BaseDataModel : Serializable {

    abstract fun copy(): BaseDataModel

}
