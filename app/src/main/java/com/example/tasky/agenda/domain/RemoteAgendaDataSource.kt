//package com.example.tasky.agenda.domain
//
//import com.example.tasky.agenda.domain.model.Attendee
//import com.example.tasky.agenda.domain.model.Event
//import com.example.tasky.agenda.domain.model.Reminder
//import com.example.tasky.agenda.domain.model.Task
//import com.example.tasky.core.domain.util.DataError
//import com.example.tasky.core.domain.util.EmptyResult
//import com.example.tasky.core.domain.util.Result
//
//interface RemoteAgendaDataSource {
//    suspend fun getTask(id: String): Result<Task, DataError.Network>
//    suspend fun createTask(task: Task): Result<Task, DataError.Network>
//    suspend fun updateTask(task: Task): Result<Task, DataError.Network>
//    suspend fun deleteTask(id: String): EmptyResult<DataError.Network>
//
//    suspend fun getReminder(id: String): Result<Reminder, DataError.Network>
//    suspend fun createReminder(reminder: Reminder): Result<Reminder, DataError.Network>
//    suspend fun updateReminder(reminder: Reminder): Result<Reminder, DataError.Network>
//    suspend fun deleteReminder(id: String): EmptyResult<DataError.Network>
//
//    suspend fun getEvent(id: String): Result<Event, DataError.Network>
//    suspend fun createEvent(event: Event): Result<Event, DataError.Network>
//    suspend fun updateEvent(event: Event): Result<Event, DataError.Network>
//    suspend fun deleteEvent(id: String): EmptyResult<DataError.Network>
//    suspend fun confirmUpload(ids: List<String>): Result<Event, DataError.Network>
//
//    suspend fun getAttendee(email: String): Result<Attendee, DataError.Network>
//    suspend fun deleteAttendee(id: String): EmptyResult<DataError.Network>
//}
