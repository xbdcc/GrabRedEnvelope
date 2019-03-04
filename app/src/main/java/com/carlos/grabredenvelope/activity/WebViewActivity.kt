package com.carlos.grabredenvelope.activity

import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Created by Carlos on 2019/2/23.
 */
open class WebViewActivity : BaseActivity() {

    fun initSetting(webView: WebView) {
        val webSettings = webView.settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);//是否可以缩放，默认false
        webSettings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        webSettings.setUseWideViewPort(true);//大视图模式
        webSettings.setAppCacheEnabled(true);//是否使用缓存
        webSettings.setDomStorageEnabled(true);

    }

    fun initWebView(webView: WebView) {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http")) {
                    webView.loadUrl(url)
                }
                return true
            }
        }
    }

}