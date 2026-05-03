# 🛡️ Sudarshan Mobile Guard

> **B.Tech Capstone Project** | Android Security Application  
> Yatendra Dixit | CSE Cybersecurity | 2027

A full Android malware detection app with a 3-layer offline-first detection engine — no VirusTotal API needed, no internet required for core scanning.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────┐
│                  SUDARSHAN GUARD                     │
├─────────────────────────────────────────────────────┤
│  UI Layer                                           │
│  ├── SplashActivity      (boot screen)              │
│  ├── MainActivity        (dashboard + risk gauge)   │
│  ├── ScanActivity        (live scan progress)       │
│  ├── ReportActivity      (detailed threat report)   │
│  └── HistoryActivity     (all past scans)           │
├─────────────────────────────────────────────────────┤
│  Detection Engine (3 Layers)                        │
│  ├── Layer 1: HashEngine                            │
│  │   └── SHA-256 → MalwareHashDatabase (30+ hashes)│
│  ├── Layer 2: PermissionIntelligenceEngine          │
│  │   ├── 30+ dangerous permission profiles          │
│  │   └── Context Mismatch Detection                 │
│  │       (flashlight asking GPS = suspicious)       │
│  └── Layer 3: BehaviorPatternAnalyzer               │
│      ├── Installer origin verification              │
│      ├── Brand spoofing detection                   │
│      ├── Hidden app detection                       │
│      └── Suspicious component analysis              │
├─────────────────────────────────────────────────────┤
│  Background Services                               │
│  ├── InstallMonitorService  (foreground, persistent)│
│  ├── PackageInstallReceiver (auto-scan on install)  │
│  └── BootReceiver           (restart on boot)       │
├─────────────────────────────────────────────────────┤
│  Data Layer                                         │
│  └── Room Database (SQLite) — scan history          │
└─────────────────────────────────────────────────────┘
```

---

## 🔬 Detection Engine Details

### Layer 1 — Hash Engine (Weight: 50%)
- Computes **SHA-256** of every APK
- Matches against **MalwareHashDatabase** (offline, 30+ known malware families)
- Families covered: BankBot, Joker, Triada, Cerberus, BlackRock, AhMyth RAT, SpyMax RAT, FlixOnline, Hydra, EventBot, Ginp, Alien, and more
- If hash matches → **minimum risk floor of 80/100**

### Layer 2 — Permission Intelligence (Weight: 30%)
- **30+ dangerous permission profiles** with abuse scenarios
- **Context Mismatch Detection**: flags illogical combos
  - Flashlight + GPS = Suspicious
  - Game app + READ_SMS = Suspicious  
  - Any app + OVERLAY + ACCESSIBILITY = Banking Trojan signature (Critical)
- Danger scores per permission (1–10)

### Layer 3 — Behavior Pattern Analysis (Weight: 20%)
- **Installer origin**: Sideloaded (+20 risk), ADB (+15), Unknown source (+10)
- **Brand spoofing**: "SBI Mobile Banking" from unknown publisher = +30 risk
- **Typosquatting**: g00gle, facebok, whatsaap detection
- **Hidden app detection**: No launcher icon = hiding from user (+20)
- **Suspicious component names**: keylog, spy, monitor, stealer in service names

### Final Score Formula
```
Score = (Hash% × 0.50) + (Permission% × 0.30) + (Behavior% × 0.20)
        capped at 100, floored at 80 if hash match found
```

---

## 📱 Risk Levels

| Score  | Level    | Color  | Action              |
|--------|----------|--------|---------------------|
| 0–25   | SAFE     | 🟢 Green | No action needed  |
| 26–50  | LOW      | 🟡 Lime  | Monitor            |
| 51–75  | MEDIUM   | 🟠 Amber | Review permissions |
| 76–90  | HIGH     | 🔴 Red   | Uninstall advised  |
| 91–100 | CRITICAL | ⬛ Dark Red | Uninstall immediately |

---

## 🚀 Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17+
- Android SDK 34
- Physical device or emulator with Android 8.0+ (API 26+)

### Steps

```bash
# 1. Clone / open in Android Studio
git clone https://github.com/yatendradixit05/SudarshanMobileGuard

# 2. Sync Gradle
# File → Sync Project with Gradle Files

# 3. Build
# Build → Make Project (Ctrl+F9)

# 4. Run on device
# Run → Run 'app' (Shift+F10)
```

### First Run
1. Grant **notification permission** (Android 13+)
2. Tap **FULL DEVICE SCAN** to baseline all installed apps
3. The background service starts automatically and monitors new installs

---

## 🗂️ Project Structure

```
app/src/main/
├── java/com/sudarshan/mobileguard/
│   ├── SudarshanApplication.java       ← App class, pre-warms DB
│   ├── activities/
│   │   ├── SplashActivity.java
│   │   ├── MainActivity.java           ← Dashboard
│   │   ├── ScanActivity.java           ← Live scan + full device
│   │   ├── ReportActivity.java         ← Detailed report
│   │   └── HistoryActivity.java
│   ├── engine/
│   │   ├── ScanEngine.java             ← Master orchestrator
│   │   ├── HashEngine.java             ← Layer 1
│   │   ├── MalwareHashDatabase.java    ← Offline hash blacklist
│   │   ├── PermissionIntelligenceEngine.java  ← Layer 2
│   │   └── BehaviorPatternAnalyzer.java       ← Layer 3
│   ├── services/
│   │   ├── InstallMonitorService.java  ← Foreground guard service
│   │   ├── PackageInstallReceiver.java ← Auto-scan on install
│   │   └── BootReceiver.java
│   ├── database/
│   │   ├── AppDatabase.java            ← Room DB
│   │   └── ScanResultDao.java
│   ├── models/
│   │   ├── ScanResult.java             ← Main data model
│   │   └── PermissionProfile.java
│   └── adapters/
│       └── ScanResultAdapter.java
└── res/
    ├── layout/                         ← All screen layouts
    ├── values/colors.xml               ← Deep navy + amber palette
    ├── values/strings.xml
    ├── values/themes.xml               ← Material3 Dark theme
    └── drawable/                       ← Shield icons
```

---

## 🔮 Future Enhancements

| Feature | Implementation |
|---------|---------------|
| VirusTotal API | Add to HashEngine as optional cloud layer |
| Dynamic hash DB updates | Firebase Remote Config or CDN JSON |
| DEX analysis | Use dex2jar + custom byte pattern scanner |
| Network behavior monitoring | Android VPN API (no root needed) |
| Export PDF report | Use iText or Apache PDFBox |
| AV signature updates | Background WorkManager daily job |

---

## 📜 Legal & Ethics

This app only analyzes apps the user explicitly scans or that are installed on the user's own device. No data is sent to any server. The malware hash database contains publicly documented hashes from threat intelligence reports (MalwareBazaar, ESET, Kaspersky public blogs).

---

*Built with ❤️ for cybersecurity — Sudarshan Mobile Guard*
