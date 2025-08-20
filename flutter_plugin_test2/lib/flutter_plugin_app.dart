import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Privacy Sandbox Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.example.privacy_client/runtime_aware');

  // State variables
  int _fileSize = 3;
  String _adType = 'Banner';
  String _mediationOption = 'NONE';
  bool _allowSdkToLaunchActivity = false;

  final List<int> _fileSizes = [3, 9, 18];
  final List<String> _adTypes = ['Banner', 'WebView Banner'];
  final List<String> _mediationOptions = ['NONE', 'RUNTIME_MEDIATEE', 'INAPP_MEDIATEE', 'REFRESH_MEDIATED_ADS'];

  void _showSnackBar(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  Future<void> _initializeSdk() async {
    try {
      final String result = await platform.invokeMethod('initializeRuntimeAwareSdk');
      _showSnackBar('SDK Initialization: $result');
    } on PlatformException catch (e) {
      _showSnackBar('Failed to initialize SDK: ${e.message}');
    }
  }

  Future<void> _createFile() async {
    try {
      final String? result = await platform.invokeMethod('createFile', {'sizeInMb': _fileSize});
      if (result != null) {
        _showSnackBar(result);
      } else {
        _showSnackBar('Please load the SDK first!');
      }
    } on PlatformException catch (e) {
      _showSnackBar('Failed to create file: ${e.message}');
    }
  }

  Future<void> _requestBanner() async {
    try {
      await platform.invokeMethod('requestBanner', {
        'loadWebView': _adType.contains('WebView'),
        'mediationType': _mediationOption,
        'shouldStartActivity': _allowSdkToLaunchActivity,
      });
    } on PlatformException catch (e) {
      _showSnackBar('Failed to request banner: ${e.message}');
    }
  }

  Future<void> _showFullscreen() async {
    try {
      await platform.invokeMethod('showFullscreen', {
        'mediationType': _mediationOption,
        'shouldStartActivity': _allowSdkToLaunchActivity,
      });
    } on PlatformException catch (e) {
      _showSnackBar('Failed to show fullscreen ad: ${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Privacy Sandbox Demo'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              ElevatedButton(
                onPressed: _initializeSdk,
                child: const Text('Initialize SDK'),
              ),
              const Divider(height: 30),
              Row(
                children: [
                  const Text('File Size:'),
                  const SizedBox(width: 10),
                  DropdownButton<int>(
                    value: _fileSize,
                    onChanged: (int? newValue) {
                      setState(() {
                        _fileSize = newValue!;
                      });
                    },
                    items: _fileSizes.map<DropdownMenuItem<int>>((int value) {
                      return DropdownMenuItem<int>(
                        value: value,
                        child: Text('$value MB'),
                      );
                    }).toList(),
                  ),
                  const SizedBox(width: 20),
                  ElevatedButton(
                    onPressed: _createFile,
                    child: const Text('Create File'),
                  ),
                ],
              ),
              const Divider(height: 30),
              Row(
                children: [
                  const Text('Ad Type:'),
                  const SizedBox(width: 10),
                  DropdownButton<String>(
                    value: _adType,
                    onChanged: (String? newValue) {
                      setState(() {
                        _adType = newValue!;
                      });
                    },
                    items: _adTypes.map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              Row(
                children: [
                  const Text('Mediation:'),
                  const SizedBox(width: 10),
                  DropdownButton<String>(
                    value: _mediationOption,
                    onChanged: (String? newValue) {
                      setState(() {
                        _mediationOption = newValue!;
                      });
                    },
                    items: _mediationOptions.map<DropdownMenuItem<String>>((String value) {
                      return DropdownMenuItem<String>(
                        value: value,
                        child: Text(value),
                      );
                    }).toList(),
                  ),
                ],
              ),
              CheckboxListTile(
                title: const Text('Allow SDK to launch an activity'),
                value: _allowSdkToLaunchActivity,
                onChanged: (bool? value) {
                  setState(() {
                    _allowSdkToLaunchActivity = value!;
                  });
                },
                controlAffinity: ListTileControlAffinity.leading,
              ),
              const SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  ElevatedButton(
                    onPressed: _requestBanner,
                    child: const Text('Request Banner'),
                  ),
                  ElevatedButton(
                    onPressed: _showFullscreen,
                    child: const Text('Show Fullscreen'),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
