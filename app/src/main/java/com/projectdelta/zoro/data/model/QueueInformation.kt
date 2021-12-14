package com.projectdelta.zoro.data.model

import java.io.Serializable

/**
 * Data object for Queue info.
 * @param name : Name of Queue as per Rabbit broker
 * @param routingKey : Routing Key of Queue.
 */
data class QueueInformation(
    val name : String,
    val routingKey : String
) : Serializable
