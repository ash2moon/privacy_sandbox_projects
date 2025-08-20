
import 'flutter_plugin_test_platform_interface.dart';

class FlutterPluginTest {
  Future<String?> getPlatformVersion() {
    return FlutterPluginTestPlatform.instance.getPlatformVersion();
  }
}
