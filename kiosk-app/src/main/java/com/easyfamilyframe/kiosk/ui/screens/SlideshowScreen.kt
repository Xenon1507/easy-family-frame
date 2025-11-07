package com.easyfamilyframe.kiosk.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.easyfamilyframe.kiosk.viewmodels.SlideshowViewModel
import com.easyfamilyframe.shared.models.ImageData
import kotlinx.coroutines.delay

@Composable
fun SlideshowScreen(
    viewModel: SlideshowViewModel = viewModel()
) {
    val currentImage by viewModel.currentImage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val imageCount by viewModel.imageCount.collectAsState()

    // Auto-advance slideshow
    LaunchedEffect(currentImage) {
        if (currentImage != null) {
            delay(30000L) // 30 seconds
            viewModel.nextImage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when {
            isLoading -> {
                LoadingView()
            }
            errorMessage != null -> {
                ErrorView(errorMessage!!)
            }
            currentImage != null -> {
                ImageView(currentImage!!)
            }
            imageCount == 0 -> {
                EmptyStateView()
            }
        }
    }
}

@Composable
private fun ImageView(image: ImageData) {
    val context = LocalContext.current

    Crossfade(
        targetState = image.id,
        animationSpec = tween(1000),
        label = "image_crossfade"
    ) { imageId ->
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(image.filePath)
                .crossfade(true)
                .build(),
            contentDescription = image.fileName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun ErrorView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Fehler: $message",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
private fun EmptyStateView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "📱",
                fontSize = 64.sp,
                color = Color.White
            )
            Text(
                text = "Keine Bilder vorhanden",
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Verwende die Companion App, um Bilder zu übertragen",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
