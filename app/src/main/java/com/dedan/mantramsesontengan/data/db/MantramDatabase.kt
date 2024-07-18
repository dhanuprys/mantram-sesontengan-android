package com.dedan.mantramsesontengan.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dedan.mantramsesontengan.model.SavedMantram

@Database(entities = [SavedMantram::class], version = 1, exportSchema = false)
abstract class MantramDatabase : RoomDatabase() {
    abstract fun savedMantramDao(): SavedMantramDao

    companion object {
        const val DATABASE_NAME = "mantram_database"

        @Volatile
        private var Instance: MantramDatabase? = null

        fun getDatabase(context: Context): MantramDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MantramDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}