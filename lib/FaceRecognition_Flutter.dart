import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'face_aisdk_result.dart';

export 'face_aisdk_view.dart';
export 'face_aisdk_result.dart';

class FaceAiSdkFlutterPlugin {
  FaceAiSdkFlutterPlugin._();

  static const MethodChannel _channel = MethodChannel('FaceRecognition_Flutter');

  /// 1. 摄像头采集人脸特征并保存
  /// [faceId] 用户唯一标识
  /// [addFacePerformanceMode] 采集模式：1: 快速模式, 2: 精确模式(人脸品质高)
  /// [needShowConfirmDialog] 是否显示确认弹窗，强烈建议设置为 true
  static Future<FaceAiSdkResult> addFaceBySDKCamera({
    required String faceId,
    int addFacePerformanceMode = 1,
    bool needShowConfirmDialog = true,
  }) async {
    final Map? result = await _channel.invokeMethod('addFaceBySDKCamera', {
      'faceId': faceId,
      'addFacePerformanceMode': addFacePerformanceMode,
      'needShowConfirmDialog': needShowConfirmDialog,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('addFaceBySDKCamera', finalResult);
    return finalResult;
  }

  /// 2. 人脸识别 (1:1) + 活体检测
  /// [faceId] 待比对的用户ID
  /// [threshold] 相似度阈值 [0.75, 0.95]，默认 0.84
  /// [livenessType] 活体类型：1: 动作活体, 2: 动作+炫彩活体, 3: 炫彩活体, 4: 静默活体
  /// [motionLivenessTypes] 动作类型用英文","隔开：1.张嘴 2.微笑 3.眨眼 4.摇头 5.点头
  /// [motionLivenessTimeOut] 动作超时时间 [3, 10]秒，默认 7
  /// [motionLivenessSteps] 需要完成的动作步数，1 或 2 个
  /// [allowMultiFaces] 是否允许多张人脸入镜 (仅 Android)
  static Future<FaceAiSdkResult> faceVerify({
    required String faceId,
    double threshold = 0.84,
    int livenessType = 1,
    String motionLivenessTypes = "1,2,3,4,5",
    int motionLivenessTimeOut = 7,
    int motionLivenessSteps = 2,
    bool allowMultiFaces = false,
  }) async {
    final Map? result = await _channel.invokeMethod('faceVerify', {
      'faceId': faceId,
      'threshold': threshold,
      'livenessType': livenessType,
      'motionLivenessTypes': motionLivenessTypes,
      'motionLivenessTimeOut': motionLivenessTimeOut,
      'motionLivenessSteps': motionLivenessSteps,
      'allowMultiFaces': allowMultiFaces,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('faceVerify', finalResult);
    return finalResult;
  }

  /// 3. 活体检测
  /// [livenessType] 1.动作活体 2.动作+炫彩活体 3.炫彩活体 4.静默活体
  /// [motionLivenessTypes] 动作类型：1.张嘴 2.微笑 3.眨眼 4.摇头 5.点头
  /// [motionLivenessTimeOut] 超时时间
  /// [motionLivenessSteps] 动作步数
  /// [allowMultiFaces] 是否允许多人脸 (仅 Android)
  static Future<FaceAiSdkResult> livenessVerify({
    int livenessType = 2,
    String motionLivenessTypes = "1,2,3,4,5",
    int motionLivenessTimeOut = 7,
    int motionLivenessSteps = 2,
    bool allowMultiFaces = true,
    bool showResultTips = true,
  }) async {
    final Map? result = await _channel.invokeMethod('livenessVerify', {
      'livenessType': livenessType,
      'motionLivenessTypes': motionLivenessTypes,
      'motionLivenessTimeOut': motionLivenessTimeOut,
      'motionLivenessSteps': motionLivenessSteps,
      'allowMultiFaces': allowMultiFaces,
      'showResultTips': showResultTips,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('livenessVerify', finalResult);
    return finalResult;
  }

  /// 4. 检测本地是否有 faceID 对应的人脸特征值
  static Future<FaceAiSdkResult> getFaceFeature(String faceId) async {
    final Map? result = await _channel.invokeMethod('getFaceFeature', {
      'faceId': faceId,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('getFaceFeature', finalResult);
    return finalResult;
  }

  /// 5. 同步/插入人脸特征值
  static Future<FaceAiSdkResult> insertFaceFeature({
    required String faceId,
    required String feature,
  }) async {
    final Map? result = await _channel.invokeMethod('insertFaceFeature', {
      'faceId': faceId,
      'feature': feature,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('insertFaceFeature', finalResult);
    return finalResult;
  }

  /// 6. 人脸图录入人脸信息
  static Future<FaceAiSdkResult> addFaceBySDKImage({
    required String faceId,
    required String imageBase64,
  }) async {
    final Map? result = await _channel.invokeMethod('addFaceBySDKImage', {
      'faceId': faceId,
      'imageBase64': imageBase64,
    });
    final finalResult = FaceAiSdkResult.fromMap(result ?? {});
    _printResult('addFaceBySDKImage', finalResult);
    return finalResult;
  }

  /// 删除人脸特征
  static Future<void> deleteFaceFeature(String faceId) async {
    await _channel.invokeMethod('deleteFaceFeature', {
      'faceId': faceId,
    });
  }

  /// 检查人脸是否存在
  static Future<bool> isFaceExist(String faceId) async {
    final dynamic result = await _channel.invokeMethod('isFaceExist', {
      'faceId': faceId,
    });
    if (result is bool) return result;
    if (result is Map) return result['code'] == 1;
    return false;
  }

  /// 获取人脸图片 Base64
  static Future<String?> getFaceImageBase64(String faceId) async {
    final String? base64 = await _channel.invokeMethod('getFaceImageBase64', {
      'faceId': faceId,
    });
    return base64;
  }

  /// 切换摄像头 (仅 Android)
  static Future<void> switchCamera(int cameraId) async {
    await _channel.invokeMethod('switchCamera', {'cameraId': cameraId});
  }

  /// 跳转原生 Demo 导航页
  static Future<void> goNativeDemoNavi() async {
    await _channel.invokeMethod('goNativeDemoNavi');
  }

  static void _printResult(String method, FaceAiSdkResult result) {
    if (kDebugMode) {
      print('FaceAiSdkPlugin $method result: $result');
      if (result.faceBase64 != null && result.faceBase64!.length > 50) {
        print('  faceBase64: ${result.faceBase64!.substring(0, 20)}...');
      }
    }
  }
}

class FaceAiSdkResultCode {
  static const int cancel = 0;
  static const int verifySuccess = 1;
  static const int verifyFailed = 2;
  static const int motionLivenessSuccess = 3;
  static const int motionLivenessTimeout = 4;
  static const int noFaceMulti = 5;
  static const int noFaceFeature = 6;
  static const int colorLivenessSuccess = 7;
  static const int colorLivenessFailed = 8;
  static const int colorLivenessLightTooHigh = 9;
  static const int allLivenessSuccess = 10;
  static const int silentLivenessFailed = 11;
  static const int noBaseFaceFeature = 12;
  static const int notAllowMultiFaces = 13;
}
