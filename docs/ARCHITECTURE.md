# 🏗️ Architektur-Dokumentation: Easy Family Frame

## 📐 System-Übersicht

Easy Family Frame ist eine **Offline-First** Android-Lösung für digitale Bilderrahmen mit optionaler Cloud-Synchronisation. Das System besteht aus drei Hauptkomponenten in einer Mono-Repo-Struktur.

```
┌─────────────────────────────────────────────────────────────────┐
│                         SYSTEM ARCHITECTURE                      │
└─────────────────────────────────────────────────────────────────┘

        SMARTPHONE                          TABLET/FRAME
    ┌──────────────────┐                ┌──────────────────┐
    │  COMPANION APP   │                │    KIOSK APP     │
    │                  │                │                  │
    │  ┌────────────┐  │   HTTP/8080   │  ┌────────────┐  │
    │  │ Image      │  │───────────────>│  │ HTTP       │  │
    │  │ Picker     │  │                │  │ Server     │  │
    │  └────────────┘  │                │  └────────────┘  │
    │  ┌────────────┐  │                │        │         │
    │  │ Device     │  │   Discovery   │  ┌──────▼──────┐  │
    │  │ Scanner    │  │<──────────────│  │ Storage     │  │
    │  └────────────┘  │                │  │ Manager     │  │
    │  ┌────────────┐  │                │  └──────┬──────┘  │
    │  │ Upload     │  │                │         │         │
    │  │ Service    │  │                │  ┌──────▼──────┐  │
    │  └────────────┘  │                │  │ Slideshow   │  │
    │                  │                │  │ UI          │  │
    └──────────────────┘                │  └─────────────┘  │
            │                           │         │         │
            │                           │  ┌──────▼──────┐  │
            └───────────────────────────┼─>│ Room DB     │  │
                  SHARED MODULE         │  └─────────────┘  │
                                        └──────────────────┘

                   OPTIONAL (FUTURE)
              ┌──────────────────────┐
              │  CLOUD SOURCES       │
              │  ┌────────────────┐  │
              │  │ WebDAV         │  │
              │  │ (Netcup, etc.) │  │
              │  └────────────────┘  │
              │  ┌────────────────┐  │
              │  │ Immich API     │  │
              │  └────────────────┘  │
              └──────────────────────┘
                        │
                        ▼
                  KIOSK APP
               (Scheduled Sync)
```

## 🎯 Architektur-Prinzipien

### 1. **Offline-First**
- Alle Kernfunktionen ohne Internet
- Lokale Datenhaltung als Primary Source
- Cloud als optionale Sync-Quelle

### 2. **Separation of Concerns**
- Drei Module mit klarer Verantwortung
- Shared Models für Konsistenz
- Lose Kopplung über HTTP/REST

### 3. **Android Compatibility**
- Min SDK 23 (Android 6.0, 2015)
- Target SDK 34 (Android 14, 2024)
- Jetpack Compose mit Compat-Layer

### 4. **Lightweight & Efficient**
- Minimaler Memory Footprint
- Effizientes Bild-Caching
- Battery-aware Scheduling

## 📦 Modul-Struktur

### **Shared Module** (Library)
Gemeinsame Datenmodelle und Datenbank-Logik.

```kotlin
shared/
├── models/
│   ├── ImageData.kt          // Bild-Metadaten (Room Entity)
│   ├── Config.kt             // App-Konfiguration
│   └── TransferMessage.kt    // Netzwerk-Protokoll
├── database/
│   ├── AppDatabase.kt        // Room Database
│   └── ImageDao.kt           // Database Access Object
└── build.gradle.kts
```

**Kernklassen:**

```kotlin
@Entity
data class ImageData(
    val id: Long,
    val filePath: String,      // Lokaler Pfad
    val fileName: String,      // Original-Name
    val fileSize: Long,        // Größe in Bytes
    val dateAdded: Long,       // Timestamp
    val source: String?        // "smartphone", "webdav", "immich"
)
```

