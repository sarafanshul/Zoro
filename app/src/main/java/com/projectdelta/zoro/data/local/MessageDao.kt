@file:Suppress("unused")

package com.projectdelta.zoro.data.local

import androidx.room.*
import com.projectdelta.zoro.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message)

    @Update
    fun updateMessage(message: Message)

    @Delete
    fun deleteMessage(message: Message)

    @Query("SELECT * FROM MESSAGE_TABLE WHERE seen =:seen")
    fun getAllMessagesFilteredBySeen(seen : Boolean) : Flow<List<Message>>

}