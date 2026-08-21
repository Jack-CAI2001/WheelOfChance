package com.example.wheelofchance

import android.app.Application
import com.example.wheelofchance.data.WheelRepository
import com.example.wheelofchance.data.local.AppDatabase
import com.example.wheelofchance.data.local.getDatabaseBuilder
import com.example.wheelofchance.data.local.getRoomDatabase

class WheelOfChanceApplication : Application() {
    lateinit var database: AppDatabase
        private set

    lateinit var repository: WheelRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val builder = getDatabaseBuilder(this)
        database = getRoomDatabase(builder)

        repository = WheelRepository(
            database.wheelDao(),
            database.entryDao()
        )
    }
}
