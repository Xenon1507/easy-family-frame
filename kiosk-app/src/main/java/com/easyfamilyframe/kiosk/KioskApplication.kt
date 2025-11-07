package com.easyfamilyframe.kiosk

import android.app.Application
import com.easyfamilyframe.shared.database.AppDatabase

class KioskApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: KioskApplication
            private set
    }
}
