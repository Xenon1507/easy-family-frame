package com.easyfamilyframe.companion

import android.app.Application

class CompanionApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CompanionApplication
            private set
    }
}
