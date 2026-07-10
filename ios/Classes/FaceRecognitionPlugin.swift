import Flutter
import UIKit
import FaceAISDK_Core

public class FaceRecognitionPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    // Initialize FaceAISDK
    FaceAISDK.initSDK()

    let channel = FlutterMethodChannel(name: "FaceRecognition_Flutter", binaryMessenger: registrar.messenger())
    let instance = FaceRecognitionPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)

    let factory = FaceRecognitionViewFactory(messenger: registrar.messenger())
    registrar.register(factory, withId: "com.facerecognition/view")
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    let args = call.arguments as? [String: Any]
    print("FaceAiSdkPlugin handle method: \(call.method), args: \(String(describing: args))")

    switch call.method {
    case "addFaceBySDKCamera":
      let faceId = args?["faceId"] as? String ?? ""
      let performanceMode = args?["addFacePerformanceMode"] as? NSNumber ?? 0

      let needConfirm = args?["needShowConfirmDialog"] as? Bool ?? true
      FaceSDKSwiftManager.showAddFaceByCamera(faceId, performanceMode, needConfirm) { code, feature, message in
          var faceBase64 = ""
          if code.intValue == 1 {
              faceBase64 = FaceSDKSwiftManager.getFaceImageBase64(faceId)
          }
          let res: [String: Any] = ["code": code, "faceFeature": feature, "faceBase64": faceBase64, "message": message]
          print("FaceAiSdkPlugin addFaceBySDKCamera result: \(res)")
          result(res)
      }

    case "addFaceBySDKImage":
      let faceId = args?["faceId"] as? String ?? ""
      let imageBase64 = args?["imageBase64"] as? String ?? ""
      FaceSDKSwiftManager.addFaceByBase64(faceId, imageBase64) { code, feature, message in
          let res: [String: Any] = ["code": code, "faceFeature": feature, "message": message]
          print("FaceAiSdkPlugin addFaceBySDKImage result: \(res)")
          result(res)
      }

    case "faceVerify":
      let faceId = args?["faceId"] as? String ?? ""
      let threshold = args?["threshold"] as? NSNumber ?? 0.84
      let livenessType = args?["livenessType"] as? NSNumber ?? 1
      let motionLivenessTypes = args?["motionLivenessTypes"] as? String ?? "1,2,3,4,5"
      let motionLivenessTimeOut = args?["motionLivenessTimeOut"] as? NSNumber ?? 7
      let motionLivenessSteps = args?["motionLivenessSteps"] as? NSNumber ?? 2
      FaceSDKSwiftManager.showFaceVerify(faceId, threshold, livenessType, motionLivenessTypes, motionLivenessTimeOut, motionLivenessSteps) { code, similarity, liveness, message in
          var faceBase64 = ""
          if code.intValue == 1 {
              faceBase64 = FaceSDKSwiftManager.getFaceImageBase64("verifyBitmap")
          }
          let res: [String: Any] = [
              "code": code,
              "similarity": similarity,
              "livenessValue": liveness,
              "faceBase64": faceBase64,
              "message": message
          ]
          print("FaceAiSdkPlugin faceVerify result: \(res)")
          result(res)
      }

    case "livenessVerify":
      let livenessType = args?["livenessType"] as? NSNumber ?? 1
      let motionLivenessTypes = args?["motionLivenessTypes"] as? String ?? "1,2,3,4,5"
      let motionLivenessTimeOut = args?["motionLivenessTimeOut"] as? NSNumber ?? 7
      let motionLivenessSteps = args?["motionLivenessSteps"] as? NSNumber ?? 2
      FaceSDKSwiftManager.showLivenessVerify(livenessType, motionLivenessTypes, motionLivenessTimeOut, motionLivenessSteps) { code, liveness, message in
          var faceBase64 = ""
          if code.intValue == 10 {
              faceBase64 = FaceSDKSwiftManager.getFaceImageBase64("liveBitmap")
          }
          let res: [String: Any] = [
              "code": code,
              "livenessValue": liveness,
              "faceBase64": faceBase64,
              "message": message
          ]
          print("FaceAiSdkPlugin livenessVerify result: \(res)")
          result(res)
      }

    case "deleteFaceFeature":
      let faceId = args?["faceId"] as? String ?? ""
      FaceSDKSwiftManager.deleteFaceFeature(faceId)
      print("FaceAiSdkPlugin deleteFaceFeature faceId: \(faceId)")
      result(["code": 1, "message": "Success"])

    case "insertFaceFeature":
      let faceId = args?["faceId"] as? String ?? ""
      let feature = args?["feature"] as? String ?? ""
      FaceSDKSwiftManager.insertFaceFeature(faceId, feature) { code, message in
          let res: [String: Any] = ["code": code, "message": message, "faceId": faceId]
          print("FaceAiSdkPlugin insertFaceFeature result: \(res)")
          result(res)
      }

    case "getFaceFeature":
      let faceId = args?["faceId"] as? String ?? ""
      let feature = FaceSDKSwiftManager.getiOSFaceFeature(faceId)
      var code = 0
      var message = "Face Feature not exist"
      if !feature.isEmpty {
          if feature.count == 1024 {
              code = 1
              message = "Face Feature exist"
          } else {
              message = "Face Feature length should be 1024"
          }
      }
      let res: [String: Any] = ["code": code, "message": message, "faceId": faceId, "faceFeature": feature]
      result(res)

    case "isFaceExist":
      let faceId = args?["faceId"] as? String ?? ""
      let feature = FaceSDKSwiftManager.getiOSFaceFeature(faceId)
      var code = 0
      var message = "Face Feature not exist"
      if !feature.isEmpty {
          if feature.count == 1024 {
              code = 1
              message = "Face Feature exist"
          } else {
              message = "Face Feature length should be 1024"
          }
      }
      let res: [String: Any] = ["code": code, "message": message, "faceId": faceId, "faceFeature": feature]
      result(res)

    case "getFaceImageBase64":
      let faceId = args?["faceId"] as? String ?? ""
      result(FaceSDKSwiftManager.getFaceImageBase64(faceId))

    case "goNativeDemoNavi":
      FaceSDKSwiftManager.goNativeDemoNavi()
      result(nil)

    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
