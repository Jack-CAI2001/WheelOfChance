package com.example.wheelofchance.data

import com.example.wheelofchance.data.local.Entry
import com.example.wheelofchance.data.local.EntryDao
import com.example.wheelofchance.data.local.Wheel
import com.example.wheelofchance.data.local.WheelDao
import kotlinx.coroutines.flow.Flow

class WheelRepository(
    private val wheelDao: WheelDao,
    private val entryDao: EntryDao
) {
    fun getAllWheels(): Flow<List<Wheel>> = wheelDao.getAllWheels()

    fun getWheelById(id: Long): Flow<Wheel?> = wheelDao.getWheelById(id)

    suspend fun insertWheel(wheel: Wheel): Long = wheelDao.insertWheel(wheel)

    suspend fun updateWheel(wheel: Wheel) = wheelDao.updateWheel(wheel)

    suspend fun deleteWheel(wheel: Wheel) = wheelDao.deleteWheel(wheel)

    fun getEntriesForWheel(wheelId: Long): Flow<List<Entry>> = entryDao.getEntriesForWheel(wheelId)

    suspend fun insertEntry(entry: Entry): Long = entryDao.insertEntry(entry)

    suspend fun updateEntry(entry: Entry) = entryDao.updateEntry(entry)

    suspend fun deleteEntry(entry: Entry) = entryDao.deleteEntry(entry)
}
