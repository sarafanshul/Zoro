package com.projectdelta.zoro.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.projectdelta.zoro.data.model.Message
import com.projectdelta.zoro.util.Constants.DATABASE_NAME
import java.util.concurrent.Executors

@Database(
    entities = [
        Message::class,
    ],
    version = 3,
    exportSchema = true
)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {

        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getInstance(application: Application): MessageDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = buildDatabase(application)
                INSTANCE = instance
                return instance
            }
        }

        private fun buildDatabase(application: Application): MessageDatabase {
            return Room.databaseBuilder(
                application,
                MessageDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        INSTANCE?.let {
                            // Prepopulate data here if needed
                            /*
                            it.taskDao().insertData(...)
                             */
                        }
                    }
                }
            }).fallbackToDestructiveMigration()
                .build()
        }

    }

}