### **Kiosk-App** (Application)
Haupt-App für den Bilderrahmen.

```kotlin
kiosk-app/
├── services/
│   └── ImageReceiverService.kt   // HTTP Server (Ktor)
├── ui/
│   ├── screens/
│   │   └── SlideshowScreen.kt    // Fullscreen Slideshow
│   └── theme/                     // Material Theme
├── utils/
│   ├── ImageStorageManager.kt    // Lokale Speicherung
│   └── NetworkUtils.kt           // IP-Adresse etc.
├── viewmodels/
│   └── SlideshowViewModel.kt     // Business Logic
└── MainActivity.kt                // Entry Point
```

**Technologien:**
- **Jetpack Compose** - Deklarative UI
- **Ktor Server (Netty)** - Leichtgewichtiger HTTP Server
- **Coil** - Async Image Loading
- **Room** - SQLite Abstraction
- **Coroutines** - Asynchrone Programmierung

**HTTP Server Endpoints:**

| Endpoint | Methode | Beschreibung |
|----------|---------|--------------|
| `/discover` | GET | Device Discovery (gibt Name, IP zurück) |
| `/health` | GET | Health Check |
| `/upload` | POST | Bild-Upload (Multipart) |
| `/stats` | GET | Statistiken (Anzahl Bilder, Speicher) |

### **Companion-App** (Application)
Smartphone-App zum Übertragen von Bildern.

```kotlin
companion-app/
├── models/
│   └── Device.kt                 // Kiosk-Geräte
├── services/
│   ├── DeviceScanner.kt          // Netzwerk-Scan
│   └── ImageUploadService.kt     // HTTP Client
├── ui/
│   ├── screens/
│   │   └── MainScreen.kt         // Haupt-UI
│   └── theme/
├── viewmodels/
│   └── MainViewModel.kt          // Business Logic
└── MainActivity.kt
```

**Technologien:**
- **Jetpack Compose** - Moderne UI
- **Ktor Client (Android)** - HTTP Client
- **Material 3** - Design System
- **Coroutines** - Async Operations

## 🔄 Datenfluss

### 1. **Device Discovery**

```
┌─────────────┐                    ┌─────────────┐
│ Companion   │                    │ Kiosk App   │
│ App         │                    │             │
└──────┬──────┘                    └──────┬──────┘
       │                                  │
       │  1. Scan 192.168.1.1-254        │
       ├────────────────────────────────>│
       │  GET /discover                   │
       │                                  │
       │  2. Response: 200 OK            │
       │<────────────────────────────────┤
       │  {deviceName, version}          │
       │                                  │
       │  3. Add to Device List          │
       │                                  │
```

### 2. **Image Upload**

```
┌─────────────┐                    ┌─────────────┐
│ Companion   │                    │ Kiosk App   │
│ App         │                    │             │
└──────┬──────┘                    └──────┬──────┘
       │                                  │
       │  1. Select Images (Gallery)     │
       │     [image1.jpg, image2.jpg]    │
       │                                  │
       │  2. For each image:             │
       │     POST /upload                 │
       │     Content-Type: multipart     │
       ├────────────────────────────────>│
       │     Body: image bytes            │
       │                                  │
       │                           3. Save to
       │                              /files/images/
       │                              IMG_<timestamp>.jpg
       │                                  │
       │                           4. Insert into
       │                              Room Database
       │                                  │
       │  5. Response: 200 OK            │
       │<────────────────────────────────┤
       │  {success: true, imageId: 123}  │
       │                                  │
       │  6. Update Progress             │
       │     (50% / 100%)                │
       │                                  │
```

### 3. **Slideshow Display**

