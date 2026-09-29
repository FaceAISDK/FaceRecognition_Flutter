# Flutter example

<p align="center">
  <strong>English</strong> | <a href="README.zh-CN.md">简体中文</a>
</p>

This app shows how to call `face_recognition_flutter` from Flutter. The SDK provides the native camera screens and face processing.

## Run

```bash
cd example
flutter pub get
flutter run
```

Use a physical Android or iOS device with a camera. See the [plugin README](../README.md) for platform requirements. The Android example uses your Flutter SDK's `minSdkVersion`, which may exceed the plugin's API 21 minimum. For iOS, set your signing team and bundle ID in Xcode.

To run a release build on a physical device, use `flutter run --release`.

## Demo flow

1. Use **Camera Enrollment** to enroll `yourFaceID`. Replace this sample ID in your own app.
2. Try **Face Verification**, **Liveness Detection**, or **Retrieve Face Feature**.
3. To test feature restoration, retrieve the feature, delete it, then tap **Restore Face Feature**. The demo keeps the 1024-character feature in memory for this session.
4. To test **Image Enrollment**, copy a Base64-encoded image to the device clipboard.

The result panel shows SDK codes and messages, with shortened previews of features and image Base64.
