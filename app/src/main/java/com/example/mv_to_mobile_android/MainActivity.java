package com.example.mv_to_mobile_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    public interface RequestPermissionCallback {
        public void onGranted();
        public void onDenied();
    }

    private RequestPermissionCallback requestPermissionCallback;
    private final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideNavigationBar();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        ScriptHandler scriptHandler = new ScriptHandler();
        Handler handler = new Handler();
        scriptHandler.setActivity(MainActivity.this);
        scriptHandler.setHandler(handler);
        scriptHandler.setWebView(webView);
        webView.addJavascriptInterface(scriptHandler, "MVZxAndroidHandlers");

        final WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/htmlSource/", new WebViewAssetLoader.AssetsPathHandler(this))
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .addPathHandler("/res/", new WebViewAssetLoader.ResourcesPathHandler(this))
                .build();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });

        String path = "https://appassets.androidplatform.net/htmlSource/index.html";
        this.webView.loadUrl(path);

        Advertisement.initAd();
        Advertisement.loadRewardedAd(null);
        Advertisement.loadInterstitialAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
    }

    private void hideNavigationBar() {
        // Hide both the navigation bar and the status bar.
        View decoView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decoView.setSystemUiVisibility(uiOptions);
    }

    public void requestWriteExternalStoragePermission(RequestPermissionCallback callback) {
        if (Build.VERSION.SDK_INT < 23) {
            callback.onGranted();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionCallback = callback;
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE);
            return;
        }
        callback.onGranted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (requestPermissionCallback != null) {
                        requestPermissionCallback.onGranted();
                        return;
                    }
                }
                break;
            default:
                break;
        }
        if (requestPermissionCallback != null) {
            requestPermissionCallback.onDenied();
        }
    }
}