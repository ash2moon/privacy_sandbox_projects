package com.example.privacy_client

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.runtimeaware.sdk.ExistingSdk
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.launch
import java.util.logging.Level
import java.util.logging.Logger

class MainActivity : FlutterActivity() {
    private val runtimeAwareSdk = ExistingSdk(this)
    private val CHANNEL = "com.example.privacy_client/runtime_aware"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "initializeRuntimeAwareSdk" -> {
                    lifecycleScope.launch {
                        val inited = runtimeAwareSdk.initialize()
                        Logger.getGlobal().log(Level.SEVERE, "Inited: $inited")
                        result.success("Inited: $inited")
                    }
                }
                "createFile" -> {
                    val sizeInMb = call.argument<Int>("sizeInMb")
                    if (sizeInMb != null) {
                        lifecycleScope.launch {
                            val success = runtimeAwareSdk.createFile(sizeInMb)
                            result.success(success)
                        }
                    } else {
                        result.error("INVALID_ARGUMENT", "sizeInMb is required", null)
                    }
                }
                "requestBanner" -> {
                    val intent = Intent(this, AdActivity::class.java).apply {
                        putExtra("action", "requestBanner")
                        putExtra("loadWebView", call.argument<Boolean>("loadWebView"))
                        putExtra("mediationType", call.argument<String>("mediationType"))
                        putExtra("shouldStartActivity", call.argument<Boolean>("shouldStartActivity"))
                    }
                    startActivity(intent)
                    result.success(null)
                }
                "showFullscreen" -> {
                    val intent = Intent(this, AdActivity::class.java).apply {
                        putExtra("action", "showFullscreen")
                        putExtra("mediationType", call.argument<String>("mediationType"))
                        putExtra("shouldStartActivity", call.argument<Boolean>("shouldStartActivity"))
                    }
                    startActivity(intent)
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }
    }
}
