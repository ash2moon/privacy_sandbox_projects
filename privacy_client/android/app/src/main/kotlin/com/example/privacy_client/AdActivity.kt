package com.example.privacy_client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.runtimeaware.sdk.BannerAd
import com.runtimeaware.sdk.FullscreenAd
import kotlinx.coroutines.launch

class AdActivity : AppCompatActivity() {

    private lateinit var bannerAd: BannerAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)

        bannerAd = findViewById(R.id.banner_ad)

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
