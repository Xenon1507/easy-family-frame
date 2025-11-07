# 📱 Benutzer-Anleitung: Easy Family Frame

Eine Schritt-für-Schritt-Anleitung für die Einrichtung und Nutzung deines digitalen Bilderrahmens.

## 📋 Inhaltsverzeichnis

1. [Was du brauchst](#was-du-brauchst)
2. [Installation](#installation)
3. [Erste Einrichtung](#erste-einrichtung)
4. [Bilder übertragen](#bilder-übertragen)
5. [Tipps & Tricks](#tipps--tricks)
6. [Problemlösung](#problemlösung)
7. [FAQ](#faq)

---

## 🛒 Was du brauchst

### Hardware

**Für den Bilderrahmen:**
- 📱 **Android-Tablet oder Smartphone** (mindestens Android 6.0)
  - Empfehlung: 8-10 Zoll Display
  - Gebrauchte Tablets ab 30-50€ auf eBay Kleinanzeigen
  - Beispiele: Samsung Galaxy Tab A, Lenovo Tab M8/M10, Amazon Fire HD
- 🔌 **USB-Netzteil** (zum Dauerbetrieb)
- 📶 **WLAN-Zugang** (nur für Bildübertragung, danach optional)
- 🖼️ **Tablet-Ständer** (optional, 5-15€)

**Für die Bildübertragung:**
- 📱 **Smartphone mit Android** (mindestens Android 6.0)
- 📶 **Selbes WLAN** wie der Bilderrahmen

### Software
- **Easy Family Frame** (beide Apps aus diesem Repository)
- **Android Studio** (für Installation, alternativ APK-Export)

---

## 📥 Installation

### Option 1: Mit Android Studio (Entwickler)

1. **Repository klonen**
   ```bash
   git clone https://github.com/Xenon1507/easy-family-frame.git
   cd easy-family-frame
   ```

2. **Projekt in Android Studio öffnen**
   - File → Open → `easy-family-frame` Ordner auswählen
   - Warte auf Gradle Sync (kann 2-5 Minuten dauern)

3. **Kiosk-App installieren** (auf Tablet/Bilderrahmen)
   - Tablet via USB-Kabel mit PC verbinden
   - USB-Debugging aktivieren (siehe [USB-Debugging aktivieren](#usb-debugging-aktivieren))
   - In Android Studio: Run Configuration auf `kiosk-app` stellen
   - Play-Button (▶) drücken
   - Tablet als Zielgerät auswählen
   - Warten bis App installiert und gestartet ist

4. **Companion-App installieren** (auf Smartphone)
   - Smartphone via USB verbinden
   - USB-Debugging aktivieren
   - Run Configuration auf `companion-app` stellen
   - Play-Button drücken
   - Smartphone als Zielgerät auswählen

### Option 2: APK-Installation (Endbenutzer)

**APKs erstellen:**
```bash
./gradlew kiosk-app:assembleRelease
./gradlew companion-app:assembleRelease
```

APKs finden in:
- `kiosk-app/build/outputs/apk/release/kiosk-app-release.apk`
- `companion-app/build/outputs/apk/release/companion-app-release.apk`

**APKs installieren:**
1. APK-Dateien auf Gerät kopieren (via USB, E-Mail, Cloud)
2. "Installation aus unbekannten Quellen" aktivieren
3. APK öffnen und installieren

---

## ⚙️ Erste Einrichtung

### Schritt 1: Bilderrahmen vorbereiten

1. **Tablet einschalten** und mit WLAN verbinden
2. **Entwickleroptionen aktivieren**:
   - Einstellungen → Über das Tablet
   - 7× auf "Build-Nummer" tippen
3. **USB-Debugging aktivieren** (für Installation)
4. **Display-Timeout deaktivieren**:
   - Einstellungen → Display → Bildschirm-Timeout → "Nie"
5. **"Nicht stören"-Modus aktivieren** (optional):
   - Verhindert störende Benachrichtigungen

### Schritt 2: Kiosk-App starten

1. **App öffnen**: "Family Frame" auf dem Tablet
2. **Berechtigungen erteilen**:
   - Speicher (für Bildverwaltung)
   - Benachrichtigungen (für Status-Info)
3. **Fullscreen-Modus** aktiviert sich automatisch
4. **Initiale Anzeige**: "Keine Bilder vorhanden"

### Schritt 3: IP-Adresse notieren

Die Kiosk-App zeigt eine **persistente Benachrichtigung** mit der IP-Adresse:

```
Family Frame Empfänger
Bereit zum Empfangen von Bildern auf 192.168.1.100:8080
```

**Notiere dir die IP-Adresse** (z.B. `192.168.1.100`) - du brauchst sie für die manuelle Verbindung.

💡 **Tipp**: Ziehe die Benachrichtigungsleiste herunter, um die IP zu sehen, ohne die Slideshow zu unterbrechen.

### Schritt 4: Companion-App einrichten

1. **App öffnen**: "Family Frame Companion" auf dem Smartphone
2. **Berechtigungen erteilen**:
   - Speicher/Fotos (für Bildauswahl)
   - Netzwerk (für Gerätesuche)
3. **Automatische Suche** startet sofort

---

## 📤 Bilder übertragen

### Methode 1: Automatische Geräte-Erkennung (empfohlen)

1. **Companion-App öffnen**
2. **Warte auf Gerätescan** (~30-60 Sekunden)
   - Zeigt "Suche nach Geräten..." mit Ladeanimation
3. **Gerät auswählen**:
   - Tippe auf "Family Frame (192.168.1.100:8080)"
   - Gerät wird blau markiert
4. **Bilder hinzufügen**:
   - Button "Bilder hinzufügen" tippen
   - Fotos aus Galerie auswählen (mehrere möglich)
   - Mit Häkchen bestätigen
5. **Bilder senden**:
   - Button "Bilder senden" tippen
   - Fortschrittsbalken beobachten
   - Warte auf "✓ X Bild(er) erfolgreich übertragen"
6. **Fertig!** 🎉
   - Wechsle zum Tablet
   - Slideshow startet automatisch

### Methode 2: Manuelle Verbindung

Falls die automatische Suche nicht funktioniert:

1. **"Manuelle Verbindung" ausklappen**:
   - Pfeil-Button neben "Manuelle Verbindung" tippen
2. **IP-Adresse eingeben**:
   - Trage die notierte IP ein (z.B. `192.168.1.100`)
   - Port: `8080` (Standard)
3. **"Verbinden" tippen**
4. **Gerät erscheint in der Liste** und ist ausgewählt
5. **Weiter mit Schritt 4** von Methode 1

---

## 💡 Tipps & Tricks

### Bildqualität optimieren

**Auflösung:**
- Für 8" Tablet: 1280×800 px ausreichend
- Für 10" Tablet: 1920×1200 px empfohlen
- Große Bilder (>5MB) können Upload verlangsamen

**Format:**
- JPEG: Beste Kompatibilität, kleinere Dateigröße
- PNG: Höhere Qualität, größere Dateien
- HEIC: Nicht empfohlen (Kompatibilitätsprobleme)

**Seitenverhältnis:**
- 16:10 für die meisten Tablets
- Hochformat-Bilder werden automatisch angepasst

### Slideshow-Intervall ändern

Aktuell: **30 Sekunden** (fest)

**Anpassen** (erfordert Code-Änderung):
1. Öffne `kiosk-app/src/main/java/.../ui/screens/SlideshowScreen.kt`
2. Finde Zeile: `delay(30000L) // 30 seconds`
3. Ändere auf gewünschte Zeit in Millisekunden:
   - 15 Sekunden: `15000L`
   - 1 Minute: `60000L`
   - 2 Minuten: `120000L`
4. App neu bauen und installieren

💡 **Geplant**: UI-basierte Konfiguration in Version 2.0

### Tablet dauerhaft im Kiosk-Modus

**Android 6-8:**
- Einstellungen → Sicherheit → Bildschirm anheften
- App starten → Multitasking → Anheften

**Android 9+:**
- Settings → Digital Wellbeing → Focus Mode
- Oder: "Screen Pinning" in Developer Options

**Dedizierter Kiosk-Modus** (erfordert Root):
- Apps wie "Kiosk Browser Lockdown" nutzen
- Verhindert versehentliches Beenden

### Energiesparen

**Display-Helligkeit reduzieren:**
- Manuell: Einstellungen → Display → Helligkeit (z.B. 50%)
- Geplant: Auto-Helligkeit basierend auf Tageszeit

**Zeitgesteuerte Slideshow:**
- Geplant: Slideshow nur von 8:00-22:00 Uhr
- Display aus während der Nacht

### Bilder organisieren

**Aktuell**: Alle Bilder in einer Liste

**Geplant** (Version 2.0):
- Kategorien/Alben (z.B. "Urlaub 2024", "Familie")
- Smart-Alben nach Datum
- Favoriten markieren

---

## 🔧 Problemlösung

### Problem: "Keine Geräte gefunden"

**Mögliche Ursachen:**

1. **Nicht im selben WLAN**
   - ✅ Prüfe: Smartphone und Tablet im selben Netzwerk?
   - ✅ Gast-WLAN oft isoliert → Haupt-WLAN nutzen

2. **Firewall blockiert**
   - ✅ Router-Firewall: "Client-Isolation" deaktivieren
   - ✅ Android-Firewall (z.B. NetGuard): Ausnahme für App

3. **Kiosk-App nicht gestartet**
   - ✅ Prüfe: Ist "Family Frame" auf Tablet geöffnet?
   - ✅ Prüfe: Benachrichtigung mit IP-Adresse sichtbar?

4. **Port blockiert**
   - ✅ Prüfe: Andere App nutzt Port 8080?
   - ✅ Lösung: Andere App beenden

**Workaround:**
- Manuelle Verbindung mit IP-Adresse aus Benachrichtigung

### Problem: Upload schlägt fehl

**Fehler: "Keine Antwort vom Server"**
- ✅ Prüfe Netzwerkverbindung (Ping-Test)
- ✅ Tablet neu starten
- ✅ Kiosk-App neu starten

**Fehler: "Bild zu groß"**
- ✅ Bild auf <10MB verkleinern
- ✅ Komprimierungs-App nutzen (z.B. "Photo Compressor")

**Fehler: "Speicher voll"**
- ✅ Tablet-Speicher prüfen (Einstellungen → Speicher)
- ✅ Alte Bilder löschen (aktuell nur via Code möglich)

### Problem: Slideshow zeigt Bilder nicht

**Schwarzer Bildschirm:**
- ✅ Prüfe: Wurde mindestens 1 Bild hochgeladen?
- ✅ Kiosk-App neu starten
- ✅ Logs prüfen (Android Studio → Logcat)

**Bilder verzerrt:**
- ✅ Aspect Ratio prüfen
- ✅ ContentScale auf `Fit` (Standard) belassen

**Bilder wechseln nicht:**
- ✅ Timer prüft alle 30 Sekunden
- ✅ Bei nur 1 Bild: Kein Wechsel (expected)

### Problem: App stürzt ab

**Häufige Ursachen:**

1. **Out of Memory** (zu viele große Bilder)
   - ✅ Bilder komprimieren
   - ✅ Anzahl Bilder reduzieren (<100)

2. **Veraltetes Android** (< 6.0)
   - ✅ Min SDK 23 (Android 6.0) erforderlich

3. **Beschädigte Datenbank**
   - ✅ App-Daten löschen: Einstellungen → Apps → Family Frame → Speicher → Daten löschen
   - ⚠️ Löscht alle Bilder!

**Logs sammeln:**
```bash
adb logcat | grep "FamilyFrame"
```

### Problem: Tablet wird heiß

**Überhitzung vermeiden:**
- ✅ Tablet nicht in direktem Sonnenlicht
- ✅ Helligkeit reduzieren (50% oder weniger)
- ✅ Hülle entfernen (bessere Kühlung)
- ✅ Slideshow-Intervall erhöhen (weniger CPU-Last)

---

## ❓ FAQ (Häufig gestellte Fragen)

### Allgemein

**Q: Brauche ich dauerhaft Internet?**
A: Nein! Nach dem Upload funktioniert alles offline. Internet nur für Upload nötig.

**Q: Kann ich mehrere Bilderrahmen nutzen?**
A: Ja! Installiere Kiosk-App auf jedem Gerät. Companion-App findet alle automatisch.

**Q: Wie viele Bilder kann ich speichern?**
A: Hängt vom Tablet-Speicher ab. Bei 16GB Speicher: ~500-1000 Bilder (je nach Größe).

**Q: Unterstützt die App Videos?**
A: Noch nicht. Geplant für Version 2.0.

### Sicherheit & Datenschutz

**Q: Sind meine Bilder sicher?**
A: Ja! Bilder bleiben lokal auf dem Tablet. Keine Cloud-Uploads (außer du konfigurierst WebDAV/Immich).

**Q: Brauche ich ein Passwort?**
A: Aktuell nicht. Da alles im lokalen WLAN läuft, ist das nicht nötig. PIN-Schutz ist für Version 2.0 geplant.

**Q: Kann die App meine Daten sammeln?**
A: Nein! Keine Telemetrie, keine Analytics, keine Tracking-Pixel. 100% privat.

### Technisch

**Q: Funktioniert die App mit Android 14?**
A: Ja! Target SDK 34 (Android 14). Getestet bis Android 14.

**Q: Kann ich die App auf Fire HD Tablets nutzen?**
A: Ja, aber Google Play Services installieren (Anleitung: [XDA Forums](https://forum.xda-developers.com/)).

**Q: Wie ändere ich den Server-Port?**
A: Code ändern in `ImageReceiverService.kt`, Zeile 30: `private val port = 8080`

**Q: Kann ich SD-Karten-Speicher nutzen?**
A: Noch nicht. Geplant für Version 2.0.

### Fehlerbehebung

**Q: Warum findet die Suche mein Gerät nicht?**
A: Siehe [Problemlösung: "Keine Geräte gefunden"](#problem-keine-geräte-gefunden)

**Q: App startet nicht auf Android 6?**
A: Prüfe, ob alle System-Updates installiert sind. Mindestens Android 6.0.1 empfohlen.

**Q: Kann ich Bilder wieder löschen?**
A: Aktuell nur via App-Daten löschen (löscht ALLE Bilder). Einzelbild-Löschung in Version 2.0 geplant.

---

## 📞 Support & Community

### Hilfe bekommen

1. **GitHub Issues**: [github.com/Xenon1507/easy-family-frame/issues](https://github.com/Xenon1507/easy-family-frame/issues)
2. **Discussions**: [github.com/Xenon1507/easy-family-frame/discussions](https://github.com/Xenon1507/easy-family-frame/discussions)

### Beitragen

Wir freuen uns über:
- 🐛 Bug-Reports
- 💡 Feature-Requests
- 📖 Dokumentations-Verbesserungen
- 🔧 Pull Requests

---

## 📚 Weitere Ressourcen

- **[README.md](../README.md)** - Projekt-Übersicht
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Technische Dokumentation
- **[Android Developer Docs](https://developer.android.com/)** - Offizielle Android-Docs

---

## 📝 Anhang

### USB-Debugging aktivieren

**Android 6-14:**
1. Einstellungen → Über das Telefon/Tablet
2. 7× auf "Build-Nummer" tippen
3. "Entwickleroptionen" aktiviert
4. Zurück → Entwickleroptionen
5. "USB-Debugging" aktivieren
6. Gerät mit PC verbinden
7. "USB-Debugging zulassen" bestätigen

### APK-Signierung (für Release-Builds)

```bash
# Keystore erstellen
keytool -genkey -v -keystore release.keystore -alias easy-family-frame -keyalg RSA -keysize 2048 -validity 10000

# APK signieren
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore release.keystore app-release-unsigned.apk easy-family-frame

# Zipalign
zipalign -v 4 app-release-unsigned.apk app-release.apk
```

---

**Viel Spaß mit deinem digitalen Bilderrahmen!** 🖼️✨

Bei Fragen oder Problemen: Erstelle ein Issue auf GitHub oder kontaktiere die Community.

---

**Letzte Aktualisierung:** 2025-11-07
**Version:** 1.0.0
**Autor:** Easy Family Frame Team
