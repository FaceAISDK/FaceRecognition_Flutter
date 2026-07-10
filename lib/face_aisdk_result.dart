import 'face_aisdk_flutter_plugin.dart';

/// FaceAISDK operation result.
class FaceAiSdkResult {
  /// Result code. See [FaceAiSdkResultCode].
  final int code;

  /// Tip message or error description.
  final String? message;

  /// Face similarity score [0.0, 1.0].
  final double? similarity;

  /// Liveness detection score.
  final double? livenessValue;

  /// Base64 string of the captured face image.
  final String? faceBase64;

  /// Face feature string.
  final String? faceFeature;

  FaceAiSdkResult({
    required this.code,
    this.message,
    this.similarity,
    this.livenessValue,
    this.faceBase64,
    this.faceFeature,
  });

  factory FaceAiSdkResult.fromMap(Map map) {
    return FaceAiSdkResult(
      code: map['code'] as int? ?? 0,
      message: map['message'] as String?,
      similarity: (map['similarity'] as num?)?.toDouble(),
      livenessValue: (map['livenessValue'] as num?)?.toDouble(),
      faceBase64: map['faceBase64'] as String?,
      faceFeature: map['faceFeature'] as String?,
    );
  }

  /// Whether the operation was successful.
  bool get isSuccess {
    return code == FaceAiSdkResultCode.verifySuccess ||
        code == FaceAiSdkResultCode.motionLivenessSuccess ||
        code == FaceAiSdkResultCode.allLivenessSuccess;
  }

  @override
  String toString() {
    return 'FaceAiSdkResult(code: $code, message: $message, similarity: $similarity, livenessValue: $livenessValue)';
  }
}
