package com.projectdelta.zoro.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.projectdelta.zoro.util.Constants.MESSAGE_TABLE

@Keep
@Entity(tableName = MESSAGE_TABLE)
data class Message(

    @PrimaryKey(autoGenerate = true) var id: Int? = null,

    @ColumnInfo(name = "senderId")
    @SerializedName("senderId")
    var senderId: String? = null,

    @ColumnInfo(name = "receiverId")
    @SerializedName("receiverId")
    var receiverId: String? = null,

    @ColumnInfo(name = "data")
    @SerializedName("data")
    var data: String? = null,

    @ColumnInfo(name = "time")
    @SerializedName("time")
    var time: Long? = null,

    @ColumnInfo(name = "seen")
    @SerializedName("seen")
    var seen: Boolean = false,

    @Ignore
    var type: MessageType = MessageType.INCOMING

) : BaseDataModel() {

    companion object {
        enum class MessageType {
            INCOMING, OUTGOING
        }
    }

    override fun copy() =
        Message(id, senderId, receiverId, data, time, seen)

}
