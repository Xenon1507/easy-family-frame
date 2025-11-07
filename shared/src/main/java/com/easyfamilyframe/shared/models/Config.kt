package com.easyfamilyframe.shared.models

/**
 * Configuration for the family frame
 */
data class Config(
    /** Slideshow interval in seconds */
    val slideshowIntervalSeconds: Int = 30,

    /** Whether to shuffle images */
    val shuffleEnabled: Boolean = true,

    /** Screen brightness (0-100) */
    val screenBrightness: Int = 100,

    /** Whether to enable sync */
    val syncEnabled: Boolean = false,

    /** Sync source type */
    val syncSourceType: SyncSourceType = SyncSourceType.NONE,

    /** WebDAV URL (if applicable) */
    val webdavUrl: String? = null,

    /** WebDAV username */
    val webdavUsername: String? = null,

    /** WebDAV password (stored securely in production) */
    val webdavPassword: String? = null,

    /** Immich server URL */
    val immichUrl: String? = null,

    /** Immich API key */
    val immichApiKey: String? = null,

    /** Sync schedule time (HH:mm format) */
    val syncScheduleTime: String = "03:00",

    /** HTTP server port for receiving images from companion app */
    val httpServerPort: Int = 8080,

    /** Device name for identification */
    val deviceName: String = "Family Frame"
)

enum class SyncSourceType {
    NONE,
    WEBDAV,
    IMMICH,
    SMARTPHONE
}
