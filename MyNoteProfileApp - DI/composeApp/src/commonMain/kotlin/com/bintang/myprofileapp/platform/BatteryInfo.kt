package com.bintang.myprofileapp.platform

expect class BatteryInfo {
    fun getBatteryLevel(): Int
    fun isCharging(): Boolean
}
