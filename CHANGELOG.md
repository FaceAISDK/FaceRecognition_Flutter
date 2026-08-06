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
