import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:face_recognition_flutter_example/main.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();
  const channel = MethodChannel('FaceRecognition_Flutter');
  final messenger =
      TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger;

  tearDown(() => messenger.setMockMethodCallHandler(channel, null));

  Future<void> tapAction(WidgetTester tester, String label) async {
    final action = find.text(label);
    await tester.scrollUntilVisible(
      action,
      150,
      scrollable: find.byType(Scrollable).first,
    );
    await Scrollable.ensureVisible(tester.element(action), alignment: 0.2);
    await tester.pumpAndSettle();
    await tester.tap(action);
    await tester.pumpAndSettle();
  }

  testWidgets('sync uses a feature returned by the SDK', (tester) async {
    tester.view.physicalSize = const Size(375, 812);
    tester.view.devicePixelRatio = 1;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);

    final calls = <MethodCall>[];
    final feature = List.filled(1024, 'a').join();
    final imageBase64 = List.filled(100, 'b').join();
    messenger.setMockMethodCallHandler(channel, (call) async {
      calls.add(call);
      switch (call.method) {
        case 'getFaceFeature':
          return {
            'code': 1,
            'faceFeature': feature,
            'faceBase64': imageBase64,
          };
        case 'insertFaceFeature':
          return {'code': 1, 'message': 'Success'};
      }
      return null;
    });

    await tester.pumpWidget(const MyApp());
    expect(find.text('Face recognition in Flutter'), findsNothing);
    expect(tester.widget<Text>(find.text('Face Recognition SDK Demo')).maxLines,
        1);
    expect(tester.widget<Text>(find.text('Camera Enrollment')).maxLines, 1);
    await tapAction(tester, 'Restore Face Feature');
    expect(calls, isEmpty);
    expect(find.textContaining('Enroll a face or retrieve its feature first'),
        findsOneWidget);

    await tapAction(tester, 'Retrieve Face Feature');
    final featureLine = tester.widget<SelectableText>(find.byWidgetPredicate(
      (widget) =>
          widget is SelectableText &&
          (widget.data?.startsWith('feature:') ?? false),
    ));
    final imageLine = tester.widget<SelectableText>(find.byWidgetPredicate(
      (widget) =>
          widget is SelectableText &&
          (widget.data?.startsWith('faceBase64:') ?? false),
    ));
    expect(featureLine.data, 'feature: aaaaaaaa...aaaaaaaa');
    expect(imageLine.data, 'faceBase64: bbbbbbbb...bbbbbbbb');
    expect(featureLine.maxLines, 1);
    expect(imageLine.maxLines, 1);

    await tapAction(tester, 'Restore Face Feature');
    expect(calls.last.method, 'insertFaceFeature');
    expect(calls.last.arguments, {'faceId': 'yourFaceID', 'feature': feature});
  });

  testWidgets('a pending SDK call blocks another action and reports errors',
      (tester) async {
    final calls = <String>[];
    final pending = Completer<Object?>();
    messenger.setMockMethodCallHandler(channel, (call) {
      calls.add(call.method);
      return pending.future;
    });

    await tester.pumpWidget(const MyApp());
    final verify = find.text('Face Verification');
    await tester.scrollUntilVisible(
      verify,
      150,
      scrollable: find.byType(Scrollable).first,
    );
    await tester.tap(verify);
    await tester.pump();

    final liveness = find.text('Liveness Detection');
    await tester.scrollUntilVisible(
      liveness,
      150,
      scrollable: find.byType(Scrollable).first,
    );
    await tester.tap(liveness);
    expect(calls, ['faceVerify']);

    pending.completeError(PlatformException(
      code: 'CAMERA_ERROR',
      message: 'Camera unavailable',
    ));
    await tester.pumpAndSettle();
    expect(find.textContaining('SDK call failed'), findsOneWidget);
  });
}
