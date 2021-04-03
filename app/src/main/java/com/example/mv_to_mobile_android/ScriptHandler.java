package com.example.mv_to_mobile_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;

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

    @JavascriptInterface
    public void showInterstitialAd(String callbackKey) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                Advertisement.showInterstitialAd(activity, new Advertisement.ShowInterstitialCallback() {
                    @Override
                    public void onSucceeded() {
                        callbackToJavascript("onSucceeded", callbackKey);
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
    public void shareSNS(String shareText, String imageData) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                showShareSheet(shareText, imageData);
            }
        });
    }

    private void showShareSheet(String shareText, String imageData) {
        Uri imageUri = null;
        if (imageData != null && !"".equals(imageData)) {
            byte[] bytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageUri = getImageUri(bitmap);
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/jpeg");
        }
        this.activity.startActivity(Intent.createChooser(shareIntent, null));
    }

    private Uri getImageUri(Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        Context context = MyApplication.getInstance();
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage,"title",null);
        return Uri.parse(path);
    }

}
