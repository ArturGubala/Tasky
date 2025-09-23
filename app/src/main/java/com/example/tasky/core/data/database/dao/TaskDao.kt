package com.example.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tasky.core.data.database.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTask(id: String): Flow<TaskEntity>

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query(
        """
        SELECT * FROM task
        WHERE time >= :startOfDay AND time < :endOfDay
    """
    )
    fun getTasksForDay(startOfDay: Long, endOfDay: Long): Flow<TaskEntity>
}
