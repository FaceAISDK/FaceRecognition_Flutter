import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter/services.dart';
import 'package:face_recognition_flutter/face_recognition_flutter.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      // Match the SDK's English and Simplified Chinese UI.
      localizationsDelegates: [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      supportedLocales: [
        Locale('en', 'US'),
        Locale('zh', 'CN'),
      ],
      home: MyHomePage(),
    );
  }
}

/// Simple entry points for the SDK's main Flutter APIs.
class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const _green = Color(0xFF176B52);

  String _resultDisplay = '';

  // Use the same user ID for enrollment, verification, and feature management.
  final String _testFaceId = "yourFaceID";

  // Keep the last exported feature so the demo can delete and restore it.
  String? _cachedFeature;
  bool _isBusy = false;

  String t(String key) {
    bool isZh = Localizations.localeOf(context).languageCode == 'zh';
    Map<String, Map<String, String>> localizedValues = {
      'title': {'en': 'Face Recognition SDK Demo', 'zh': 'FaceAISDK 演示'},
      'actions': {'en': 'SDK Operations', 'zh': 'SDK 功能'},
      'result': {'en': 'Latest Result', 'zh': '最近一次结果'},
      'waiting': {'en': 'Waiting for operation...', 'zh': '等待操作...'},
      'btn_add_camera': {'en': 'Camera Enrollment', 'zh': 'SDK相机录入人脸信息'},
      'hint_add_camera': {'en': 'Capture and enroll a face', 'zh': '拍摄并保存人脸'},
      'btn_verify': {'en': 'Face Verification', 'zh': '人脸识别+活体检测'},
      'hint_verify': {
        'en': 'Verify an enrolled face with liveness checks',
        'zh': '与已录入的人脸进行比对'
      },
      'btn_liveness': {'en': 'Liveness Detection', 'zh': '检测人脸是否活体'},
      'hint_liveness': {
        'en': 'Check liveness without face matching',
        'zh': '仅检测活体，不进行身份比对'
      },
      'btn_query': {'en': 'Retrieve Face Feature', 'zh': '查询人脸特征信息'},
      'hint_query': {
        'en': 'Retrieve a stored face feature',
        'zh': '读取已保存的人脸特征'
      },
      'btn_compare': {'en': 'Compare Face Features', 'zh': '比较人脸特征相似度'},
      'hint_compare': {
        'en': 'Set two SDK features in the demo code first',
        'zh': '先在演示代码中填写两个 SDK 人脸特征'
      },
      'btn_insert': {'en': 'Restore Face Feature', 'zh': '同步人脸特征信息'},
      'hint_insert': {
        'en': 'Re-import a feature saved in this session',
        'zh': '重新导入本次采集的人脸特征'
      },
      'btn_add_image': {'en': 'Image Enrollment', 'zh': '人脸图录入人脸信息'},
      'hint_add_image': {
        'en': 'Enroll a face from clipboard image Base64',
        'zh': '从剪贴板读取图片 Base64'
      },
      'btn_delete': {'en': 'Delete Face Feature', 'zh': '删除人脸特征信息'},
      'hint_delete': {
        'en': 'Delete the stored face feature',
        'zh': '移除已保存的人脸特征'
      },
      'delete_done': {'en': 'Face feature deleted', 'zh': '删除完成'},
      'working': {'en': 'Calling SDK...', 'zh': '正在调用 SDK...'},
      'operation_failed': {'en': 'SDK call failed', 'zh': 'SDK 调用失败'},
      'feature_missing': {
        'en': 'Enroll a face or retrieve its feature first.',
        'zh': '请先录入或查询人脸，以获取可同步的特征值。'
      },
      'image_clipboard_empty': {
        'en': 'Copy a complete image Base64 to the clipboard first.',
        'zh': '请先将完整的图片 Base64 复制到剪贴板。'
      },
    };
    return localizedValues[key]?[isZh ? 'zh' : 'en'] ?? key;
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (_resultDisplay.isEmpty) {
      _resultDisplay = t('waiting');
    }
  }

  // Save a valid exported feature and show a short, readable result.
  void _updateDisplay(FaceRecognitionResult result, {String? method}) {
    if (!mounted) return;
    if (result.code == 1 && result.faceFeature?.length == 1024) {
      _cachedFeature = result.faceFeature;
    }
    if (method == 'faceVerify' || method == 'livenessVerify') {
      _resultDisplay = "code: ${result.code}\n"
          "message: ${result.message}\n"
          "${result.similarity != null ? 'similarity: ${result.similarity}\n' : ''}"
          "liveness: ${result.livenessValue}\n"
          "faceBase64: ${_truncate(result.faceBase64)}";
    } else if (method == 'compareFaceFeatures') {
      final similarity = result.similarity;
      _resultDisplay = "code: ${result.code}\n"
          "message: ${result.message}"
          "${similarity == null ? '' : '\nsimilarity: ${(similarity * 100).toStringAsFixed(2)}%'}";
    } else {
      _resultDisplay = "code: ${result.code}\n"
          "message: ${result.message}\n"
          "feature: ${_truncate(result.faceFeature)}\n"
          "faceBase64: ${_truncate(result.faceBase64)}";
    }
    setState(() {});
  }

  void _showMessage(String message) {
    if (mounted) setState(() => _resultDisplay = message);
  }

  String _featureLengthMessage(int index, int length) {
    const expected = FaceRecognitionFlutter.faceFeatureLength;
    return Localizations.localeOf(context).languageCode == 'zh'
        ? '人脸特征 $index 长度应为 $expected 个字符（当前 $length）。'
        : 'Face feature $index must be $expected characters (got $length).';
  }

  // Keep camera flows serial and surface platform errors in the result panel.
  Future<void> _runOperation(Future<void> Function() operation) async {
    if (_isBusy) return;
    setState(() {
      _isBusy = true;
      _resultDisplay = t('working');
    });
    try {
      await operation();
    } catch (error) {
      if (mounted) _showMessage('${t('operation_failed')}: $error');
    } finally {
      if (mounted) setState(() => _isBusy = false);
    }
  }

  // Keep the beginning and end of long biometric values on one line.
  String _truncate(dynamic value) {
    if (value is String && value.length > 19) {
      return "${value.substring(0, 8)}...${value.substring(value.length - 8)}";
    }
    return value?.toString() ?? "null";
  }

  @override
  Widget build(BuildContext context) {
    final bottomInset = MediaQuery.viewPaddingOf(context).bottom;
    final contactBottomSpace = Theme.of(context).platform == TargetPlatform.iOS
        ? bottomInset - 14
        : bottomInset;
    final contactBottomPadding =
        contactBottomSpace > 8 ? contactBottomSpace : 8.0;

    return Scaffold(
      backgroundColor: const Color(0xFFF5F8F6),
      appBar: AppBar(
        backgroundColor: Colors.white,
        foregroundColor: const Color(0xFF18352C),
        elevation: 0,
        title: Text(t('title'),
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
            style: const TextStyle(fontSize: 18, fontWeight: FontWeight.w600)),
      ),
      body: SafeArea(
        top: false,
        bottom: false,
        child: Column(
          children: [
            Expanded(
              child: ListView(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 4),
                children: [
                  Padding(
                    padding: const EdgeInsets.fromLTRB(4, 4, 4, 12),
                    child: Text(t('actions'),
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFF18352C))),
                  ),
                  _buildMenuButton(t('btn_add_camera'), t('hint_add_camera'),
                      Icons.add_a_photo_outlined, () async {
                    final result =
                        await FaceRecognitionFlutter.addFaceBySDKCamera(
                      faceId: _testFaceId,
                      addFacePerformanceMode: 1,
                      needShowConfirmDialog: true,
                    );
                    _updateDisplay(result);
                  }),

                  _buildMenuButton(t('btn_verify'), t('hint_verify'),
                      Icons.verified_user_outlined, () async {
                    final result = await FaceRecognitionFlutter.faceVerify(
                      faceId: _testFaceId,
                      threshold: 0.84,
                      livenessType: 1,
                      motionLivenessTypes: "1,2,3,4,5",
                      motionLivenessTimeOut: 7,
                      motionLivenessSteps: 2,
                      allowMultiFaces: false,
                    );
                    _updateDisplay(result, method: 'faceVerify');
                  }),

                  _buildMenuButton(t('btn_liveness'), t('hint_liveness'),
                      Icons.visibility_outlined, () async {
                    final result = await FaceRecognitionFlutter.livenessVerify(
                      livenessType: 1,
                      motionLivenessTypes: "1,2,3,4,5",
                      motionLivenessTimeOut: 7,
                      motionLivenessSteps: 2,
                      showResultTips: true,
                    );
                    _updateDisplay(result, method: 'livenessVerify');
                  }),

                  _buildMenuButton(
                      t('btn_query'), t('hint_query'), Icons.search_outlined,
                      () async {
                    final res = await FaceRecognitionFlutter.getFaceFeature(
                        _testFaceId);
                    _updateDisplay(res);
                  }),

                  // Replace both placeholders with features produced by the SDK.
                  _buildMenuButton(t('btn_compare'), t('hint_compare'),
                      Icons.compare_arrows_outlined, () async {
                    const feature1 = '';
                    const feature2 = '';
                    if (feature1.length !=
                        FaceRecognitionFlutter.faceFeatureLength) {
                      _showMessage(_featureLengthMessage(1, feature1.length));
                      return;
                    }
                    if (feature2.length !=
                        FaceRecognitionFlutter.faceFeatureLength) {
                      _showMessage(_featureLengthMessage(2, feature2.length));
                      return;
                    }
                    final result =
                        await FaceRecognitionFlutter.compareFaceFeatures(
                      feature1: feature1,
                      feature2: feature2,
                    );
                    _updateDisplay(result, method: 'compareFaceFeatures');
                  }),

                  // Restore the feature cached by this demo session.
                  _buildMenuButton(
                      t('btn_insert'), t('hint_insert'), Icons.sync_outlined,
                      () async {
                    final feature = _cachedFeature;
                    if (feature == null) {
                      _showMessage(t('feature_missing'));
                      return;
                    }
                    final res = await FaceRecognitionFlutter.insertFaceFeature(
                        faceId: _testFaceId, feature: feature);
                    _updateDisplay(res);
                  }),

                  // Use the clipboard as this demo's image input.
                  _buildMenuButton(t('btn_add_image'), t('hint_add_image'),
                      Icons.image_outlined, () async {
                    final imageBase64 =
                        (await Clipboard.getData(Clipboard.kTextPlain))
                            ?.text
                            ?.trim();
                    if (!mounted) return;
                    if (imageBase64 == null || imageBase64.isEmpty) {
                      _showMessage(t('image_clipboard_empty'));
                      return;
                    }
                    final res = await FaceRecognitionFlutter.addFaceBySDKImage(
                      faceId: _testFaceId,
                      imageBase64: imageBase64,
                    );
                    _updateDisplay(res);
                  }),

                  // Delete the local feature; keep the in-memory copy for restore.
                  _buildMenuButton(
                      t('btn_delete'), t('hint_delete'), Icons.delete_outline,
                      () async {
                    await FaceRecognitionFlutter.deleteFaceFeature(_testFaceId);
                    if (mounted) _showMessage(t('delete_done'));
                  }),
                ],
              ),
            ),

            // Keep the latest SDK result visible while the action list scrolls.
            Container(
              height: 153,
              width: double.infinity,
              margin: const EdgeInsets.fromLTRB(16, 8, 16, 4),
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.white,
                border: Border.all(color: const Color(0xFFDCE9E1)),
                borderRadius: BorderRadius.circular(16),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      const Icon(Icons.terminal_rounded,
                          color: _green, size: 16),
                      const SizedBox(width: 8),
                      Text(t('result'),
                          style: const TextStyle(
                              fontSize: 13,
                              fontWeight: FontWeight.w600,
                              color: Color(0xFF18352C))),
                      if (_isBusy) ...[
                        const SizedBox(width: 8),
                        const SizedBox(
                            width: 14,
                            height: 14,
                            child: CircularProgressIndicator(strokeWidth: 2)),
                      ],
                    ],
                  ),
                  const SizedBox(height: 10),
                  Expanded(
                    child: SingleChildScrollView(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          for (final line in _resultDisplay.split('\n'))
                            SelectableText(
                              line,
                              maxLines: line.startsWith('feature:') ||
                                      line.startsWith('faceBase64:')
                                  ? 1
                                  : null,
                              style: const TextStyle(
                                  color: Color(0xFF39574B),
                                  fontSize: 12,
                                  height: 1.4,
                                  fontFamily: 'monospace'),
                            ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),

            // Keep the contact close to the bottom without covering the home indicator.
            Padding(
              padding: EdgeInsets.fromLTRB(12, 8, 12, contactBottomPadding),
              child: const Text('FaceAISDK.Service@gmail.com',
                  style: TextStyle(color: Color(0xFF687C71), fontSize: 12)),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildMenuButton(String title, String subtitle, IconData icon,
      Future<void> Function() onPressed) {
    return Card(
      margin: const EdgeInsets.only(bottom: 8),
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(14),
        side: const BorderSide(color: Color(0xFFE2EBE6)),
      ),
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
        leading: CircleAvatar(
          backgroundColor: const Color(0xFFE8F3EC),
          child: Icon(icon, color: _green),
        ),
        title: Text(title,
            maxLines: 1,
            overflow: TextOverflow.ellipsis,
            style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w600)),
        subtitle: Text(subtitle, style: const TextStyle(fontSize: 12)),
        trailing: const Icon(Icons.chevron_right, color: Color(0xFF82958A)),
        onTap: _isBusy ? null : () => _runOperation(onPressed),
      ),
    );
  }
}
