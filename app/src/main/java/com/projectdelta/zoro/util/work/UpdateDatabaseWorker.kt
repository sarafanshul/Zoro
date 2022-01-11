package com.projectdelta.zoro.util.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.data.repository.MessageRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Removes the messages from Database where [Message.seen] == True
 * - Runs everytime on app_startup
 * - Uses [CoroutineWorker] for working in background thread
 */
@HiltWorker
class UpdateDatabaseWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: MessageRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object{
        val workerRequest : WorkRequest
            get() = OneTimeWorkRequestBuilder<UpdateDatabaseWorker>().build()
    }

    override suspend fun doWork(): Result {

        withContext(Dispatchers.IO){
            repository.deleteAllMessagesFilteredBySeen(true)
        }
        return Result.success()

    }
}