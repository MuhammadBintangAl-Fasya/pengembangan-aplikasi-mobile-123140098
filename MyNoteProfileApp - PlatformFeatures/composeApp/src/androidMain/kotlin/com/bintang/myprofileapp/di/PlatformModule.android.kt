package com.bintang.myprofileapp.di

import com.bintang.myprofileapp.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { com.bintang.myprofileapp.platform.DeviceInfo() }
    single { com.bintang.myprofileapp.platform.BatteryInfo(androidContext()) }
    single { com.bintang.myprofileapp.platform.NetworkMonitor(androidContext()) }
}
