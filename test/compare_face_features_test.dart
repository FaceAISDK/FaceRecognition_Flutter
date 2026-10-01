import 'package:face_recognition_flutter/face_recognition_flutter.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  const channel = MethodChannel('FaceRecognition_Flutter');
  final messenger =
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger;

  tearDown(() => messenger.setMockMethodCallHandler(channel, null));

  test('rejects missing or malformed features before calling native code',
      () async {
    final calls = <MethodCall>[];
    messenger.setMockMethodCallHandler(channel, (call) async {
      calls.add(call);
      return null;
    });

    final valid =
        List.filled(FaceRecognitionFlutter.faceFeatureLength, 'a').join();
    final missing = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: '',
      feature2: valid,
    );
    expect(missing.code, 0);
    expect(missing.message, contains('Feature 1 must be 1024 characters'));

    final short = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: valid,
      feature2: 'short',
    );
    expect(short.message, contains('Feature 2 must be 1024 characters'));

    final malformed = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: valid,
      feature2: List.filled(1024, '*').join(),
    );
    expect(malformed.message, contains('Feature 2 must be valid unpadded'));

    final padded = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: '${List.filled(1022, 'a').join()}==',
      feature2: valid,
    );
    expect(padded.code, 0);
    expect(padded.message, contains('Feature 1 must be valid unpadded'));

    final whitespace = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: '${valid.substring(0, 512)} ${valid.substring(513)}',
      feature2: valid,
    );
    expect(whitespace.code, 0);
    expect(calls, isEmpty);
  });

  test('passes valid features to native SDK and returns its score', () async {
    MethodCall? nativeCall;
    messenger.setMockMethodCallHandler(channel, (call) async {
      nativeCall = call;
      return {'code': 1, 'message': 'Comparison completed', 'similarity': 0.91};
    });

    final feature1 = List.filled(256, '-_aA').join();
    final feature2 = List.filled(1024, 'b').join();
    final result = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: ' \n$feature1\n',
      feature2: ' $feature2 ',
    );

    expect(nativeCall?.method, 'compareFaceFeatures');
    expect(nativeCall?.arguments, {
      'feature1': feature1,
      'feature2': feature2,
    });
    expect(result.isSuccess, isTrue);
    expect(result.similarity, 0.91);
  });

  test('rejects absent, non-finite, or out-of-range native success scores',
      () async {
    final feature = List.filled(1024, 'a').join();
    for (final score in [null, double.nan, double.infinity, -0.1, 1.1, '0.9']) {
      messenger.setMockMethodCallHandler(
          channel,
          (_) async => {
                'code': 1,
                'similarity': score,
              });
      final result = await FaceRecognitionFlutter.compareFaceFeatures(
        feature1: feature,
        feature2: feature,
      );
      expect(result.isSuccess, isFalse);
      expect(result.similarity, isNull);
      expect(result.message, contains('invalid similarity score'));
    }
  });

  test('preserves valid boundary scores and native failure messages', () async {
    final feature = List.filled(1024, 'a').join();
    for (final score in [0.0, 1.0]) {
      messenger.setMockMethodCallHandler(
          channel, (_) async => {'code': 1, 'similarity': score});
      final result = await FaceRecognitionFlutter.compareFaceFeatures(
        feature1: feature,
        feature2: feature,
      );
      expect(result.isSuccess, isTrue);
      expect(result.similarity, score);
    }
    messenger.setMockMethodCallHandler(channel,
        (_) async => {'code': 0, 'message': 'Face feature comparison failed'});
    final failed = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: feature,
      feature2: feature,
    );
    expect(failed.isSuccess, isFalse);
    expect(failed.message, 'Face feature comparison failed');
  });

  test('reports missing native results and propagates platform errors',
      () async {
    final feature = List.filled(1024, 'a').join();
    messenger.setMockMethodCallHandler(channel, (_) async => null);
    final empty = await FaceRecognitionFlutter.compareFaceFeatures(
      feature1: feature,
      feature2: feature,
    );
    expect(empty.isSuccess, isFalse);
    expect(empty.message, contains('returned no result'));

    messenger.setMockMethodCallHandler(channel, (_) async {
      throw PlatformException(code: 'SDK_ERROR', message: 'SDK unavailable');
    });
    await expectLater(
      FaceRecognitionFlutter.compareFaceFeatures(
        feature1: feature,
        feature2: feature,
      ),
      throwsA(isA<PlatformException>()),
    );
  });
}
