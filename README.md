# 🖼️ Easy Family Frame

Eine **offline-first** digitale Bilderrahmen-Lösung für Android, die speziell für ältere Android-Geräte (Android 6+) entwickelt wurde und ohne Cloud-Anbindung funktioniert.

## 🎯 Kernfeatures

### ✅ **Offline-First Architektur**
- Vollständig funktionsfähig ohne Internetverbindung
- Lokale Bildspeicherung im internen Speicher
- Keine Cloud-Abhängigkeit

### 📱 **Zwei Apps, ein System**
1. **Kiosk-App** (für Bilderrahmen/Tablets)
   - Fullscreen-Slideshow mit Crossfade-Animationen
   - Automatische Bildrotation (konfigurierbar)
   - HTTP-Server zum Empfangen von Bildern
   - Minimaler Ressourcenverbrauch

2. **Companion-App** (für Smartphones)
   - Automatische Geräte-Erkennung im lokalen Netzwerk
   - Bildauswahl aus der Galerie
   - Multi-Image Upload
   - Einfache Bedienung

### 🔄 **Flexible Synchronisation**
- **Primär**: Direktübertragung vom Smartphone (ohne Cloud)
- **Optional**: WebDAV-Sync (geplant)
- **Optional**: Immich API Integration (geplant)

### 🛠️ **Technische Highlights**
- **Min SDK 23** (Android 6.0) - Läuft auf alten Geräten
- **Target SDK 34** (Android 14) - Modern und sicher
- **Kotlin + Jetpack Compose** - Moderne UI-Entwicklung
- **Room Database** - Effiziente Metadaten-Verwaltung
- **Ktor** - Leichtgewichtiges Networking
- **Coil** - Optimiertes Bild-Loading

## 📁 Projektstruktur

```
easy-family-frame/
├── kiosk-app/              # Bilderrahmen-App
│   ├── src/main/java/
│   │   ├── services/       # HTTP Server für Bildempfang
│   │   ├── ui/             # Compose UI (Slideshow)
│   │   ├── utils/          # Storage & Netzwerk
│   │   └── viewmodels/     # Business Logic
│   └── build.gradle.kts
│
├── companion-app/          # Smartphone Companion-App
│   ├── src/main/java/
│   │   ├── models/         # Device Models
│   │   ├── services/       # Device Scanner & Upload
│   │   ├── ui/             # Compose UI
│   │   └── viewmodels/     # Business Logic
│   └── build.gradle.kts
│
├── shared/                 # Gemeinsame Module
│   ├── src/main/java/
│   │   ├── database/       # Room Database
│   │   └── models/         # Datenmodelle
│   └── build.gradle.kts
│
├── docs/                   # Dokumentation
│   ├── ARCHITECTURE.md
│   └── USER_GUIDE.md
│
└── build.gradle.kts        # Root Build Config
```

## 🚀 Quick Start

### Voraussetzungen
- **Android Studio** (Arctic Fox oder neuer)
- **Android SDK 23+**
- **JDK 17**
- Zwei Android-Geräte im selben WLAN:
  - 1x Tablet/altes Smartphone als Bilderrahmen
  - 1x Smartphone zum Senden von Bildern

### Installation

1. **Repository klonen**
   ```bash
   git clone https://github.com/Xenon1507/easy-family-frame.git
   cd easy-family-frame
   ```

2. **Projekt in Android Studio öffnen**
   - File → Open → easy-family-frame auswählen
   - Gradle Sync abwarten

3. **Kiosk-App auf Bilderrahmen installieren**
   - Gerät via USB verbinden
   - Run Configuration: `kiosk-app` auswählen
   - Run (▶) klicken

4. **Companion-App auf Smartphone installieren**
   - Gerät via USB verbinden
   - Run Configuration: `companion-app` auswählen
   - Run (▶) klicken

### Erste Schritte

1. **Kiosk-App starten** auf dem Bilderrahmen
   - App startet im Fullscreen-Modus
   - Zeigt zunächst "Keine Bilder vorhanden"
   - Notiere die IP-Adresse aus der Benachrichtigung (z.B. `192.168.1.100:8080`)

2. **Companion-App öffnen** auf dem Smartphone
   - App sucht automatisch nach Bilderrahmen im Netzwerk
   - Alternativ: Manuelle IP-Eingabe möglich

3. **Bilder übertragen**
   - "Bilder hinzufügen" tippen
   - Fotos aus Galerie auswählen
   - "Bilder senden" tippen
   - Fertig! ✅

4. **Slideshow genießen**
   - Kiosk-App zeigt Bilder automatisch
   - Wechsel alle 30 Sekunden
   - Crossfade-Animationen

## 📖 Ausführliche Dokumentation

- **[Architektur-Dokumentation](docs/ARCHITECTURE.md)** - Technische Details
- **[Benutzer-Anleitung](docs/USER_GUIDE.md)** - Schritt-für-Schritt-Anleitungen

