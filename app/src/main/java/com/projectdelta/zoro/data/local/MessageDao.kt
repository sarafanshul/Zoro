@file:Suppress("unused")

package com.projectdelta.zoro.data.local

import androidx.room.*
import com.projectdelta.zoro.data.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMessage(message: Message)

    @Update
    abstract fun updateMessage(message: Message)

    @Delete
    abstract fun deleteMessage(message: Message)

    @Query("SELECT * FROM MESSAGE_TABLE WHERE seen =:seen")
    protected abstract fun getAllMessagesFilteredBySeenBase(seen: Boolean): Flow<List<Message>>

    /**
     * Fetches all messages filtered by [Message.seen].
     *
     * *Uses Base Function to avoid false-positives emissions of data when unrelated change in table*
     */
    fun getAllMessagesFilteredBySeen(seen: Boolean): Flow<List<Message>> =
        getAllMessagesFilteredBySeenBase(seen).distinctUntilChanged()

    @Query("SELECT * FROM MESSAGE_TABLE WHERE seen =:seen AND senderId =:senderId")
    protected abstract fun getAllMessagesFilteredBySeenAndSenderBase(
        senderId: String,
        seen: Boolean
    ): Flow<List<Message>>

    fun getAllMessagesFilteredBySeenAndSender(
        senderId: String,
        seen: Boolean
    ): Flow<List<Message>> =
        getAllMessagesFilteredBySeenAndSenderBase(senderId, seen).distinctUntilChanged()

    @Query("SELECT * FROM MESSAGE_TABLE WHERE seen =:seen AND senderId =:senderId")
    abstract fun getAllMessagesFilteredBySeenAndSenderOffline(
        senderId: String,
        seen: Boolean
    ) : List<Message>


    @Query("DELETE FROM MESSAGE_TABLE WHERE seen =:seen")
    abstract fun deleteAllMessagesFilteredBySeen(seen: Boolean)

}