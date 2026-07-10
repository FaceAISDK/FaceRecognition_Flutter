import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:face_aisdk_flutter_plugin/face_aisdk_flutter_plugin.dart';

/// Main entry point of the application.
/// 应用程序入口。
void main() {
  runApp(const MyApp());
}

/// The root widget of the application.
/// 应用程序根组件。
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      debugShowCheckedModeBanner: false,
      // Configure multi-language support (English/Chinese).
      // 配置多语言支持（英文/中文）。
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

/// The home page containing all FaceAISDK demo operations.
/// 包含所有 FaceAISDK 演示操作的主页面。
class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  // String to display operation results in the UI.
  // 用于在 UI 中显示操作结果的字符串。
  String _resultDisplay = '';

  // Test FaceID, usually a unique identifier in your system (e.g., account ID/Phone/ID card).
  // 测试用 FaceID，通常为您系统中的唯一标识（如账号ID/手机号/身份证等）。
  final String _testFaceId = "yourFaceID";

  // Base64 encoded image for testing image-based enrollment.
  // (640*480 standard recommended).
  // 用于测试图片人脸采集的 Base64 编码图片（建议遵循 640*480 标准）。
  final String _testImageBase64 = "data:image/jpeg;base64,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";

  // Simple multi-language string mapping helper.
  // 简单的多语言映射助手函数。
  String t(String key) {
    bool isZh = Localizations.localeOf(context).languageCode == 'zh';
    Map<String, Map<String, String>> localizedValues = {
      'title': {'en': 'FaceAISDK Demo', 'zh': 'FaceAISDK人脸识别演示'},
      'waiting': {'en': 'Waiting for operation...', 'zh': '等待操作...'},
      'result_empty': {'en': 'Result is empty', 'zh': '结果为空'},
      'btn_add_camera': {'en': 'Register Face via SDK Camera', 'zh': 'SDK相机录入人脸信息'},
      'btn_verify': {'en': 'Face Verify + Liveness', 'zh': '人脸识别+活体检测'},
      'btn_liveness': {'en': 'Liveness Detection Only', 'zh': '检测人脸是否活体'},
      'btn_query': {'en': 'Query Face Feature', 'zh': '查询人脸特征信息'},
      'btn_insert': {'en': 'Sync/Insert Face Feature', 'zh': '同步人脸特征信息'},
      'btn_add_image': {'en': 'Register Face via Image', 'zh': '人脸图录入人脸信息'},
      'btn_delete': {'en': 'Delete Face Feature', 'zh': '删除人脸特征信息'},
      'delete_done': {'en': 'Delete Completed', 'zh': '删除完成'},
    };
    return localizedValues[key]?[isZh ? 'zh' : 'en'] ?? key;
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    // Initialize the display text with the localized 'waiting' string.
    // 使用本地化的“等待”字符串初始化显示文本。
    if (_resultDisplay.isEmpty) {
      _resultDisplay = t('waiting');
    }
  }

  /// Updates the result display area with the SDK result.
  /// 使用 SDK 返回结果更新结果显示区域。
  void _updateDisplay(FaceAiSdkResult result, {String? method}) {
    if (method == 'faceVerify' || method == 'livenessVerify') {
       _resultDisplay = "code: ${result.code}\n"
                        "message: ${result.message}\n"
                        "${result.similarity != null ? 'similarity: ${result.similarity}\n' : ''}"
                        "liveness: ${result.livenessValue}\n"
                        "faceBase64: ${_truncate(result.faceBase64)}";
    } else {
       _resultDisplay = "code: ${result.code}\n"
                        "message: ${result.message}\n"
                        "feature: ${_truncate(result.faceFeature)}\n"
                        "faceBase64: ${_truncate(result.faceBase64)}";
    }
    setState(() {});
  }

  /// Truncates long strings (like Base64 or Features) for display.
  /// 截断长字符串（如 Base64 或特征值）以便于显示。
  String _truncate(dynamic value) {
    if (value is String && value.length > 50) {
      return "${value.substring(0, 20)}...${value.substring(value.length - 20)}";
    }
    return value?.toString() ?? "null";
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFF1B4332),
        title: Text(t('title'), style: const TextStyle(color: Colors.white, fontSize: 18)),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Expanded(
            child: ListView(
              children: [
                /**
                 * 1. Register face features via SDK Camera.
                 *    (Also can be used to detect and crop face for self-server verification).
                 * 1. 人脸识别调用 SDK 相机录入人脸特征值。
                 *    （也可以用于检测人脸后裁剪好后用于自身服务器验证）。
                 * 
                 * @param faceId: Unique identifier for the user (account ID/Phone/ID card).
                 * @param addFacePerformanceMode: 1. Fast Mode, 2. Precise Mode (high quality).
                 * @param needShowConfirmDialog: Whether to show a confirmation dialog (highly recommended: true).
                 */
                _buildMenuButton(t('btn_add_camera'), () async {
                  final result = await FaceAiSdkFlutterPlugin.addFaceBySDKCamera(
                    faceId: _testFaceId,
                    addFacePerformanceMode: 1, 
                    needShowConfirmDialog: true,
                  );
                  _updateDisplay(result);
                }),

                /**
                 * 2. Face Verification (1:1) + Liveness Detection.
                 *    (Includes Silent Liveness by default) Verifies if it's the current user.
                 * 2. 「1:1人脸识别」+ 活体检测（默认都含静默活体）。
                 *    校验是否为当前注册用户。
                 * 
                 * @param faceId: User ID (Unique identifier string).
                 * @param threshold: Similarity threshold [0.75, 0.95], default 0.84.
                 * @param livenessType: 1.Action, 2.Action+Color, 3.Color (Not for strong light), 4.Silent.
                 * @param motionLivenessTypes: "1,2,3,4,5" (1.Mouth, 2.Smile, 3.Blink, 4.Shake, 5.Nod).
                 * @param motionLivenessTimeOut: Action timeout [3, 10] seconds.
                 * @param motionLivenessSteps: Actions required (1 or 2).
                 * @param allowMultiFaces: Allow multiple faces in frame (Android only).
                 */
                _buildMenuButton(t('btn_verify'), () async {
                  final result = await FaceAiSdkFlutterPlugin.faceVerify(
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

                /**
                 * 3. Liveness Detection Only (Includes Action + Color + Silent).
                 * 3. 活体检测（包含动作 + 炫彩 + 静默活体）。
                 * 
                 * @param livenessType: 1.Action, 2.Action+Color, 3.Color, 4.Silent.
                 * @param motionLivenessTypes: "1,2,3,4,5" (1.Mouth, 2.Smile, 3.Blink, 4.Shake, 5.Nod).
                 * @param motionLivenessTimeOut: Action timeout [3, 10] seconds.
                 * @param motionLivenessSteps: Actions required (1 or 2).
                 */
                _buildMenuButton(t('btn_liveness'), () async {
                  final result = await FaceAiSdkFlutterPlugin.livenessVerify(
                    livenessType: 2,
                    motionLivenessTypes: "1,2,3,4,5",
                    motionLivenessTimeOut: 7,
                    motionLivenessSteps: 2,
                    showResultTips: true,
                  );
                  _updateDisplay(result, method: 'livenessVerify');
                }),

                /**
                 * 4. Check if a local face feature exists for FaceID.
                 *    Retrieves 1024-length encrypted feature string.
                 * 4. 检测本地是否有 FaceID 对应的人脸特征值。
                 *    查询长度为 1024 的加密特征值字符串。
                 */
                _buildMenuButton(t('btn_query'), () async {
                  final res = await FaceAiSdkFlutterPlugin.getFaceFeature(_testFaceId);
                  _updateDisplay(res);
                }),

                /**
                 * 5. Sync/Insert 1:1 face feature to SDK.
                 *    Used for syncing existing features when changing devices.
                 * 5. 演示同步 1:1 人脸特征值到 SDK。
                 *    账号换设备需要此功能把已经录入的人脸特征同步到新设备。
                 */
                _buildMenuButton(t('btn_insert'), () async {
                  final res = await FaceAiSdkFlutterPlugin.insertFaceFeature(
                    faceId: _testFaceId, 
                    feature: "your_1024_face_feature_string"
                  );
                  _updateDisplay(res);
                }),

                /**
                 * 6. Extract face features from a provided image (Base64).
                 *    (Standard: 640*480 recommended).
                 * 6. 提取人脸图片（Base64）中的特征值。
                 *    （人脸图建议遵循规范：640*480）。
                 */
                _buildMenuButton(t('btn_add_image'), () async {
                  final res = await FaceAiSdkFlutterPlugin.addFaceBySDKImage(
                    faceId: _testFaceId,
                    imageBase64: _testImageBase64,
                  );
                  _updateDisplay(res);
                }),

                /**
                 * 7. Delete face feature information.
                 * 7. 删除人脸特征信息。
                 */
                _buildMenuButton(t('btn_delete'), () async {
                  await FaceAiSdkFlutterPlugin.deleteFaceFeature(_testFaceId);
                  setState(() => _resultDisplay = t('delete_done'));
                }),
              ],
            ),
          ),
          
          // Result display area showing operation details and status codes.
          // 结果展示区域，显示操作详情和状态码。
          Container(
            height: 200,
            width: double.infinity,
            margin: const EdgeInsets.all(12),
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: const Color(0xFFF8F0FF),
              border: Border.all(color: Colors.purple, width: 1.5),
              borderRadius: BorderRadius.circular(8),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Expanded(
                  child: SingleChildScrollView(
                    child: Text(
                      _resultDisplay,
                      style: const TextStyle(color: Colors.purple, fontSize: 14, height: 1.5, fontFamily: 'monospace'),
                    ),
                  ),
                ),
              ],
            ),
          ),

          // Technical support contact.
          // 技术支持联系方式。
          const Padding(
            padding: EdgeInsets.symmetric(vertical: 5),
            child: Text('Email: FaceAISDK.Service@gmail.com', style: TextStyle(color: Colors.black54, fontSize: 12)),
          ),
        ],
      ),
    );
  }

  /// Helper widget to build menu list items.
  /// 用于构建菜单列表项的助手组件。
  Widget _buildMenuButton(String title, VoidCallback onPressed) {
    return Container(
      decoration: const BoxDecoration(border: Border(bottom: BorderSide(color: Color(0xFFEEEEEE)))),
      child: ListTile(
        title: Text(title, style: const TextStyle(fontSize: 16, fontWeight: FontWeight.w400)),
        onTap: onPressed,
        tileColor: Colors.white,
      ),
    );
  }
}
