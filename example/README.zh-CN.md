# Flutter 示例

<p align="center">
  <a href="README.md">English</a> | <strong>简体中文</strong>
</p>

本应用展示如何从 Flutter 调用 `face_recognition_flutter`。原生相机界面和人脸处理由 SDK 提供。

## 运行

```bash
cd example
flutter pub get
flutter run
```

请使用带相机的 Android 或 iOS 真机。平台要求见[插件说明](../README.zh-CN.md)。Android 示例使用当前 Flutter SDK 的 `minSdkVersion`，可能高于插件要求的 API 21。iOS 真机运行前，请在 Xcode 中设置签名团队和 Bundle ID。

如需在真机上运行 Release 版本，执行 `flutter run --release`。

## 演示流程

1. 点击“SDK相机录入人脸信息”录入示例 ID `yourFaceID`；接入自己的应用时请更换该 ID。
2. 尝试“人脸识别+活体检测”“检测人脸是否活体”或“查询人脸特征信息”。
3. 测试特征恢复时，先查询特征，再删除，最后点击“同步人脸特征信息”。示例仅在本次运行期间缓存该 1024 字符特征。
4. 测试“人脸图录入人脸信息”前，先将 Base64 编码的图片复制到设备剪贴板。

结果面板显示 SDK 状态码和消息，人脸特征及图片 Base64 仅显示简短预览。