```
┌──────────────────────────────────────────┐
│           SlideshowViewModel             │
└─────────────────┬────────────────────────┘
                  │
                  │ 1. Load All Images
                  │    imageDao.getAllImages()
                  ▼
         ┌────────────────┐
         │   Room DB      │
         │   [img1, img2, │
         │    img3, ...]  │
         └────────┬───────┘
                  │
                  │ 2. Shuffle List
                  ▼
         ┌────────────────┐
         │  Shuffled List │
         └────────┬───────┘
                  │
                  │ 3. Display Image
                  │    with Crossfade
                  ▼
         ┌────────────────┐
         │ SlideshowScreen│
         │  (Compose)     │
         └────────┬───────┘
                  │
                  │ 4. Wait 30 seconds
                  │    delay(30000L)
                  │
                  │ 5. Next Image
                  └────────────────> (back to step 3)
```

## 💾 Datenspeicherung

### Lokale Speicherstruktur

```
/data/data/com.easyfamilyframe.kiosk/
├── databases/
│   └── family_frame_database      // Room SQLite DB
│       ├── images (table)         // Metadaten
│       └── ...
└── files/
    └── images/                     // Bild-Dateien
        ├── IMG_1704636000000.jpg
        ├── IMG_1704636001234.jpg
        └── ...
```

**Vorteile:**
- ✅ Keine Berechtigungen nötig (app-private directory)
- ✅ Automatisches Cleanup bei App-Deinstallation
- ✅ Schneller Zugriff
- ❌ Nicht zugreifbar via File Manager (Sicherheit vs. Bequemlichkeit)

**Alternative (geplant):**
```
/storage/emulated/0/EasyFamilyFrame/
└── images/
```
- ✅ Zugriff via File Manager
- ❌ Benötigt Storage Permission

### Room Database Schema

```sql
CREATE TABLE images (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    filePath TEXT NOT NULL,
    fileName TEXT NOT NULL,
    fileSize INTEGER NOT NULL,
    mimeType TEXT NOT NULL,
    dateAdded INTEGER NOT NULL,
    lastShown INTEGER,
    showCount INTEGER DEFAULT 0,
    category TEXT,
    source TEXT,
    isFavorite INTEGER DEFAULT 0
);

-- Indizes für Performance
CREATE INDEX idx_dateAdded ON images(dateAdded);
CREATE INDEX idx_source ON images(source);
```

## 🌐 Netzwerk-Protokoll

### Discovery Protocol

**Request:**
```http
GET /discover HTTP/1.1
Host: 192.168.1.100:8080
```

**Response:**
```json
{
  "deviceName": "Family Frame",
  "version": "1.0.0",
  "status": "ready"
}
```

### Upload Protocol

**Request:**
```http
POST /upload HTTP/1.1
Host: 192.168.1.100:8080
Content-Type: multipart/form-data; boundary=---boundary

-----boundary
Content-Disposition: form-data; name="image"; filename="photo.jpg"
Content-Type: image/jpeg

<binary image data>
-----boundary--
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Image received successfully",
  "imageId": 42
}
```

**Response (Error):**
```json
{
  "success": false,
  "message": "Error: Disk full"
}
```

## 🔐 Sicherheit & Datenschutz

### Aktuelle Sicherheitsmaßnahmen

1. **Lokales Netzwerk Only**
   - Server bindet nur an 0.0.0.0 (alle lokalen Interfaces)
   - Kein Internet-Zugriff nötig
   - Firewall schützt vor externem Zugriff

2. **Keine Authentifizierung (bewusste Design-Entscheidung)**
   - Familie = vertrauenswürdiges Netzwerk
   - Vereinfacht Nutzung erheblich
   - Für öffentliche Netzwerke NICHT geeignet

3. **Datenschutz**
   - Keine Telemetrie
   - Keine Analytics
   - Keine Cloud-Uploads (optional)
   - 100% lokale Verarbeitung

### Geplante Verbesserungen (Phase 2)

1. **Optional: PIN-Schutz**
   - 4-stelliger PIN für Upload-Endpoint
   - QR-Code mit PIN-Embedding

2. **Optional: TLS/HTTPS**
   - Self-signed Certificate
   - Man-in-the-Middle-Schutz

3. **Optional: API Key**
   - Companion App registriert sich beim Kiosk
   - Kiosk generiert API Key
   - Alle Requests benötigen Key

