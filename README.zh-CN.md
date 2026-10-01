# face_recognition_flutter

<p align="center">
  <a href="README.md">English</a> | <strong>简体中文</strong>
</p>

[![pub package](https://img.shields.io/pub/v/face_recognition_flutter.svg)](https://pub.dev/packages/face_recognition_flutter)
[![platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS-blue)](#平台支持)

FaceAISDK Flutter 离线人脸识别与活体检测插件，支持 Android 和 iOS 上的人脸录入、1:1 核验、本地特征管理和原生相机界面。

![FaceAISDK Flutter 演示](FaceAISDK.png)

## 功能特性

- 无需联网，在设备端完成人脸处理。
- 通过 SDK 相机或 Base64 图片录入人脸。
- 支持自定义相似度阈值的 1:1 人脸核验。
- 支持动作、动作 + 炫彩、炫彩和静默活体检测。
- 支持本地人脸特征查询、插入、删除、存在性检查和图片导出。
- 无需打开相机即可比较两个由 SDK 生成的人脸特征的相似度。
- 提供原生相机 UI 和可嵌入 Flutter 布局的平台视图。
- 原生 UI 内置英文和简体中文资源。

## 平台支持

| 平台 | 最低版本 | 其他要求 |
| --- | --- | --- |
| Android | API 21 | `compileSdk` 34 或更高版本；Java 17 |
| iOS | 15.5 | CocoaPods；Swift 5.9 |

> 当前暂不支持 Swift Package Manager，iOS 请使用 CocoaPods 集成。

## 安装

```bash
flutter pub add face_recognition_flutter
```

### Android

在 `android/app/src/main/AndroidManifest.xml` 中添加相机权限：

```xml
<uses-permission android:name="android.permission.CAMERA" />
```

确保应用使用 Android API 21 或更高版本，并启用 Java 17：

```kotlin
android {
    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
```

### iOS

在 `ios/Podfile` 中设置最低部署版本：

```ruby
platform :ios, '15.5'
```

在 `Runner` target 内添加 FaceAISDK Core 源。这里的标签必须与插件 podspec 要求的版本一致：

```ruby
target 'Runner' do
  use_frameworks!

  flutter_install_all_ios_pods File.dirname(File.realpath(__FILE__))

  pod 'FaceAISDK_Core',
      :git => 'https://github.com/FaceAISDK/FaceAISDK_Core.git',
      :tag => '2026.09.22'
end
```

在 `ios/Runner/Info.plist` 中添加相机用途说明：

```xml
<key>NSCameraUsageDescription</key>
<string>FaceAISDK 需要使用相机完成人脸录入和活体核验。</string>
```

请按应用支持的语言本地化这段权限说明。

安装 Pods：

```bash
cd ios
pod install
```

## 快速开始

### 1. 录入人脸

```dart
import 'package:face_recognition_flutter/face_recognition_flutter.dart';

final enrollment = await FaceRecognitionFlutter.addFaceBySDKCamera(
  faceId: 'user_001',
);

if (!enrollment.isSuccess) {
  print('录入失败：${enrollment.message}');
}
```

### 2. 核验已录入的人脸

```dart
final result = await FaceRecognitionFlutter.faceVerify(
  faceId: 'user_001',
);

if (result.isSuccess) {
  print('核验成功，相似度：${result.similarity}');
} else {
  print('核验失败：${result.message}');
}
```

## 活体检测

### 活体模式

| 值   | 模式        | 说明                 |
|-----|-----------|--------------------|
| `0` | 无活体       | -                  |
| `1` | 动作活体      | 完成一个或多个指定人脸动作      |
| `2` | 动作 + 炫彩活体 | 组合动作与屏幕炫彩活体检测      |
| `3` | 炫彩活体      | 使用屏幕颜色变化检测，请避免强光环境 |
| `4` | 静默活体      | 无需用户执行动作的无感活体检测    |

### 动作类型

使用英文逗号分隔动作值，例如 `"1,2,3,4,5"`。

| 值 | 动作 |
| --- | --- |
| `1` | 张嘴 |
| `2` | 微笑 |
| `3` | 眨眼 |
| `4` | 摇头 |
| `5` | 点头 |

请在目标设备和部署环境中验证阈值及活体检测效果。

仅执行活体检测，不进行 1:1 人脸比对：

```dart
final result = await FaceRecognitionFlutter.livenessVerify(
  livenessType: 4,
);
```

## API 参考

所有方法均为异步调用。可选参数及平台差异请参阅 Dart API 注释。

| API | 说明 | 结果 |
| --- | --- | --- |
| `addFaceBySDKCamera` | 使用原生 SDK 相机录入人脸 | `FaceRecognitionResult` |
| `addFaceBySDKImage` | 从 Base64 图片录入人脸 | `FaceRecognitionResult` |
| `faceVerify` | 执行 1:1 人脸核验和活体检测 | `FaceRecognitionResult` |
| `livenessVerify` | 仅执行活体检测 | `FaceRecognitionResult` |
| `getFaceFeature` | 获取本地保存的人脸特征 | `FaceRecognitionResult` |
| `insertFaceFeature` | 插入或同步人脸特征 | `FaceRecognitionResult` |
| `compareFaceFeatures` | 比较两个 1024 字符的人脸特征 | `FaceRecognitionResult` |
| `deleteFaceFeature` | 删除本地人脸特征 | `void` |
| `isFaceExist` | 检查本地是否存在指定人脸 ID | `bool` |
| `getFaceImageBase64` | 将已保存的人脸图片导出为 Base64 | `String?` |
| `switchCamera` | 在 Android 上切换摄像头 | `void` |
| `goNativeDemoNavi` | 打开 FaceAISDK 原生演示页面 | `void` |

### 从图片录入

```dart
final result = await FaceRecognitionFlutter.addFaceBySDKImage(
  faceId: 'user_001',
  imageBase64: imageBase64,
);
```

### 管理人脸特征

```dart
final featureResult = await FaceRecognitionFlutter.getFaceFeature('user_001');
final feature = featureResult.faceFeature;
if (feature != null) {
  await FaceRecognitionFlutter.insertFaceFeature(
    faceId: 'user_002',
    feature: feature,
  );
}

final exists = await FaceRecognitionFlutter.isFaceExist('user_002');
final image = await FaceRecognitionFlutter.getFaceImageBase64('user_001');

await FaceRecognitionFlutter.deleteFaceFeature('user_002');
```

插入特征不会创建人脸图片，因此上面的图片查询使用通过相机录入的 ID。

### 比较人脸特征

```dart
final comparison = await FaceRecognitionFlutter.compareFaceFeatures(
  feature1: firstFeature,
  feature2: secondFeature,
);
if (comparison.isSuccess) {
  print('Similarity: ${comparison.similarity}');
} else {
  print(comparison.message);
}
```

请传入 SDK 生成的两个 1024 字符、无填充 Base64 特征，支持标准及 URL-safe
字符集。校验仅检查格式。`isSuccess` 表示比较计算成功，是否属于同一人需结合
原始相似度分数和业务阈值判断。

## 嵌入原生视图

需要将原生相机视图嵌入 Flutter 布局时，可以使用 `FaceRecognitionView`：

```dart
FaceRecognitionView(
  creationParams: const <String, dynamic>{
    'needShowConfirmDialog': true,
  },
  onViewCreated: (controller) async {
    await controller.startScan();
  },
)
```

控制器提供 `startScan()` 和 `stopScan()` 方法。

## 返回结果

`FaceRecognitionResult` 包含以下字段：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `code` | `int` | 操作结果码 |
| `message` | `String?` | 原生状态或错误信息 |
| `similarity` | `double?` | `0.0` 到 `1.0` 的人脸相似度 |
| `livenessValue` | `double?` | 活体检测分数 |
| `faceBase64` | `String?` | Base64 编码的已采集人脸图片 |
| `faceFeature` | `String?` | 提取的人脸特征字符串 |
| `isSuccess` | `bool` | 结果码为 `1`、`3` 或 `10` 时为真 |

## 结果状态码

| 状态码 | 常量 | 说明 |
| --- | --- | --- |
| `0` | `cancel` | 初始化或取消状态 |
| `1` | `verifySuccess` | 1:1 人脸核验通过 |
| `2` | `verifyFailed` | 1:1 人脸核验失败 |
| `3` | `motionLivenessSuccess` | 动作活体检测通过 |
| `4` | `motionLivenessTimeout` | 动作活体检测超时 |
| `5` | `noFaceMulti` | 连续多次未成功检测到人脸 |
| `6` | `noFaceFeature` | 未检测到或无法提取有效人脸特征 |
| `7` | `colorLivenessSuccess` | 炫彩活体检测通过 |
| `8` | `colorLivenessFailed` | 炫彩活体检测失败 |
| `9` | `colorLivenessLightTooHigh` | 环境光线过强，炫彩活体检测失败 |
| `10` | `allLivenessSuccess` | 所有已配置的活体检测均通过 |
| `11` | `silentLivenessFailed` | 静默活体检测失败 |
| `12` | `noBaseFaceFeature` | 本地不存在已录入的基准人脸特征 |
| `13` | `notAllowMultiFaces` | 不允许多人脸时检测到多张人脸 |

## 运行示例

```bash
cd example
flutter pub get
flutter run
```

指定设备运行：

```bash
flutter devices
flutter run -d <device-id>
```

在 `example` 目录运行真机 Release 版本，可使用 `flutter run --release`。

## 常见问题

### `Target file "lib/main.dart" not found`

请运行示例应用，而不是在插件包根目录直接运行：

```bash
cd example
flutter run
```

### CocoaPods 提示 `FaceAISDK_Core` 版本不兼容

确认应用 `Podfile` 中显式声明的 `FaceAISDK_Core` 标签与 `ios/face_recognition_flutter.podspec` 要求的版本一致，然后执行：

```bash
cd ios
pod update FaceAISDK_Core
```

### iOS 模拟器架构警告

部分 MLKit 和 TensorFlow Lite 间接依赖可能不包含所有模拟器架构，请使用 iOS 真机进行最终验证。

### Android Studio 找不到已连接设备

先确认 `flutter devices` 能够识别设备。Android 设备必要时可重启 adb：

```bash
adb kill-server
adb start-server
```

## 隐私说明

人脸识别和活体检测均在设备本地运行。应用仍有责任获得用户授权，并妥善保护其存储、传输或同步的人脸图片及生物特征数据。

## 相关 SDK

- [FaceAISDK iOS](https://github.com/FaceAISDK/FaceAISDK_iOS)
- [FaceAISDK Android](https://github.com/FaceAISDK/FaceAISDK_Android)
- [FaceAISDK Flutter](https://github.com/FaceAISDK/FaceRecognition_Flutter)
- [FaceAISDK React Native](https://github.com/FaceAISDK/FaceRecognition_ReactNative)

版本历史请参阅 [CHANGELOG.md](CHANGELOG.md)。
