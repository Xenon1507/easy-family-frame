package com.easyfamilyframe.kiosk.utils

import android.content.Context
import android.webkit.MimeTypeMap
import com.easyfamilyframe.kiosk.KioskApplication
import com.easyfamilyframe.shared.database.ImageDao
import com.easyfamilyframe.shared.models.ImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ImageStorageManager(private val context: Context) {
    private val imageDao: ImageDao = KioskApplication.instance.database.imageDao()
    private val storageDir: File by lazy {
        File(context.filesDir, "images").apply {
            if (!exists()) mkdirs()
        }
    }

    suspend fun saveImage(
        fileName: String,
        imageBytes: ByteArray,
        source: String = "unknown"
    ): ImageData = withContext(Dispatchers.IO) {
        // Generate unique filename
        val timestamp = System.currentTimeMillis()
        val extension = getFileExtension(fileName)
        val uniqueFileName = "IMG_${timestamp}${extension}"

        // Save file to storage
        val imageFile = File(storageDir, uniqueFileName)
        FileOutputStream(imageFile).use { output ->
            output.write(imageBytes)
        }

        // Get MIME type
        val mimeType = getMimeType(extension) ?: "image/jpeg"

        // Create database entry
        val imageData = ImageData(
            filePath = imageFile.absolutePath,
            fileName = fileName,
            fileSize = imageBytes.size.toLong(),
            mimeType = mimeType,
            source = source
        )

        // Insert into database
        val id = imageDao.insertImage(imageData)
        imageData.copy(id = id)
    }

    suspend fun getImageCount(): Int = withContext(Dispatchers.IO) {
        imageDao.getImageCount()
    }

    fun getStorageUsed(): Long {
        return storageDir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    private fun getFileExtension(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot != -1) {
            fileName.substring(lastDot)
        } else {
            ".jpg"
        }
    }

    private fun getMimeType(extension: String): String? {
        val ext = extension.removePrefix(".")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }

    fun getImagesDirectory(): File = storageDir
}
