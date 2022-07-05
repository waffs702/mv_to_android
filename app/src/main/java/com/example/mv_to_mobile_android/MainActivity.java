package com.example.mv_to_mobile_android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.webkit.WebViewAssetLoader;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    private ConstraintLayout mainLayout;
    private AdView bannerView;
    private boolean isLoadedBanner;

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

        mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);

        hideNavigationBar();
        createBannerView();
        createWebView();
        adjustWebView();

        Advertisement.initAd();
        Advertisement.loadRewardedAd(null);
        Advertisement.loadInterstitialAd();

        if (!getResources().getBoolean(R.bool.handle_ad_banner_display)) {
            loadBanner();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideNavigationBar();
        if (bannerView != null) {
            bannerView.resume();
        }
    }

    @Override
    protected void onPause() {
        if (bannerView != null) {
            bannerView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (bannerView != null) {
            bannerView.destroy();
        }
        super.onDestroy();
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

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float density = metrics.density;
        float adWidthPixels = mainLayout.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = metrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void createBannerView() {
        String bannerUnitId = getString(R.string.ad_banner_unit_id);
        if (bannerUnitId.isEmpty()) {
            return;
        }
        if (bannerView != null) {
            mainLayout.removeView(bannerView);
        }

        bannerView = new AdView(this);
        bannerView.setAdUnitId(getString(R.string.ad_banner_unit_id));
        bannerView.setId(View.generateViewId());

        mainLayout.addView(bannerView);

        AdSize adSize = getAdSize();
        bannerView.setAdSize(adSize);
//        bannerView.setBackgroundColor(Color.BLACK);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)bannerView.getLayoutParams();
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        String bannerPosition = getString(R.string.ad_banner_position);
        if ("bottom".equals(bannerPosition)) {
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        }
        bannerView.setLayoutParams(layoutParams);
    }

    public void loadBanner() {
        if (bannerView == null || isLoadedBanner) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerView.loadAd(adRequest);
        isLoadedBanner = true;
    }

    private void createWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.setWebContentsDebuggingEnabled(true);

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
    }

    private void adjustWebView() {
        if (bannerView == null) {
            return;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)webView.getLayoutParams();

        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.height = 0;
        layoutParams.verticalWeight = 1;

        String bannerPosition = getString(R.string.ad_banner_position);
        if ("bottom".equals(bannerPosition)) {
            layoutParams.bottomToTop = bannerView.getId();
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            layoutParams.topToBottom = bannerView.getId();
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        }

        webView.setLayoutParams(layoutParams);
    }
}