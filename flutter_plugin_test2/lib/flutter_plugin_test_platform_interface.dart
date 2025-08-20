import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_plugin_test_method_channel.dart';

abstract class FlutterPluginTestPlatform extends PlatformInterface {
  /// Constructs a FlutterPluginTestPlatform.
  FlutterPluginTestPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterPluginTestPlatform _instance = MethodChannelFlutterPluginTest();

  /// The default instance of [FlutterPluginTestPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterPluginTest].
  static FlutterPluginTestPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterPluginTestPlatform] when
  /// they register themselves.
  static set instance(FlutterPluginTestPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
