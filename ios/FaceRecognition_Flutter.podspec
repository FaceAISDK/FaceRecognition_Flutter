Pod::Spec.new do |s|
  s.name             = 'FaceRecognition_Flutter'
  s.version          = '0.2.0'
  s.summary          = 'A Flutter plugin for FaceAISDK offline face recognition.'
  s.description      = <<-DESC
A Flutter plugin for FaceAISDK offline face recognition.
                       DESC
  s.homepage         = 'https://github.com/FaceAISDK/FaceAISDK_Flutter_Plugin'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'FaceAISDK' => 'FaceAISDK.Service@gmail.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.static_framework = true

  # 修改前
  # s.resource_bundles = {
  #   'FaceRecognition_Flutter' => ['Resources/**/*']
  # }

  # Keep resources in the app main bundle so NSLocalizedString(...) can resolve
  # Localizable.strings without changing Swift code under Classes/FaceAISDK.
  s.resources = ['Resources/*.lproj']


  s.vendored_frameworks = 'Frameworks/*.framework'
  s.dependency 'Flutter'
  s.dependency 'FaceAISDK_Core', '2026.07.22'
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
