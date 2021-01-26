package com.example.mv_to_mobile_android;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.os.Handler;

public class ScriptHandler {
    private static final String TAG = "ScriptHandler";
    private Handler handler;
    private Activity activity;
    private WebView webView;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    private void callbackToJavascript(String args, String key) {
        this.webView.loadUrl("javascript:window.MVZxNativeManager.nativeCallback('" + args + "', '" + key + "');");
    }

    @JavascriptInterface
    public void debugLog(String log) {
        Log.v(TAG, log);
    }


}
