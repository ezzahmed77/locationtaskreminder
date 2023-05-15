package com.example.locationtaskreminder.data.locationdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationtaskreminder.data.model.Reminder

@Database(entities = [Reminder::class], version = 1, exportSchema = false)
abstract class LocationTaskDatabase: RoomDatabase() {

    abstract fun locationTaskDao(): LocationTaskDao

    companion object{
        @Volatile
        private var Instance: LocationTaskDatabase? = null

        fun getDatabase(context: Context): LocationTaskDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, LocationTaskDatabase::class.java, "tasks_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}