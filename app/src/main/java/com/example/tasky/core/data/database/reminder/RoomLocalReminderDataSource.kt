package com.example.tasky.core.data.database.reminder

import android.database.sqlite.SQLiteFullException
import com.example.tasky.agenda.domain.model.Reminder
import com.example.tasky.core.data.database.reminder.dao.ReminderDao
import com.example.tasky.core.data.database.reminder.mappers.toReminder
import com.example.tasky.core.data.database.reminder.mappers.toReminderEntity
import com.example.tasky.core.domain.data.ReminderLocalDataSource
import com.example.tasky.core.domain.util.DataError
import com.example.tasky.core.domain.util.EmptyResult
import com.example.tasky.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalReminderDataSource(
    private val reminderDao: ReminderDao,
) : ReminderLocalDataSource {

    override fun getReminder(id: String): Flow<Reminder> {
        return reminderDao.getReminder(id = id)
            .map { it.toReminder() }
    }

    override fun getRemindersIds(): Flow<List<String>> {
        return reminderDao.getRemindersIds()
    }

    override fun getReminderForDay(
        startOfDay: Long,
        endOfDay: Long,
    ): Flow<List<Reminder>> {
        return reminderDao.getRemindersForDay(startOfDay = startOfDay, endOfDay = endOfDay)
            .map { remindersEntities ->
                remindersEntities.map { it.toReminder() }
            }
    }

    override suspend fun upsertReminder(reminder: Reminder): EmptyResult<DataError.Local> {
        return try {
            val entity = reminder.toReminderEntity()
            reminderDao.upsertReminder(entity)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Error(DataError.Local.DiskFull)
        }
    }

    override suspend fun insertReminders(reminders: List<Reminder>): EmptyResult<DataError.Local> {
        return try {
            val entities = reminders.map { it.toReminderEntity() }
            reminderDao.insertReminders(entities)
            Result.Success(Unit)
        } catch (_: SQLiteFullException) {
            Result.Success(Unit)
        }
    }

    override suspend fun deleteReminder(id: String) {
        reminderDao.deleteReminder(id)
    }

    override suspend fun deleteReminders() {
        reminderDao.deleteReminders()
    }
}
