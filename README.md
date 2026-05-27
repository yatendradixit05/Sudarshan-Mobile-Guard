<div align="center">

<img src="app/src/main/res/drawable/ic_shield_active.xml" width="80" height="80">

# 🛡️ Sudarshan Mobile Guard

**A powerful offline-first Android malware detection app**  
*3-Layer Security Engine | No Internet Required | Real-time Protection*

![Android](https://img.shields.io/badge/Android-8.0%2B-green?style=flat-square&logo=android)
![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=flat-square)

---

> *"Jaise Sudarshan Chakra har direction se protect karta hai — yeh app aapke phone ko 360° security deta hai"*

</div>

---

## 📱 Screenshots

| Dashboard | Scanning | Report | History |
|-----------|----------|--------|---------|
| ![Dashboard](screenshots/dashboard.jpeg) | ![Scanning](screenshots/scanning.jpeg) | ![Report](screenshots/report.jpeg) | ![History](screenshots/history.jpeg) |

---

## 🎯 Problem Statement

Aaj India mein:
- 📊 **67%** Android users unknown sources se APKs install karte hain
- 💸 Banking trojans har saal **₹1000+ crore** ka fraud karte hain
- 📵 Existing antivirus apps **internet-dependent** hain
- 🔍 Users ko pata nahi hota koi app **silently kya kar rahi hai**

**Sudarshan Mobile Guard** iska solution hai — completely **offline**, completely **transparent**.

---

## 🔬 3-Layer Detection Engine
### Layer 1 — SHA-256 Hash Engine
- Har APK ka **SHA-256 cryptographic fingerprint** nikalta hai
- **30+ documented malware families** ke against match karta hai
- Covered families: `BankBot` `Joker` `Triada` `Cerberus` `BlackRock` `AhMyth RAT` `SpyMax` `Hydra` `EventBot` `Ginp` `Alien` `DroidJack` `OmniRAT` and more
- Hash match = **minimum 80/100 risk score**

### Layer 2 — Permission Intelligence Engine
- **30+ dangerous permissions** profiled with abuse scenarios
- **Context Mismatch Detection** — illogical combos flag karta hai:

| App Type | Permission | Status |
|----------|-----------|--------|
| Flashlight | GPS Location | 🚨 Suspicious |
| Calculator | Read SMS | 🚨 Suspicious |
| Game | Send SMS | 🚨 Premium Fraud Risk |
| Any App | Overlay + Accessibility | 🔴 Banking Trojan Signature |

### Layer 3 — Behavior Pattern Analyzer
- **Installer origin** — Sideloaded apps auto-flagged
- **Brand spoofing** — Fake SBI/PayTM/Google apps detect karta hai
- **Typosquatting** — `g00gle`, `facebok`, `whatsaap` patterns
- **Hidden apps** — No launcher icon = hiding from user
- **Suspicious components** — keylog, spy, monitor named services

---

## 🚀 Features

| Feature | Description |
|---------|-------------|
| 📱 Full Device Scan | Saare installed user apps ek saath scan |
| 📂 APK File Scanner | Install karne se pehle APK check karo |
| 🔄 Auto-scan | Nayi app install hote hi automatic scan |
| 📴 Offline-First | Core scanning mein zero internet required |
| 📊 Risk Score | 0-100 real-time device health gauge |
| 📋 Detailed Reports | SHA-256, permissions, behavior findings |
| 🕐 Scan History | Saare past scans saved in local DB |
| 🔔 Threat Alerts | Instant notification on threat detection |
| 🔁 Boot Protection | Device restart pe bhi guard active |

---

## 📊 Risk Level System

| Score | Level | Badge | Action |
|-------|-------|-------|--------|
| 0–15 | SAFE | 🟢 | No action needed |
| 16–35 | LOW | 🟡 | Monitor the app |
| 36–60 | MEDIUM | 🟠 | Review permissions carefully |
| 61–80 | HIGH | 🔴 | Uninstall advised |
| 81–100 | CRITICAL | ⬛ | Uninstall immediately |

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 17 |
| Min SDK | Android 8.0 (API 26) |
| Target SDK | Android 14 (API 34) |
| Database | Room (SQLite) |
| UI Framework | Material3 Dark Theme |
| Architecture | Modular Security Architecture |
| Hash Algorithm | SHA-256 (MessageDigest) |
| Background | Foreground Service + BroadcastReceiver |

---

## 📂 Project Structure
---

## ⚙️ Installation & Setup

```bash
# 1. Clone the repository
git clone https://github.com/yatendradixit05/Sudarshan-Mobile-Guard.git

# 2. Open in Android Studio
# File → Open → Select project folder

# 3. Sync Gradle
# File → Sync Project with Gradle Files

# 4. Run on device (Android 8.0+)
# Enable USB Debugging → Connect phone → Press Shift+F10
```

**Requirements:**
- Android Studio Hedgehog or newer
- JDK 17+
- Android device with API 26+ (Android 8.0)

---

## 🔮 Future Roadmap

- [ ] VirusTotal API integration (optional cloud layer)
- [ ] Automatic hash DB updates via GitHub
- [ ] DEX bytecode pattern analysis
- [ ] Network behavior monitoring (VPN API)
- [ ] PDF report export
- [ ] Hindi language support
- [ ] Widget for home screen risk score

---

## 🛡️ Malware Families Covered

| Family | Type | Severity |
|--------|------|----------|
| BankBot | Banking Trojan | 🔴 95/100 |
| Joker | SMS Fraud Spyware | 🔴 90/100 |
| Cerberus | Banking Trojan | 🔴 97/100 |
| Triada | Pre-installed Backdoor | 🔴 99/100 |
| AhMyth | Remote Access Trojan | 🔴 97/100 |
| BlackRock | Banking Trojan | 🔴 94/100 |
| Hydra | Banking Trojan | 🔴 93/100 |
| DroidJack | RAT | 🔴 98/100 |
| Simplocker | Ransomware | 🔴 99/100 |
| HiddenAds | Adware | 🟠 60/100 |

---

## 👨‍💻 Developer

**Yatendra Dixit**
B.Tech CSE — Cybersecurity Specialization

[![GitHub](https://img.shields.io/badge/GitHub-yatendradixit05-black?style=flat-square&logo=github)](https://github.com/yatendradixit05)
[![Email](https://img.shields.io/badge/Email-yatendradixit05%40gmail.com-red?style=flat-square&logo=gmail)](mailto:yatendradixit05@gmail.com)

---

## 📜 Disclaimer

This app analyzes apps on the user's own device only. No data is sent to any external server. Malware hashes are sourced from public threat intelligence databases (MalwareBazaar, ESET, Kaspersky public reports).

---

<div align="center">

**⭐ Agar project helpful laga toh star do!**

*Built with ❤️ for cybersecurity — Sudarshan Mobile Guard*

</div>