## 🎨 Features im Detail

### Kiosk-App
- ✅ Fullscreen-Modus (versteckt Statusleiste & Navigation)
- ✅ Automatische Bildrotation mit Shuffle
- ✅ HTTP-Server auf Port 8080
- ✅ Persistente Benachrichtigung mit IP-Adresse
- ✅ Room-Datenbank für Metadaten
- ✅ Effizientes Bild-Caching mit Coil
- ✅ Keep-Screen-On während Slideshow
- ⏳ Konfigurierbare Slideshow-Intervalle (geplant)
- ⏳ Helligkeitssteuerung (geplant)

### Companion-App
- ✅ Automatische Geräte-Erkennung im Netzwerk
- ✅ Multi-Image-Upload
- ✅ Upload-Progress-Anzeige
- ✅ Manuelle IP-Eingabe
- ✅ Material 3 Design
- ⏳ Kategorisierung von Bildern (geplant)
- ⏳ QR-Code-Pairing (geplant)

## 🛣️ Roadmap

### Phase 1 (MVP) ✅ FERTIG
- [x] Kiosk-App mit Slideshow
- [x] Companion-App mit Upload
- [x] Lokale Speicherung
- [x] HTTP-Server für Bildempfang

### Phase 2 (Cloud-Sync) 🚧 In Planung
- [ ] WebDAV-Client für nächtlichen Sync
- [ ] Immich API Integration
- [ ] Geplante Sync-Zeiten
- [ ] Sync-Status-Anzeige

### Phase 3 (Advanced Features) 💡 Ideen
- [ ] Kategorien & Smart-Alben
- [ ] Favoriten-Markierung
- [ ] Bild-Statistiken
- [ ] Fernsteuerung via Companion-App
- [ ] QR-Code für schnelles Pairing
- [ ] Video-Support
- [ ] SD-Karten-Support

## 💰 Hardware-Empfehlungen

### Budget: 40-60€ pro Bilderrahmen

**Option 1: Gebrauchte Android-Tablets**
- Samsung Galaxy Tab A (2016-2019) - ~30-50€
- Lenovo Tab M8/M10 - ~40-60€
- Amazon Fire HD 8/10 (mit Google Play) - ~40-50€

**Option 2: Dedizierte Digital Frames**
- Android-basierte Frames mit WLAN - ~40-60€
- Vorteil: Oft mit Standfuß
- Nachteil: Meist Android 6/7

**Tipp**: Alte Firmen-Tablets oder Familiengeräte recyceln!

## 🔧 Entwicklung

### Build Commands

```bash
# Debug Build (beide Apps)
./gradlew assembleDebug

# Release Build (signiert)
./gradlew assembleRelease

# Tests ausführen
./gradlew test

# Lint-Checks
./gradlew lint
```

### Konfiguration

Die Konfiguration erfolgt aktuell im Code. Geplant ist eine UI-basierte Konfiguration.

**Slideshow-Intervall ändern** (kiosk-app):
```kotlin
// In SlideshowScreen.kt, Zeile ~30
delay(30000L) // 30 Sekunden → gewünschte Zeit in ms
```

**Server-Port ändern** (kiosk-app):
```kotlin
// In ImageReceiverService.kt, Zeile ~30
private val port = 8080 // → gewünschter Port
```

## 🐛 Bekannte Einschränkungen

1. **Netzwerk-Scan dauert lange** (~30-60 Sekunden)
   - Scannt alle 254 IPs im Netzwerk
   - Optimierung geplant (Multicast/Broadcast)

2. **Keine automatische Reconnect-Logik**
   - Bei WLAN-Unterbrechung muss Service neu gestartet werden
   - Watchdog geplant

3. **Große Bilder werden nicht komprimiert**
   - Upload von 20MB+ Bildern möglich, aber langsam
   - Client-seitige Komprimierung geplant

## 🤝 Beitragen

Contributions sind willkommen!

1. Fork das Repository
2. Erstelle einen Feature-Branch (`git checkout -b feature/AmazingFeature`)
3. Committe deine Changes (`git commit -m 'Add AmazingFeature'`)
4. Push zum Branch (`git push origin feature/AmazingFeature`)
5. Öffne einen Pull Request

## 📄 Lizenz

Dieses Projekt steht unter der **MIT Lizenz** - siehe [LICENSE](LICENSE) Datei.

## 🙏 Danksagungen

- **ImmichFrame** & **ImmichKiosk** - Inspiration für die Slideshow-Logik
- **Frameo** - Inspiration für das Konzept
- **Jetpack Compose** - Moderne Android UI
- **Ktor** - Networking Framework

## 📧 Kontakt

Bei Fragen oder Problemen:
- GitHub Issues: https://github.com/Xenon1507/easy-family-frame/issues
- Diskussionen: https://github.com/Xenon1507/easy-family-frame/discussions

---

**Made with ❤️ for families who want to share memories without the cloud**
