import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'face_recognition_result.dart';

export 'face_recognition_view.dart';
export 'face_recognition_result.dart';

class FaceRecognitionFlutter {
  FaceRecognitionFlutter._();

  static const MethodChannel _channel =
      MethodChannel('FaceRecognition_Flutter');

  /// Enrolls [faceId] with the native camera.
  ///
  /// [addFacePerformanceMode] selects fast (1) or quality (2) capture on iOS;
  /// Android currently ignores it. [needShowConfirmDialog] shows a review step.
  static Future<FaceRecognitionResult> addFaceBySDKCamera({
    required String faceId,
    int addFacePerformanceMode = 1,
    bool needShowConfirmDialog = true,
  }) async {
    final Map? result = await _channel.invokeMethod('addFaceBySDKCamera', {
      'faceId': faceId,
      'addFacePerformanceMode': addFacePerformanceMode,
      'needShowConfirmDialog': needShowConfirmDialog,
    });
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('addFaceBySDKCamera', finalResult);
    return finalResult;
  }

  /// Verifies [faceId] against a new capture with liveness detection.
  ///
  /// [threshold] defaults to 0.84. [livenessType] selects motion (1),
  /// motion + color (2), color (3), or silent (4) detection.
  /// [motionLivenessTypes] is a comma-separated list: open mouth (1), smile
  /// (2), blink (3), shake head (4), or nod (5).
  /// [motionLivenessTimeOut] is in seconds; [motionLivenessSteps] is the number
  /// of actions. [allowMultiFaces] applies only on Android.
  static Future<FaceRecognitionResult> faceVerify({
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
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('faceVerify', finalResult);
    return finalResult;
  }

  /// Runs liveness detection without matching an enrolled face.
  ///
  /// Liveness options match [faceVerify]. [allowMultiFaces] applies only on
  /// Android. [showResultTips] is currently ignored by the native SDK screens.
  static Future<FaceRecognitionResult> livenessVerify({
    int livenessType = 1,
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
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('livenessVerify', finalResult);
    return finalResult;
  }

  /// Returns the locally stored feature for [faceId], if present.
  static Future<FaceRecognitionResult> getFaceFeature(String faceId) async {
    final Map? result = await _channel.invokeMethod('getFaceFeature', {
      'faceId': faceId,
    });
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('getFaceFeature', finalResult);
    return finalResult;
  }

  /// Stores a 1024-character [feature] for [faceId].
  static Future<FaceRecognitionResult> insertFaceFeature({
    required String faceId,
    required String feature,
  }) async {
    final Map? result = await _channel.invokeMethod('insertFaceFeature', {
      'faceId': faceId,
      'feature': feature,
    });
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('insertFaceFeature', finalResult);
    return finalResult;
  }

  /// Enrolls [faceId] from a Base64-encoded image.
  static Future<FaceRecognitionResult> addFaceBySDKImage({
    required String faceId,
    required String imageBase64,
  }) async {
    final Map? result = await _channel.invokeMethod('addFaceBySDKImage', {
      'faceId': faceId,
      'imageBase64': imageBase64,
    });
    final finalResult = FaceRecognitionResult.fromMap(result ?? {});
    _printResult('addFaceBySDKImage', finalResult);
    return finalResult;
  }

  /// Deletes the locally stored feature for [faceId].
  static Future<void> deleteFaceFeature(String faceId) async {
    await _channel.invokeMethod('deleteFaceFeature', {
      'faceId': faceId,
    });
  }

  /// Checks whether a feature exists locally for [faceId].
  static Future<bool> isFaceExist(String faceId) async {
    final dynamic result = await _channel.invokeMethod('isFaceExist', {
      'faceId': faceId,
    });
    if (result is bool) return result;
    if (result is Map) return result['code'] == 1;
    return false;
  }

  /// Returns the stored face image as Base64, if available.
  static Future<String?> getFaceImageBase64(String faceId) async {
    final String? base64 = await _channel.invokeMethod('getFaceImageBase64', {
      'faceId': faceId,
    });
    return base64;
  }

  /// Switches to [cameraId] on Android.
  static Future<void> switchCamera(int cameraId) async {
    await _channel.invokeMethod('switchCamera', {'cameraId': cameraId});
  }

  /// Opens the native FaceAISDK demo screen.
  static Future<void> goNativeDemoNavi() async {
    await _channel.invokeMethod('goNativeDemoNavi');
  }

  static void _printResult(String method, FaceRecognitionResult result) {
    if (kDebugMode) {
      print('FaceRecognitionFlutter $method result: $result');
    }
  }
}

class FaceRecognitionResultCode {
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
