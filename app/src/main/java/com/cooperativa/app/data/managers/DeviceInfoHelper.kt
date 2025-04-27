// Archivo: app/src/main/java/com/cooperativa/app/data/managers/DeviceInfoHelper.kt
package com.cooperativa.app.data.managers

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.text.format.Formatter
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInfoHelper @Inject constructor(
    private val context: Context
) {
    // Obtener nombre del dispositivo y modelo
    fun getDeviceName(): String {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            Build.MODEL.replaceFirstChar { it.uppercase() }
        } else {
            Build.MANUFACTURER.replaceFirstChar { it.uppercase() } + " " + Build.MODEL
        }
    }
    fun getAndroidId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }
    fun getDeviceModel(): String = Build.MODEL

    // Obtener dirección MAC (con limitaciones en Android 6+)
    fun getMacAddress(): String {
        return try {
            NetworkInterface.getNetworkInterfaces()
                .toList()
                .firstOrNull { it.name.equals("wlan0", ignoreCase = true) }
                ?.hardwareAddress
                ?.joinToString(":") { String.format("%02X", it) }
                ?: "02:00:00:00:00:00" // Valor por defecto en Android 6+
        } catch (ex: Exception) {
            "02:01:00:00:00:00"
        }
    }

    // Obtener dirección IP
    fun getIpAddress(): String {
        return try {
            NetworkInterface.getNetworkInterfaces()
                .toList()
                .flatMap { it.inetAddresses.toList() }
                .firstOrNull { !it.isLoopbackAddress && it.hostAddress?.contains(':') == false }
                ?.hostAddress
                ?: ""
        } catch (ex: Exception) {
            ""
        }
    }

    // Obtener Android ID
    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    // Generar un ID más estable combinando varias propiedades
    fun getStableDeviceId(): String {
        val androidId = getDeviceId()
        val deviceInfo = "${Build.MANUFACTURER}${Build.BOARD}${Build.BRAND}"
        return "$androidId-${UUID.nameUUIDFromBytes(deviceInfo.toByteArray())}"
    }
}