## ⚡ Performance-Optimierungen

### Bild-Loading (Coil)

```kotlin
AsyncImage(
    model = ImageRequest.Builder(context)
        .data(image.filePath)
        .crossfade(true)           // Smooth transition
        .memoryCacheKey(...)       // Cache in RAM
        .diskCacheKey(...)         // Cache on Disk
        .size(screenWidth, screenHeight) // Scale down
        .build(),
    contentScale = ContentScale.Fit
)
```

**Vorteile:**
- Memory Cache: ~50MB
- Disk Cache: ~250MB
- Nur sichtbare Bilder geladen
- Automatisches Downsampling

### Netzwerk-Scan-Optimierung (geplant)

**Aktuell:**
- Sequential Scan: 254 IPs × 2s = ~8 Minuten 😱

**Geplant:**
- Multicast/Broadcast Discovery
- Parallel Scan mit Kotlin Coroutines
- Ziel: < 5 Sekunden

```kotlin
// Planned implementation
suspend fun scanFast(): List<Device> {
    return coroutineScope {
        (1..254).map { octet ->
            async {
                checkDeviceWithTimeout(
                    "$networkPrefix.$octet",
                    timeout = 500.milliseconds
                )
            }
        }.awaitAll().filterNotNull()
    }
}
```

## 🧪 Testing-Strategie

### Unit Tests
- ViewModels (Business Logic)
- Storage Manager
- Network Utils

### Integration Tests
- Room Database
- HTTP Server Endpoints
- HTTP Client Upload

### UI Tests (Compose)
- Slideshow Navigation
- Image Display
- Upload Flow

**Test-Befehle:**
```bash
./gradlew test                    # Unit Tests
./gradlew connectedAndroidTest   # Integration Tests
```

## 📊 Metriken & Monitoring

### Logging-Strategie

```kotlin
android.util.Log.i("ImageReceiverService", "Server started on port $port")
android.util.Log.e("ImageUploadService", "Upload failed", exception)
```

**Log-Levels:**
- `VERBOSE`: Detaillierte Debug-Infos
- `DEBUG`: Entwickler-Infos
- `INFO`: Wichtige Events (Server Start)
- `WARN`: Potenzielle Probleme
- `ERROR`: Fehler mit Stack-Trace

### Geplante Metriken

- Anzahl übertragener Bilder (pro Session)
- Durchschnittliche Upload-Dauer
- Speicherverbrauch
- Anzahl Slideshow-Loops

## 🔮 Zukünftige Erweiterungen

### Phase 2: Cloud-Sync

```kotlin
interface SyncSource {
    suspend fun listImages(): List<RemoteImage>
    suspend fun downloadImage(id: String): ByteArray
}

class WebDavSyncSource : SyncSource { ... }
class ImmichSyncSource : SyncSource { ... }

class SyncScheduler {
    fun scheduleNightlySync(time: LocalTime) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(...)
    }
}
```

### Phase 3: Advanced Features

- **Kategorien & Alben**: Smart-Gruppierung nach Datum, Ort
- **Video-Support**: Kurze Clips (5-30 Sekunden)
- **Remote-Config**: Companion App als Fernbedienung
- **Face Detection**: Personen-basierte Playlists (ML Kit)

## 🏁 Zusammenfassung

Easy Family Frame ist eine **robuste, offline-first Android-Lösung** für digitale Bilderrahmen mit:

✅ **Einfache Architektur**: 3 Module, klare Verantwortungen
✅ **Moderne Technologien**: Kotlin, Compose, Ktor, Room
✅ **Hohe Kompatibilität**: Android 6+ (SDK 23-34)
✅ **Datenschutz-freundlich**: 100% lokal, keine Telemetrie
✅ **Erweiterbar**: Cloud-Sync und Advanced Features geplant

---

**Letzte Aktualisierung:** 2025-11-07
**Version:** 1.0.0
**Status:** MVP fertig, Phase 2 in Planung
