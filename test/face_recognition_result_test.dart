import 'package:face_recognition_flutter/face_recognition_flutter.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  test('FaceRecognitionResult parses native result maps', () {
    final result = FaceRecognitionResult.fromMap({
      'code': FaceRecognitionResultCode.verifySuccess,
      'message': 'ok',
      'similarity': 0.91,
      'livenessValue': 0.88,
      'faceBase64': 'base64',
      'faceFeature': 'feature',
    });

    expect(result.code, FaceRecognitionResultCode.verifySuccess);
    expect(result.message, 'ok');
    expect(result.similarity, 0.91);
    expect(result.livenessValue, 0.88);
    expect(result.faceBase64, 'base64');
    expect(result.faceFeature, 'feature');
    expect(result.isSuccess, isTrue);
  });
}
