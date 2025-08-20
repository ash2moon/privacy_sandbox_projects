package com.example.flutter_plugin_test

import android.app.Activity
import android.content.Intent
import com.runtimeaware.sdk.ExistingSdk
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger

/** FlutterPluginTestPlugin */
class FlutterPluginTestPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null
    private lateinit var runtimeAwareSdk: ExistingSdk
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Logger.getGlobal().log(Level.SEVERE, "initializing runtimeawaresdk"
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "com.example.privacy_client/runtime_aware")
        channel.setMethodCallHandler(this)
        runtimeAwareSdk = ExistingSdk(flutterPluginBinding.applicationContext)
        Logger.getGlobal().log(Level.SEVERE, "runtimeAwareSdk initialized $runtimeAwareSdk")
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
//            "initializeRuntimeAwareSdk" -> {
                coroutineScope.launch {
                    val inited = runtimeAwareSdk.initialize()
                    Logger.getGlobal().log(Level.SEVERE, "Inited: $inited")
                    result.success("Inited: $inited")
                }
            }
            "createFile" -> {
                val sizeInMb = call.argument<Int>("sizeInMb")
                if (sizeInMb != null) {
                    coroutineScope.launch {
                        val success = runtimeAwareSdk.createFile(sizeInMb)
                        result.success(success)
                    }
                } else {
                    result.error("INVALID_ARGUMENT", "sizeInMb is required", null)
                }
            }
            "requestBanner" -> {
                val intent = Intent(activity, AdActivity::class.java).apply {
                    putExtra("action", "requestBanner")
                    putExtra("loadWebView", call.argument<Boolean>("loadWebView"))
                    putExtra("mediationType", call.argument<String>("mediationType"))
                    putExtra("shouldStartActivity", call.argument<Boolean>("shouldStartActivity"))
                }
                activity?.startActivity(intent)
                result.success(null)
            }
            "showFullscreen" -> {
                val intent = Intent(activity, AdActivity::class.java).apply {
                    putExtra("action", "showFullscreen")
                    putExtra("mediationType", call.argument<String>("mediationType"))
                    putExtra("shouldStartActivity", call.argument<Boolean>("shouldStartActivity"))
                }
                activity?.startActivity(intent)
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }
}
