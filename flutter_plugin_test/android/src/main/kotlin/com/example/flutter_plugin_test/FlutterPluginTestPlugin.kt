package com.example.flutter_plugin_test

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.runtimeaware.sdk.ExistingSdk
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.logging.Level
import java.util.logging.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** FlutterPluginTestPlugin */
class FlutterPluginTestPlugin :
    FlutterPlugin,
    MethodCallHandler,
    ActivityAware {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var runtimeAwareSdk: ExistingSdk
    private lateinit var context: Context
    private var activity: Activity? = null
    private var coroutineScope: CoroutineScope? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Logger.getGlobal().log(Level.SEVERE, "onAttachedToEngine")
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.example.flutter_plugin_test/flutter_plugin_test")
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
        runtimeAwareSdk = ExistingSdk(context)
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {
        val scope = coroutineScope
        if (scope == null) {
            result.error("NO_ACTIVITY", "Plugin is not attached to an activity.", null)
            return
        }

        when (call.method) {
            "initializeRuntimeAwareSdk" -> {
                scope.launch {
                    val inited = runtimeAwareSdk.initialize()
                    Logger.getGlobal().log(Level.SEVERE, "Inited: $inited")
                    result.success("Inited: $inited")
                }
            }
            "createFile" -> {
                val sizeInMb = call.argument<Int>("sizeInMb")
                if (sizeInMb != null) {
                    scope.launch {
                        val success = runtimeAwareSdk.createFile(sizeInMb)
                        result.success(success)
                    }
                } else {
                    result.error("INVALID_ARGUMENT", "sizeInMb is required", null)
                }
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Logger.getGlobal().log(Level.SEVERE, "onDetachedFromEngine")
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Logger.getGlobal().log(Level.SEVERE, "onAttachedToActivity")
        activity = binding.activity
        coroutineScope = (activity as? androidx.lifecycle.LifecycleOwner)?.lifecycleScope
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Logger.getGlobal().log(Level.SEVERE, "onDetachedFromActivityForConfigChanges")
        activity = null
        coroutineScope = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Logger.getGlobal().log(Level.SEVERE, "onReattachedToActivityForConfigChanges")
        activity = binding.activity
        coroutineScope = (activity as? androidx.lifecycle.LifecycleOwner)?.lifecycleScope
    }

    override fun onDetachedFromActivity() {
        Logger.getGlobal().log(Level.SEVERE, "onDetachedFromActivity")
        activity = null
        coroutineScope = null
    }
}
