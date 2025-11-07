package com.easyfamilyframe.kiosk.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.easyfamilyframe.kiosk.MainActivity
import com.easyfamilyframe.kiosk.R
import com.easyfamilyframe.kiosk.utils.ImageStorageManager
import com.easyfamilyframe.kiosk.utils.NetworkUtils
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import java.io.File

class ImageReceiverService : Service() {
    private var server: NettyApplicationEngine? = null
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private val port = 8080

    private lateinit var imageStorageManager: ImageStorageManager

    override fun onCreate() {
        super.onCreate()
        imageStorageManager = ImageStorageManager(this)
        startForegroundWithNotification()
        startServer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundWithNotification() {
        val channelId = createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }
        )

        val ipAddress = NetworkUtils.getIPAddress(this)
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Family Frame Empfänger")
            .setContentText("Bereit zum Empfangen von Bildern auf $ipAddress:$port")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "image_receiver_channel"
            val channelName = "Image Receiver"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification for image receiver service"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            return channelId
        }
        return ""
    }

    private fun startServer() {
        serviceScope.launch {
            try {
                server = embeddedServer(Netty, port = port, host = "0.0.0.0") {
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                        })
                    }

                    routing {
                        // Discovery endpoint
                        get("/discover") {
                            call.respond(
                                mapOf(
                                    "deviceName" to "Family Frame",
                                    "version" to "1.0.0",
                                    "status" to "ready"
                                )
                            )
                        }

                        // Health check
                        get("/health") {
                            call.respond(HttpStatusCode.OK, mapOf("status" to "healthy"))
                        }

                        // Upload image
                        post("/upload") {
                            try {
                                val multipart = call.receiveMultipart()
                                var fileName: String? = null
                                var fileBytes: ByteArray? = null

                                multipart.forEachPart { part ->
                                    when (part) {
                                        is PartData.FileItem -> {
                                            fileName = part.originalFileName ?: "image_${System.currentTimeMillis()}.jpg"
                                            fileBytes = part.streamProvider().readBytes()
                                        }
                                        else -> {}
                                    }
                                    part.dispose()
                                }

                                if (fileName != null && fileBytes != null) {
                                    val imageData = imageStorageManager.saveImage(
                                        fileName!!,
                                        fileBytes!!,
                                        "smartphone"
                                    )

                                    call.respond(
                                        HttpStatusCode.OK,
                                        mapOf(
                                            "success" to true,
                                            "message" to "Image received successfully",
                                            "imageId" to imageData.id
                                        )
                                    )
                                } else {
                                    call.respond(
                                        HttpStatusCode.BadRequest,
                                        mapOf(
                                            "success" to false,
                                            "message" to "No image data received"
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                call.respond(
                                    HttpStatusCode.InternalServerError,
                                    mapOf(
                                        "success" to false,
                                        "message" to "Error: ${e.message}"
                                    )
                                )
                            }
                        }

                        // Get image count
                        get("/stats") {
                            val count = imageStorageManager.getImageCount()
                            call.respond(
                                mapOf(
                                    "imageCount" to count,
                                    "storageUsed" to imageStorageManager.getStorageUsed()
                                )
                            )
                        }
                    }
                }.start(wait = false)

                android.util.Log.i("ImageReceiverService", "Server started on port $port")
            } catch (e: Exception) {
                android.util.Log.e("ImageReceiverService", "Error starting server", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        server?.stop(1000, 2000)
        serviceJob.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}
