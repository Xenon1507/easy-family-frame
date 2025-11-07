package com.easyfamilyframe.companion.services

import android.content.Context
import android.net.Uri
import com.easyfamilyframe.companion.models.Device
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageUploadService(private val context: Context) {
    private val client = HttpClient(Android) {
        engine {
            connectTimeout = 30000
            socketTimeout = 30000
        }
    }

    suspend fun uploadImage(device: Device, imageUri: Uri): Boolean = withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: return@withContext false

            val fileName = getFileName(imageUri)
            val imageBytes = inputStream.readBytes()
            inputStream.close()

            val response = client.post("http://${device.ipAddress}:${device.port}/upload") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("image", imageBytes, Headers.build {
                                append(HttpHeaders.ContentType, "image/*")
                                append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                            })
                        }
                    )
                )
            }

            response.status.isSuccess()
        } catch (e: Exception) {
            android.util.Log.e("ImageUploadService", "Upload failed", e)
            false
        }
    }

    private fun getFileName(uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                it.getString(nameIndex)
            } else {
                "image_${System.currentTimeMillis()}.jpg"
            }
        } ?: "image_${System.currentTimeMillis()}.jpg"
    }

    fun close() {
        client.close()
    }
}
