package com.example.startapp

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.startapp.databinding.ActivityMainBinding
import com.startapp.sdk.ads.banner.Banner
import com.startapp.sdk.ads.banner.BannerListener
import com.startapp.sdk.ads.nativead.NativeAdPreferences
import com.startapp.sdk.ads.nativead.StartAppNativeAd
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK
import com.startapp.sdk.adsbase.adlisteners.AdEventListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStartAppAds()

        binding.btnBanner.setOnClickListener {
            initStartAppBanner()
        }

        binding.btnRewardedVideo.setOnClickListener {
            showRewardedVideo()
        }

        binding.btnNative.setOnClickListener {
            initStratAppNative()
        }

        binding.btnInterstitial.setOnClickListener {
            binding.txtLog.append("\n Interstitial StartApp Show")
            StartAppAd.showAd(this)
        }
    }

    fun initStartAppAds(){
        binding.txtLog.append("\n StartApp Ads Initialized")
        StartAppSDK.init(this, "0")
    }

    fun initStartAppBanner(){
        binding.txtLog.append("\n StartApp Banner Init")
        binding.lyBannerAds.removeAllViews()
        val banner = Banner(this)
        banner.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val bannerParameters = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL)
        binding.lyBannerAds.addView(banner, bannerParameters)
        banner.visibility= GONE
        banner.setBannerListener(object : BannerListener {
            override fun onClick(p0: View?) {
                binding.txtLog.append("\n StartApp Banner onClick")
            }

            override fun onFailedToReceiveAd(p0: View?) {
                binding.txtLog.append("\n StartApp Banner onFailedToReceiveAd")
            }

            override fun onImpression(p0: View?) {
                binding.txtLog.append("\n StartApp Banner onImpression")
            }

            override fun onReceiveAd(p0: View?) {
                binding.txtLog.append("\n StartApp Banner onReceiveAd")
                banner.visibility = View.VISIBLE
            }
        })
    }

    fun showRewardedVideo() {
        binding.txtLog.append("\n RewardedVideo StartApp Show")
        val rewardedVideo = StartAppAd(this)
        rewardedVideo.setVideoListener {
            binding.txtLog.append("\n StartApp Grant the reward to user")
        }
        rewardedVideo.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                rewardedVideo.showAd()
            }

            override fun onFailedToReceiveAd(ad: Ad) {
                binding.txtLog.append("\n StartAppCan't show rewarded video")
            }
        })
    }

    fun initStratAppNative(){
        binding.txtLog.append("\n initStratAppNative")
        binding.lyBannerAds.removeAllViews()
        val mAdView = layoutInflater.inflate( R.layout.native_ads_layout_startapp,binding.lyBannerAds,false)
        val startAppNativeAd = StartAppNativeAd(this)
        startAppNativeAd.loadAd(
            NativeAdPreferences().setAdsNumber(1)
                .setAutoBitmapDownload(true)
                .setPrimaryImageSize(2),
            object : AdEventListener {
                override fun onFailedToReceiveAd(p0: Ad?) {
                    binding.txtLog.append("\n StratApp Native onFailedToReceiveAd "+p0?.errorMessage)
                }
                override fun onReceiveAd(p0: Ad?) {
                    binding.txtLog.append("\n StratApp Native onReceiveAd")
                    val ads = startAppNativeAd.nativeAds // get NativeAds list
                    ads.forEach {
                        populateStratAppNative(it,mAdView)
                        binding.lyBannerAds.addView(mAdView)
                    }
                }
            })

    }

}