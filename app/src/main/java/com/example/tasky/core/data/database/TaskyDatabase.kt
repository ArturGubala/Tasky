package com.example.tasky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [],
    version = 1,
)

abstract class TaskyDatabase: RoomDatabase() {
}
