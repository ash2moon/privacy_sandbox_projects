import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_plugin_test/flutter_plugin_test.dart';
import 'package:flutter_plugin_test/flutter_plugin_test_platform_interface.dart';
import 'package:flutter_plugin_test/flutter_plugin_test_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterPluginTestPlatform
    with MockPlatformInterfaceMixin
    implements FlutterPluginTestPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterPluginTestPlatform initialPlatform = FlutterPluginTestPlatform.instance;

  test('$MethodChannelFlutterPluginTest is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterPluginTest>());
  });

  test('getPlatformVersion', () async {
    FlutterPluginTest flutterPluginTestPlugin = FlutterPluginTest();
    MockFlutterPluginTestPlatform fakePlatform = MockFlutterPluginTestPlatform();
    FlutterPluginTestPlatform.instance = fakePlatform;

    expect(await flutterPluginTestPlugin.getPlatformVersion(), '42');
  });
}
