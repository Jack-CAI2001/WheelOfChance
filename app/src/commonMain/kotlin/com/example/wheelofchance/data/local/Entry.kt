package com.example.wheelofchance.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "entries",
    foreignKeys = [
        ForeignKey(
            entity = Wheel::class,
            parentColumns = ["id"],
            childColumns = ["wheelId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["wheelId"])]
)
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val wheelId: Long,
    val text: String,
    val color: String
)
