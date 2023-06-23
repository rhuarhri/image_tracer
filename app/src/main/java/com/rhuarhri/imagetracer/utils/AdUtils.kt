package com.rhuarhri.imagetracer.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdUtils {

    private val testFullScreenAdId = "ca-app-pub-3940256099942544/1033173712"
    private val fullScreenAdId = "ca-app-pub-6053844014996876/1939038404"

    private val testBannerAdId = "ca-app-pub-3940256099942544/6300978111"
    private val bannerAdId = "ca-app-pub-6053844014996876/5871115379"

    fun showFullScreenAd(
        context : Context,
        onDismiss : () -> Unit,
        onError : () -> Unit
    ) {

        fun Context.findActivity(): Activity {
            var context = this
            while (context is ContextWrapper) {
                if (context is Activity) return context
                context = context.baseContext
            }
            throw IllegalStateException("no activity")
        }

        val activity = context.findActivity()

        InterstitialAd.load(
            context,
            fullScreenAdId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    /*
                    Information on ad error codes
                    https://support.google.com/admob/thread/3494603/admob-error-codes-logs?hl=en
                     */

                    if(!NetworkUtils.checkConnection(context)) {
                        //The problem is that the user is not connected to the internet
                        onError.invoke()
                    } else {
                        //The problem was related to something else. So the user is allowed to
                        // keep using the app.
                        onDismiss.invoke()
                    }

                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdDismissedFullScreenContent() {
                            onDismiss.invoke()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            if(!NetworkUtils.checkConnection(context)) {
                                //The problem is that the user is not connected to the internet
                                onError.invoke()
                            } else {
                                //The problem was related to something else. So the user is allowed to
                                // keep using the app.
                                onDismiss.invoke()
                            }
                        }

                        override fun onAdShowedFullScreenContent() {}
                    }
                    interstitialAd.show(activity)
                }
            }
        )
    }

    @Composable
    fun BannerAd() {
        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    this.setAdSize(AdSize.BANNER)
                    this.adUnitId = bannerAdId
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}