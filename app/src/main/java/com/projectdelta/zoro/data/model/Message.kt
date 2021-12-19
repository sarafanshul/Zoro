package com.projectdelta.zoro.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.projectdelta.zoro.util.Constants.MESSAGE_TABLE
import java.io.Serializable

@Keep
@Entity(tableName = MESSAGE_TABLE)
data class Message(

    @PrimaryKey(autoGenerate = true)  val id : Int? = null,

    @ColumnInfo(name = "senderId")
    @SerializedName("senderId")
    val senderId : String? = null ,

    @ColumnInfo(name = "receiverId")
    @SerializedName("receiverId")
    val receiverId : String? = null,

    @ColumnInfo(name = "data")
    @SerializedName("data")
    val data : String? = null,

    @ColumnInfo(name = "time")
    @SerializedName("time")
    val time : Long? = null,

    @ColumnInfo(name = "seen")
    @SerializedName("seen")
    var seen : Boolean = false

) : Serializable
