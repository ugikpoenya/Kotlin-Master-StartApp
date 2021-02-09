package com.example.startapp

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.startapp.sdk.ads.nativead.NativeAdDetails


fun populateStratAppNative(nativeAd: NativeAdDetails, adView: View) {
    val title =adView.findViewById<TextView>(R.id.title)
    val description =adView.findViewById<TextView>(R.id.description)

    val button =adView.findViewById<Button>(R.id.button)
    val icon =adView.findViewById<ImageView>(R.id.icon)

    icon.setImageBitmap(nativeAd.imageBitmap)
    title.text = nativeAd.title
    description.text = nativeAd.description
    button.text = if (nativeAd.isApp) "Install" else "Open"

    button.setOnClickListener {
        adView.performClick()
    }
    nativeAd.registerViewForInteraction(adView)
}
