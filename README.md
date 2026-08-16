# NovaVest 💎
### Premium Luxury Wealth & Asset Tracker

NovaVest is a high-end financial management application built with **Jetpack Compose** and **Material 3**. Designed for high-net-worth individuals, it combines sophisticated glassmorphic aesthetics with powerful analytics to provide a premium wealth tracking experience.

---

## ✨ Key Features

### 📊 Executive Dashboard
*   **Glassmorphic Balance Card**: A high-fidelity card showcasing total net worth with subtle blur effects and premium typography.
*   **Custom Canvas Donut Chart**: A bespoke, hardware-accelerated asset allocation visualization (Gold, Real Estate, Cash).
*   **Smooth Motion UI**: Transaction history featuring orchestrated slide-in animations for a fluid, polished feel.

### 📈 Wealth Analytics
*   **Canvas Line Chart**: Dynamic wealth growth visualization drawn directly on the Android Canvas for maximum performance and precision.
*   **Interactive Time-Filtering**: Quick-action chips (1M, 6M, 1Y) with smooth layout transitions and state-driven UI updates.
*   **Asset Performance Tracking**: Detailed breakdown of top-performing assets with real-time growth indicators.

---

## 🛠 Tech Stack & Architecture

The project follows **Clean Architecture** principles and the **MVVM** pattern to ensure scalability and maintainability.

-   **Language**: Kotlin
-   **UI Framework**: Jetpack Compose (100%)
-   **Design System**: Material 3 (Customized)
-   **Concurrency**: Kotlin Coroutines & StateFlow
-   **Drawing**: Compose Canvas API
-   **Architecture**: MVVM + Repository Pattern

---

## 📂 Project Structure

```text
app/src/main/java/com/uifinance/project291/
├── data/
│   ├── repository/        # Data implementation
│   └── model/             # DTOs and Data models
├── domain/
│   ├── usecase/           # Business logic
│   └── model/             # Domain entities
├── ui/
│   ├── dashboard/         # Dashboard screen & ViewModels
│   ├── analytics/         # Analytics screen & ViewModels
│   └── components/        # Reusable UI components (Charts, Cards)
└── design_system/
    ├── Color.kt           # Deep Obsidian, Emerald Green, Muted Gold
    ├── Type.kt            # Premium Bodoni Moda & Inter configurations
    └── Theme.kt           # Custom Material 3 Theme wrapper
```

---

## 🎨 Design System: Emerald Nocturne

NovaVest utilizes a bespoke "Luxury Dark" palette defined by high contrast and rich textures.

| Element | Color Name | Hex Code |
| :--- | :--- | :--- |
| **Surface** | Deep Obsidian | `#0B0E11` |
| **Primary** | Emerald Green | `#10B981` |
| **Accent** | Muted Gold | `#D4AF37` |
| **Text** | High-Emphasis | `#FFFFFF` |

---

## 💻 Code Highlights

### Custom Donut Chart (Canvas)
A snippet demonstrating the precision drawing of the asset allocation segments using the Compose Canvas API.

```kotlin
Canvas(modifier = Modifier.size(240.dp)) {
    val strokeWidth = 40f
    drawArc(
        color = EmeraldGreen,
        startAngle = 0f,
        sweepAngle = 162f, // 45%
        useCenter = false,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}
```

---

*Developed with precision for the next generation of wealth management.*
