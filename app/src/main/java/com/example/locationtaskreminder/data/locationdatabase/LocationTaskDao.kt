package com.example.locationtaskreminder.data.locationdatabase

import androidx.room.*
import com.example.locationtaskreminder.data.model.Reminder
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationTaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Reminder)

    @Update
    suspend fun update(task: Reminder)

    @Delete
    suspend fun delete(task: Reminder)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id: Int): Flow<Reminder>

    @Query("SELECT * FROM tasks ORDER BY title ASC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Query("DELETE FROM tasks")
    suspend fun deleteAllReminders()
}