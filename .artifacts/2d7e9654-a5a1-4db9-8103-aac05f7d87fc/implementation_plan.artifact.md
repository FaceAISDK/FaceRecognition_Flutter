# 方案：配置 Android Studio 以运行 Flutter 项目到设备

用户反映在 Android Studio 中无法选择设备运行 Flutter 项目，即使已开启开发者模式。由于这是一个 Flutter 插件项目，通常需要运行 `example` 目录下的示例应用。

## 用户审核事项

- 请确认您的 Android 设备已通过 USB 连接到电脑，且已允许 **USB 调试**。
- 请确认 Android Studio 已安装 **Flutter** 和 **Dart** 插件。

## 拟议变更

### 配置与环境

#### [NEW] [main_dart.xml](file:///Users/anylife/StudioProjects/FaceRecognition_Flutter/.idea/runConfigurations/main_dart.xml)
创建一个标准的 Android Studio 运行配置，直接指向 `example/lib/main.dart`。这样用户在 IDE 顶部的运行按钮旁就能直接看到并选择该配置。

#### [MODIFY] [ReadMe.md](file:///Users/anylife/StudioProjects/FaceRecognition_Flutter/ReadMe.md) (可选)
如果需要，可以在 ReadMe 中添加如何运行示例项目的说明。

## 验证计划

### 自动化测试
- 使用 `flutter devices` 确认设备已连接（已验证：设备 `SM S9210` 在线）。
- 检查 `example/lib/main.dart` 是否存在且无语法错误（已验证）。

### 手动验证
- 用户需要在 Android Studio 顶部的运行配置下拉菜单中选择 "example/lib/main.dart"。
- 用户需要在设备选择器中选择已连接的设备（SM S9210）。
- 点击运行按钮进行部署。
