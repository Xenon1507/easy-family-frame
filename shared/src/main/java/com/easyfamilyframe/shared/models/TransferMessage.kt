package com.easyfamilyframe.shared.models

/**
 * Message format for transferring images between devices
 */
data class TransferMessage(
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val category: String? = null
)

/**
 * Response from the kiosk app when receiving an image
 */
data class TransferResponse(
    val success: Boolean,
    val message: String,
    val imageId: Long? = null
)

/**
 * Device discovery message for finding kiosk apps on the network
 */
data class DiscoveryMessage(
    val deviceName: String,
    val ipAddress: String,
    val port: Int,
    val version: String = "1.0.0"
)
