/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.data.model

/**
 * Data object for Queue info.
 * @param name : Name of Queue as per Rabbit broker
 * @param routingKey : Routing Key of Queue.
 */
data class QueueInformation(
    val name: String,
    val routingKey: String
) : BaseDataModel() {

    override fun copy() =
        QueueInformation(name, routingKey)
}
