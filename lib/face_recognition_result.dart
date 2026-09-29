import 'face_recognition_flutter.dart';

/// FaceRecognition operation result.
class FaceRecognitionResult {
  /// Result code. See [FaceRecognitionResultCode].
  final int code;

  /// Native status or error message.
  final String? message;

  /// Face similarity score [0.0, 1.0].
  final double? similarity;

  /// Liveness detection score.
  final double? livenessValue;

  /// Base64 string of the captured face image.
  final String? faceBase64;

  /// Face feature string.
  final String? faceFeature;

  FaceRecognitionResult({
    required this.code,
    this.message,
    this.similarity,
    this.livenessValue,
    this.faceBase64,
    this.faceFeature,
  });

  factory FaceRecognitionResult.fromMap(Map map) {
    return FaceRecognitionResult(
      code: map['code'] as int? ?? 0,
      message: map['message'] as String?,
      similarity: (map['similarity'] as num?)?.toDouble(),
      livenessValue: (map['livenessValue'] as num?)?.toDouble(),
      faceBase64: map['faceBase64'] as String?,
      faceFeature: map['faceFeature'] as String?,
    );
  }

  /// True for success codes 1, 3, and 10.
  bool get isSuccess {
    return code == FaceRecognitionResultCode.verifySuccess ||
        code == FaceRecognitionResultCode.motionLivenessSuccess ||
        code == FaceRecognitionResultCode.allLivenessSuccess;
  }

  @override
  String toString() {
    return 'FaceRecognitionResult(code: $code, message: $message, similarity: $similarity, livenessValue: $livenessValue)';
  }
}
