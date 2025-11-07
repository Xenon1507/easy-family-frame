package com.easyfamilyframe.companion.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyfamilyframe.companion.CompanionApplication
import com.easyfamilyframe.companion.models.Device
import com.easyfamilyframe.companion.services.DeviceScanner
import com.easyfamilyframe.companion.services.ImageUploadService
import com.easyfamilyframe.companion.ui.screens.UploadStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val context = CompanionApplication.instance
    private val deviceScanner = DeviceScanner()
    private val uploadService = ImageUploadService(context)

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice: StateFlow<Device?> = _selectedDevice.asStateFlow()

    private val _selectedImages = MutableStateFlow<List<Uri>>(emptyList())
    val selectedImages: StateFlow<List<Uri>> = _selectedImages.asStateFlow()

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus: StateFlow<UploadStatus> = _uploadStatus.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    init {
        scanForDevices()
    }

    fun scanForDevices() {
        viewModelScope.launch {
            _isScanning.value = true
            val foundDevices = deviceScanner.scan()
            _devices.value = foundDevices
            _isScanning.value = false

            // Auto-select first device if none selected
            if (_selectedDevice.value == null && foundDevices.isNotEmpty()) {
                _selectedDevice.value = foundDevices.first()
            }
        }
    }

    fun selectDevice(device: Device) {
        _selectedDevice.value = device
    }

    fun connectManually(ipAddress: String, port: Int) {
        val device = Device(
            name = "Manual Device",
            ipAddress = ipAddress,
            port = port
        )
        _devices.value = _devices.value + device
        _selectedDevice.value = device
    }

    fun setSelectedImages(uris: List<Uri>) {
        _selectedImages.value = uris
        _uploadStatus.value = UploadStatus.Idle
    }

    fun uploadImages() {
        val device = _selectedDevice.value ?: return
        val images = _selectedImages.value

        if (images.isEmpty()) return

        viewModelScope.launch {
            try {
                _uploadStatus.value = UploadStatus.Uploading(0f)

                var successCount = 0
                images.forEachIndexed { index, uri ->
                    val success = uploadService.uploadImage(device, uri)
                    if (success) successCount++

                    val progress = (index + 1).toFloat() / images.size
                    _uploadStatus.value = UploadStatus.Uploading(progress)
                }

                _uploadStatus.value = if (successCount == images.size) {
                    UploadStatus.Success("$successCount Bild(er) erfolgreich übertragen")
                } else {
                    UploadStatus.Error("$successCount von ${images.size} Bildern übertragen")
                }

                // Clear selection after upload
                _selectedImages.value = emptyList()
            } catch (e: Exception) {
                _uploadStatus.value = UploadStatus.Error("Fehler: ${e.message}")
            }
        }
    }
}
