Pod::Spec.new do |s|
  s.name             = 'face_recognition_flutter'
  s.version          = '2.1.0'
  s.summary          = 'Offline face recognition and liveness detection for Flutter.'
  s.description      = <<-DESC
Offline face recognition and liveness detection Flutter plugin for Android and iOS.
                       DESC
  s.homepage         = 'https://github.com/FaceAISDK/FaceRecognition_Flutter'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'FaceAISDK' => 'FaceAISDK.Service@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.static_framework = true

  # Load localized strings from the app bundle.
  s.resources = ['Resources/*.lproj']


  s.vendored_frameworks = 'Frameworks/*.framework'
  s.dependency 'Flutter'
  s.dependency 'FaceAISDK_Core', '2026.09.22'
  s.dependency 'TensorFlowLiteSwift'
  s.platform = :ios, '15.5'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
    'BUILD_LIBRARY_FOR_DISTRIBUTION' => 'NO'
  }

  s.user_target_xcconfig = {
    'BUILD_LIBRARY_FOR_DISTRIBUTION' => 'NO'
  }
  s.swift_version = '5.9'
end
