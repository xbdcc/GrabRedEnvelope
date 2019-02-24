package com.carlos.grabredenvelope.activity

import android.os.Bundle
import android.webkit.WebView
import com.carlos.grabredenvelope.R

/**
 * Created by Carlos on 2019/2/23.
 */
class GithubIssuesActivity : WebViewActivity() {

    private val address = "https://github.com/xbdcc/GrabRedEnvelope/issues"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_issues)

        back()

        title = "Github意见反馈"


        val webView = findViewById<WebView>(R.id.webview)
        initSetting(webView)
        initWebView(webView)
        webView.loadUrl(address)
    }
}