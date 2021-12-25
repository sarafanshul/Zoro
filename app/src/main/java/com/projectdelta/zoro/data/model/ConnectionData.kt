package com.projectdelta.zoro.data.model

/**
 * API Param of of `Update/Delete` connection as of API update *Luffy-1.0.2-STABLE*
 * @param senderUser connection initiator id
 * @param receiverUser connection receiver id
 */
data class ConnectionData(
    val senderUser : String? = null,
    val receiverUser : String? = null,
) : BaseDataModel(){

    override fun copy() =
        ConnectionData(senderUser, receiverUser)

}
