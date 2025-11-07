package com.easyfamilyframe.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Represents an image stored in the family frame
 */
@Entity(tableName = "images")
data class ImageData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Local file path */
    val filePath: String,

    /** Original filename */
    val fileName: String,

    /** File size in bytes */
    val fileSize: Long,

    /** MIME type (e.g., image/jpeg) */
    val mimeType: String,

    /** When the image was added */
    val dateAdded: Long = System.currentTimeMillis(),

    /** When the image was last shown */
    val lastShown: Long? = null,

    /** Number of times shown */
    val showCount: Int = 0,

    /** Optional: Category or album name */
    val category: String? = null,

    /** Optional: Source identifier (e.g., "smartphone", "webdav", "immich") */
    val source: String? = null,

    /** Is this a favorite? */
    val isFavorite: Boolean = false
)
