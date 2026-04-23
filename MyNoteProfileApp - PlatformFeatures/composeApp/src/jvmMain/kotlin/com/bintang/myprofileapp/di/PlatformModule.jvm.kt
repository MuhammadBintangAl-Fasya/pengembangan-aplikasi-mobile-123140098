package com.bintang.myprofileapp.di

import com.bintang.myprofileapp.db.DatabaseDriverFactory
import com.bintang.myprofileapp.platform.BatteryInfo
import com.bintang.myprofileapp.platform.DeviceInfo
import com.bintang.myprofileapp.platform.NetworkMonitor
import org.koin.dsl.module

actual val platformModule = module {
    single { DatabaseDriverFactory() }
    single { DeviceInfo() }
    single { NetworkMonitor() }
    single { BatteryInfo() }
}
