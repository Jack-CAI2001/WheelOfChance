package com.example.wheelofchance.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WheelDao {
    @Query("SELECT * FROM wheels")
    fun getAllWheels(): Flow<List<Wheel>>

    @Query("SELECT * FROM wheels WHERE id = :id")
    fun getWheelById(id: Long): Flow<Wheel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWheel(wheel: Wheel): Long

    @Update
    suspend fun updateWheel(wheel: Wheel)

    @Delete
    suspend fun deleteWheel(wheel: Wheel)
}
