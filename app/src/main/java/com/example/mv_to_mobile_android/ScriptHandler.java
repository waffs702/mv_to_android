package com.example.mv_to_mobile_android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class ScriptHandler {
    private static final String TAG = "ScriptHandler";
    private Handler handler;
    private MainActivity activity;
    private WebView webView;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    private void callbackToJavascript(String args, String key) {
        Log.v("callbackJS", args);
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
    public void showBannerAd(String callbackKey) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                activity.loadBanner();
            }
        });
    }

    @JavascriptInterface
    public void shareSNS(String shareText, String imageData) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity != null) {
                    MainActivity mainActivity = (MainActivity)activity;
                    mainActivity.requestWriteExternalStoragePermission(new MainActivity.RequestPermissionCallback() {
                        @Override
                        public void onGranted() {
                            showShareSheet(shareText, imageData);
                        }

                        @Override
                        public void onDenied() {
                            showShareSheet(shareText, "");
                        }
                    });
                    return;
                }
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
        } else {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TITLE, shareText);
        }
        this.activity.startActivity(Intent.createChooser(shareIntent, null));
    }

    private Uri getImageUri(Bitmap inImage){
        Context context = MyApplication.getInstance();

        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String title = sdf.format(new Date());

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, title,null);
        return Uri.parse(path);
    }

    @JavascriptInterface
    public void callBrowser(String url) {
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                callBrowserIntent(url);
            }
        });
    }

    private void callBrowserIntent(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        this.activity.startActivity(intent);
    }

}
