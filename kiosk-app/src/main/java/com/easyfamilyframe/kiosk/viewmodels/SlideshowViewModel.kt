package com.easyfamilyframe.kiosk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyfamilyframe.kiosk.KioskApplication
import com.easyfamilyframe.shared.database.ImageDao
import com.easyfamilyframe.shared.models.ImageData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SlideshowViewModel : ViewModel() {
    private val imageDao: ImageDao = KioskApplication.instance.database.imageDao()

    private val _currentImage = MutableStateFlow<ImageData?>(null)
    val currentImage: StateFlow<ImageData?> = _currentImage.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _imageCount = MutableStateFlow(0)
    val imageCount: StateFlow<Int> = _imageCount.asStateFlow()

    private var allImages: List<ImageData> = emptyList()
    private var currentIndex = 0

    init {
        loadImages()
    }

    private fun loadImages() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                imageDao.getAllImages().collect { images ->
                    allImages = images.shuffled() // Shuffle for variety
                    _imageCount.value = images.size

                    if (images.isNotEmpty()) {
                        currentIndex = 0
                        _currentImage.value = allImages[currentIndex]
                        markCurrentImageAsShown()
                        _errorMessage.value = null
                    } else {
                        _currentImage.value = null
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unbekannter Fehler"
                _isLoading.value = false
            }
        }
    }

    fun nextImage() {
        if (allImages.isEmpty()) return

        currentIndex = (currentIndex + 1) % allImages.size
        _currentImage.value = allImages[currentIndex]
        markCurrentImageAsShown()
    }

    fun previousImage() {
        if (allImages.isEmpty()) return

        currentIndex = if (currentIndex == 0) allImages.size - 1 else currentIndex - 1
        _currentImage.value = allImages[currentIndex]
        markCurrentImageAsShown()
    }

    private fun markCurrentImageAsShown() {
        viewModelScope.launch {
            _currentImage.value?.let { image ->
                imageDao.markImageAsShown(image.id, System.currentTimeMillis())
            }
        }
    }
}
