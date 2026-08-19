# Implementation Plan - Fix Onboarding Persistence

This plan addresses the issue where the onboarding screen is shown every time the app starts, even if previously completed.

## User Review Required

> [!IMPORTANT]
> The app will now wait for the `AppPreferences` to load before displaying the main navigation host. This ensures that we correctly determine whether to show the Onboarding screen or the Dashboard on startup.

## Proposed Changes

### Onboarding Feature

#### [NEW] [OnboardingViewModel.kt](file:///Users/admin/Documents/uifinance/app/src/main/java/com/uifinance/project291/ui/onboarding/OnboardingViewModel.kt)
- Create a ViewModel for the onboarding screen.
- Inject `AppPreferences`.
- Provide a `completeOnboarding()` function that calls `appPreferences.setOnboardingCompleted(true)`.

#### [MODIFY] [OnboardingScreen.kt](file:///Users/admin/Documents/uifinance/app/src/main/java/com/uifinance/project291/ui/onboarding/OnboardingScreen.kt)
- Update `OnboardingScreen` to take `OnboardingViewModel` (defaulting to `hiltViewModel()`).
- Call `viewModel.completeOnboarding()` inside the `onFinish` block before triggering the navigation callback.

### Navigation Logic

#### [MODIFY] [NovaVestApp.kt](file:///Users/admin/Documents/uifinance/app/src/main/java/com/uifinance/project291/NovaVestApp.kt)
- Inject `AppPreferences` into `NovaVestApp` (or pass it from `MainActivity`).
- Collect `onboardingCompleted` as state with lifecycle.
- While `onboardingCompleted` is `null` (loading), show a background-colored box to avoid UI flicker.
- Set `startDestination` of `NavHost` to `VaultDestination` if `onboardingCompleted` is `true`, otherwise `OnboardingDestination`.

## Verification Plan

### Automated Tests
- Run `:app:assembleDebug` to verify compilation.

### Manual Verification
- Deploy to device.
- Complete onboarding.
- Close the app (kill it from recents).
- Open the app again and verify it starts on the Dashboard (Vault) screen.
