package com.example.flutter_plugin_test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flutter_plugin_test.databinding.ActivityAdBinding
import com.runtimeaware.sdk.BannerAd
import com.runtimeaware.sdk.FullscreenAd
import kotlinx.coroutines.launch

class AdActivity : AppCompatActivity() {

    private lateinit var bannerAd: BannerAd
    private lateinit var binding: ActivityAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdBinding.inflate(layoutInflater)
        setContentView(binding.root)


        bannerAd = binding.bannerAd

        val action = intent.getStringExtra("action")
        val loadWebView = intent.getBooleanExtra("loadWebView", false)
        val mediationType = intent.getStringExtra("mediationType") ?: "NONE"
        val shouldStartActivity = intent.getBooleanExtra("shouldStartActivity", false)

        if (action == "requestBanner") {
            lifecycleScope.launch {
                bannerAd.loadAd(
                    this@AdActivity,
                    "com.example.privacy_client",
                    { shouldStartActivity },
                    loadWebView,
                    mediationType
                )
            }
        } else if (action == "showFullscreen") {
            lifecycleScope.launch {
                val fullscreenAd = FullscreenAd.create(this@AdActivity, mediationType)
                fullscreenAd.show(this@AdActivity) { shouldStartActivity }
            }
        }
    }
}
