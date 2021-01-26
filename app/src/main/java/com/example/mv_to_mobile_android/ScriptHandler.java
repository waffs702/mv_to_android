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

    @JavascriptInterface
    public void initAd() {
        Advertisement.initAd();
    }

    @JavascriptInterface
    public void loadRewardedAd(String callbackKey) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                Advertisement.loadRewardedAd(new Advertisement.LoadRewardedCallback() {
                    @Override
                    public void onLoaded() {
                        callbackToJavascript("onLoaded", callbackKey);
                    }

                    @Override
                    public void onFailed() {
                        callbackToJavascript("onFailed", callbackKey);
                    }
                });
            }
        });
    }

    @JavascriptInterface
    public void showRewardedAd(String callbackKey) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                Advertisement.showRewardedAd(activity, new Advertisement.ShowRewardedCallback() {
                    @Override
                    public void onCanceled() {
                        callbackToJavascript("onCanceled", callbackKey);
                    }

                    @Override
                    public void onFailed() {
                        callbackToJavascript("onFailed", callbackKey);
                    }

                    @Override
                    public void onRewarded() {
                        callbackToJavascript("onRewarded", callbackKey);
                    }
                });
            }
        });
    }


}
