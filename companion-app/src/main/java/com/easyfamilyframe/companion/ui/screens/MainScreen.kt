package com.easyfamilyframe.companion.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.easyfamilyframe.companion.models.Device
import com.easyfamilyframe.companion.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val devices by viewModel.devices.collectAsState()
    val selectedDevice by viewModel.selectedDevice.collectAsState()
    val selectedImages by viewModel.selectedImages.collectAsState()
    val uploadStatus by viewModel.uploadStatus.collectAsState()
    val isScanning by viewModel.isScanning.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        viewModel.setSelectedImages(uris)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Family Frame Companion") },
                actions = {
                    IconButton(onClick = { viewModel.scanForDevices() }) {
                        Icon(
                            imageVector = if (isScanning) Icons.Default.Refresh else Icons.Default.Search,
                            contentDescription = "Scan"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Device Selection Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Verfügbare Bilderrahmen",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (devices.isEmpty() && !isScanning) {
                        Text(
                            text = "Keine Geräte gefunden. Drücke auf das Such-Icon.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (isScanning) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            Text("Suche nach Geräten...")
                        }
                    }

                    devices.forEach { device ->
                        DeviceItem(
                            device = device,
                            isSelected = device == selectedDevice,
                            onSelect = { viewModel.selectDevice(device) }
                        )
                    }
                }
            }

            // Manual Device Input Card
            ManualDeviceCard(
                onConnect = { ip, port ->
                    viewModel.connectManually(ip, port)
                }
            )

            // Image Selection Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Bilder auswählen",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Bilder hinzufügen")
                    }

                    if (selectedImages.isNotEmpty()) {
                        Text(
                            text = "${selectedImages.size} Bild(er) ausgewählt",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Upload Button
            Button(
                onClick = { viewModel.uploadImages() },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedDevice != null && selectedImages.isNotEmpty() && uploadStatus !is UploadStatus.Uploading
            ) {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Bilder senden")
            }

            // Upload Status
            when (val status = uploadStatus) {
                is UploadStatus.Uploading -> {
                    LinearProgressIndicator(
                        progress = status.progress,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("${(status.progress * 100).toInt()}% hochgeladen")
                }
                is UploadStatus.Success -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = "✓ ${status.message}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is UploadStatus.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "✗ ${status.message}",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: Device,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        onClick = onSelect,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${device.ipAddress}:${device.port}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ManualDeviceCard(
    onConnect: (String, Int) -> Unit
) {
    var ipAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("8080") }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Manuelle Verbindung",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand"
                    )
                }
            }

            if (expanded) {
                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text("IP-Adresse") },
                    placeholder = { Text("192.168.1.100") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port") },
                    placeholder = { Text("8080") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        onConnect(ipAddress, port.toIntOrNull() ?: 8080)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = ipAddress.isNotBlank()
                ) {
                    Text("Verbinden")
                }
            }
        }
    }
}

sealed class UploadStatus {
    object Idle : UploadStatus()
    data class Uploading(val progress: Float) : UploadStatus()
    data class Success(val message: String) : UploadStatus()
    data class Error(val message: String) : UploadStatus()
}
