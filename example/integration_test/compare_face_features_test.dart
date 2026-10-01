import 'dart:convert';
import 'dart:typed_data';

import 'package:face_recognition_flutter/face_recognition_flutter.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';

// Synthetic vectors exercise the native API without storing biometric data.
String feature({double direction = 1, bool urlSafe = true}) {
  final bytes = ByteData(768)
    ..setFloat32(0, direction * 0.9921875, Endian.little)
    ..setFloat32(4, direction * 0.125, Endian.little);
  return (urlSafe ? base64Url : base64).encode(bytes.buffer.asUint8List());
}

void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();
  const channel = MethodChannel('FaceRecognition_Flutter');

  testWidgets('native comparison supports standard and URL-safe SDK features',
      (tester) async {
    final same = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: feature(urlSafe: false),
      feature2: feature(),
    );
    expect(same.isSuccess, isTrue, reason: same.message);
    expect(same.similarity, greaterThanOrEqualTo(0.99));

    final different = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: feature(),
      feature2: feature(direction: -1),
    );
    expect(different.isSuccess, isTrue, reason: different.message);
    expect(different.similarity, inInclusiveRange(0, 1));
    expect(different.similarity, lessThan(same.similarity!));
  });

  testWidgets('native validation rejects malformed payloads before comparison',
      (tester) async {
    for (final invalid in [
      '',
      List.filled(1024, '*').join(),
      '${List.filled(1022, 'a').join()}==',
    ]) {
      // Bypass Dart validation to exercise the Android/iOS guard directly.
      final result = await channel.invokeMapMethod<String, dynamic>(
        'compareFaceFeatures',
        {'feature1': feature(), 'feature2': invalid},
      );
      expect(result?['code'], 0);
      expect(result?['similarity'], isNull);
      expect(result?['message'], isNotEmpty);
    }
  });
}
