package com.easyfamilyframe.companion.services

import com.easyfamilyframe.companion.CompanionApplication
import com.easyfamilyframe.companion.models.Device
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import java.net.InetAddress
import java.net.NetworkInterface

class DeviceScanner {
    private val client = HttpClient(Android) {
        engine {
            connectTimeout = 2000
            socketTimeout = 2000
        }
    }

    suspend fun scan(): List<Device> = withContext(Dispatchers.IO) {
        val devices = mutableListOf<Device>()
        val localIp = getLocalIpAddress()

        if (localIp == null) {
            return@withContext devices
        }

        // Extract network prefix (e.g., "192.168.1")
        val ipParts = localIp.split(".")
        if (ipParts.size != 4) {
            return@withContext devices
        }
        val networkPrefix = "${ipParts[0]}.${ipParts[1]}.${ipParts[2]}"

        // Scan common IP range (1-254)
        val jobs = (1..254).map { lastOctet ->
            async {
                val ip = "$networkPrefix.$lastOctet"
                checkDevice(ip, 8080)
            }
        }

        jobs.awaitAll().filterNotNull().let { devices.addAll(it) }

        devices
    }

    private suspend fun checkDevice(ip: String, port: Int): Device? {
        return try {
            val response = client.get("http://$ip:$port/discover")
            if (response.status.value == 200) {
                Device(
                    name = "Family Frame",
                    ipAddress = ip,
                    port = port
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (!address.isLoopbackAddress && address is java.net.Inet4Address) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun close() {
        client.close()
    }
}
