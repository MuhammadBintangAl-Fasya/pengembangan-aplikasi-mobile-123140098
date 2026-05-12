package com.bintang.myprofileapp.platform

actual class BatteryInfo {
    actual fun getBatteryLevel(): Int = -1
    actual fun isCharging(): Boolean = false
}
