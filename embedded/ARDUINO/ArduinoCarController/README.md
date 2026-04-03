# Arduino Car Controller

A complete Android application for controlling an Arduino-based WiFi car remotely using Jetpack Compose and MVVM architecture.

## Features

- **Real-time WiFi Control**: Connect to Arduino car via TCP socket communication
- **Intuitive UI Controls**:
  - Circular steering wheel with touch gesture support
  - Acceleration and brake pedals with vertical drag control
  - Real-time feedback of control values
- **MVVM Architecture**: Clean separation of concerns with ViewModel, Repository, and Use Cases
- **Coroutine-based Async**: Non-blocking async operations for network communication
- **Hilt Dependency Injection**: Modern DI framework for clean code
- **Jetpack Compose UI**: Modern declarative UI framework

## Technology Stack

### Latest Versions (as of 2024)
- **Kotlin**: 1.9.21
- **Gradle**: 8.5
- **Jetpack Compose**: January 2024 BOM
- **Android SDK**: Target 34, Min 24

### Key Libraries
- **AndroidX Core**: 1.12.0
- **Lifecycle**: 2.7.0
- **Jetpack Compose**: 2024.01.00
- **Navigation Compose**: 2.7.6
- **Hilt**: 2.48
- **Coroutines**: 1.7.3
- **Ktor Client**: 2.3.7
- **Kotlinx Serialization**: 1.6.2

## Project Structure

```
ArduinoCarController/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/example/arduinocar/
│   │   │   │   ├── data/
│   │   │   │   │   ├── network/        # Socket communication
│   │   │   │   │   └── repository/     # Data layer implementation
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/          # Data models
│   │   │   │   │   ├── repository/     # Repository interfaces
│   │   │   │   │   └── usecase/        # Business logic
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screen/         # Main screens
│   │   │   │   │   └── components/     # Reusable UI components
│   │   │   │   ├── viewmodel/          # MVVM ViewModels
│   │   │   │   ├── di/                 # Dependency injection
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── ArduinoCarApplication.kt
│   │   │   └── AndroidManifest.xml
│   │   └── res/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Architecture

### MVVM (Model-View-ViewModel)
- **Model**: Domain models (`CarState`, `CarCommand`, etc.)
- **View**: Jetpack Compose UI (`CarControlScreen`, UI components)
- **ViewModel**: `CarControlViewModel` manages UI state and orchestrates business logic

### Clean Architecture Layers

**Presentation Layer**
- `MainActivity`: Activity entry point
- `CarControlScreen`: Main UI screen
- `ControlComponents`: Reusable UI components
- `CarControlViewModel`: Manages UI state and handles user interactions

**Domain Layer**
- Models: `CarState`, `CarCommand`, `ConnectionState`
- Repository Interface: `CarControlRepository`
- Use Cases: `SendCommandUseCase`, `ConnectUseCase`, `DisconnectUseCase`

**Data Layer**
- `ArduinoSocketClient`: Handles WiFi socket communication
- `CarControlRepositoryImpl`: Implements repository pattern

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android SDK API 34

### Build & Run

```bash
cd ArduinoCarController
./gradlew build

# Run on emulator or connected device
./gradlew installDebug
./gradlew installRelease
```

### Connecting to Arduino

1. **Configure Server**: 
   - Default IP: `192.168.1.100`
   - Default Port: `8888`
   - Change as needed in the app

2. **Arduino Sketch**: Expected to listen for JSON commands:
   ```json
   {
     "acceleration": 50,
     "brake": 0,
     "steeringAngle": 45,
     "timestamp": 1234567890
   }
   ```

## UI Components

### SteeringWheel
- Circular control with drag-based angle input
- Range: -90° to +90°
- Real-time angle display

### PedalControl
- Vertical pedal with drag input
- Acceleration: Green color, Range: 0-100%
- Brake: Red color, Range: 0-100%

### ConnectionStatusCard
- Shows current connection state
- Visual feedback (green/red/orange)
- Loading indicator during connection

## Usage Examples

### Connect to Car
```kotlin
viewModel.setServerHost("192.168.1.100")
viewModel.setServerPort("8888")
viewModel.connect()
```

### Send Command
```kotlin
viewModel.setAcceleration(50)  // 50%
viewModel.setBrake(0)           // 0%
viewModel.setSteeringAngle(45)  // 45 degrees right
```

### Disconnect
```kotlin
viewModel.disconnect()
```
