package com.bintang.myprofileapp.platform

actual class DeviceInfo {
    actual fun getDeviceName(): String = "${System.getProperty("os.name")} ${System.getProperty("os.arch")}"
    actual fun getOsVersion(): String = "JVM ${System.getProperty("java.version")}"
    actual fun getAppVersion(): String = "1.0.0"
}
