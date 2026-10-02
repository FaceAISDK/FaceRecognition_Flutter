## 2.1.1
- Updated the Android FaceAISDK dependency to `2026.09.29` in the plugin and example.
- Added missing native demo runtime dependencies for Android release builds.
- Refined the example's result typography, feature card borders, and demo image.

## 2.1.0
- Added `compareFaceFeatures` for Android and iOS, with a bilingual demo.
- Validated feature length, unpadded Base64 payloads, and similarity scores; normalized standard and URL-safe Base64 for each native SDK.
- Serialized Android comparisons off the UI thread and added unit, widget, and native integration tests.
- Updated example build configuration for the current Flutter SDK and native integration tests.
- Corrected the package repository and issue links.

## 2.0.2
- Updated the Android and iOS FaceAISDK integrations to the 2026.09.22 release.
- Removed biometric data from native debug logs and tightened demo app settings.
- Refreshed the example UI, app icons, and English/Chinese documentation.

## 2.0.1
- Improved liveness detection sensitivity.
- Other minor updates and refinements.

## 2.0.0
- iOS: Made face size detection threshold more lenient for better user experience.
- Android: Redesigned and polished the "Add Face" dialog UI.

## 1.2.0
- Initial adaptation for iOS 27，Android 17
- Silent liveness threshold (iOS/Android): 0.85–0.95
- Reduce SDK size
- Brief Translation
- fix iOS duplicate symbols

## 1.0.0

### Added
- Offline face enrollment by SDK camera or Base64 image.
- 1:1 face verification with configurable liveness detection.
- Liveness-only detection, face feature query, insert, delete, and image export APIs.
- Android and iOS example app with English and Simplified Chinese UI.

### Changed
- Renamed the public package to `face_recognition_flutter` for pub.dev compatibility.
- Updated the iOS podspec name and version to match the Dart package.
- Improved README, release notes, and published package contents for the 1.0.0 release.

### Fixed
- Fixed iOS example source inclusion after CocoaPods regeneration.
- Fixed publish dry-run warnings for package name, README naming, and excluded generated files.

## 0.0.2

- Improved iOS performance and stability.
- Updated project structure and multilingual support.

## 0.0.1

- Initial release of the FaceAISDK Flutter plugin.
- Added platform view support for Android and iOS.
