# Logistics Tracker System – Android Customer App

A native Android application that allows customers to create, manage, and track logistics trips/orders in real time.

[![Android](https://img.shields.io/badge/Android-Java-3DDC84?style=flat&logo=android)](https://developer.android.com)
[![minSdk](https://img.shields.io/badge/minSdk-27-blue)](https://developer.android.com)

## Overview

**Logistics Tracker Customer App** is the customer-facing mobile application for the Logistics Tracker System. It enables users to:

- Register and log in
- Create new trips / delivery requests
- Set pickup and destination locations (with maps)
- View pending and active orders
- Track trip status and details
- Manage their profile

The app is designed to work with the companion **Logistics Tracker Dashboard** (Laravel) and driver/staff Android app.

**Package:** `com.scg.scgcustomerlogistics`  
**Backend API:** `https://logisticstrackerdemo.av.ke/api/`

## Key Features

- User authentication (Login & Signup)
- Create new trips with origin and destination
- Location selection using Google Maps & Places
- View list of trips and pending orders
- Trip details and tracking
- Profile management
- Real-time location services
- Secure local data storage
- Image loading and modern Material UI

## Tech Stack

| Component          | Technology                          |
|--------------------|-------------------------------------|
| Language           | Java                                |
| UI                 | AndroidX + Material Design          |
| Networking         | Retrofit 2 + OkHttp                 |
| Maps & Location    | Google Maps, Places, Play Services Location |
| Image Loading      | Picasso                             |
| Background Tasks   | WorkManager                         |
| Analytics          | Firebase Analytics + Crashlytics    |
| Security           | AndroidX Security Crypto            |
| Min SDK            | 27                                  |
| Target / Compile   | 35                                  |

## Prerequisites

- Android Studio (Ladybug or newer)
- JDK 11+
- Android SDK 35
- Google Maps API key (configured in the project)
- Git

## Getting Started

```bash
# Clone the repository
git clone https://github.com/JavisOtieno/Logistics-Tracker-System-Android-Customer-App.git
cd Logistics-Tracker-System-Android-Customer-App

# Open in Android Studio
# File → Open → select the project folder

# Sync Gradle
# Build → Make Project

# Run on emulator or physical device
