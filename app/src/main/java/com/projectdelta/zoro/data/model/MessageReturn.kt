package com.projectdelta.zoro.data.model

/**
 * Updated API return of `/message/send` as of update *Luffy-1.0.2-STABLE*
 */
data class MessageReturn(
    val messageData: Message? = null ,
    val queueInformation: QueueInformation? = null
) : BaseDataModel(){

    override fun copy() =
        MessageReturn(messageData, queueInformation)
}
