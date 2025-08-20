#include "include/flutter_plugin_test/flutter_plugin_test_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "flutter_plugin_test_plugin.h"

void FlutterPluginTestPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  flutter_plugin_test::FlutterPluginTestPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
