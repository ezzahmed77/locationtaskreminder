package com.example.locationtaskreminder.data.repository

import com.example.locationtaskreminder.data.locationdatabase.LocationTaskDao
import com.example.locationtaskreminder.data.model.Reminder
import kotlinx.coroutines.flow.Flow

interface LocationTaskRepository {
    suspend fun insert(reminder: Reminder)

    suspend fun update(reminder: Reminder)

    suspend fun delete(reminder: Reminder)

    fun getReminder(id: Int): Flow<Reminder>

    fun getAllReminders(): Flow<List<Reminder>>

    suspend fun deleteAllReminders()

}

class LocationTaskRepositoryImpl(
    private val locationTaskDao: LocationTaskDao
): LocationTaskRepository{
    override suspend fun insert(reminder: Reminder) {
        return locationTaskDao.insert(reminder)
    }

    override suspend fun update(reminder: Reminder) {
        return locationTaskDao.update(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        return locationTaskDao.delete(reminder)
    }

    override fun getReminder(id: Int): Flow<Reminder> {
        return locationTaskDao.getTask(id)
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return locationTaskDao.getAllReminders()
    }

    override suspend fun deleteAllReminders() {
        return locationTaskDao.deleteAllReminders()
    }

}