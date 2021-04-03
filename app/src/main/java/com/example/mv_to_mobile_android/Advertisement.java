package com.example.mv_to_mobile_android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Advertisement {
    private static RewardedAd mRewardedAd;
    private static Boolean isRewarded = false;
    private static Boolean isLoading = false;

    private static InterstitialAd mInterstitialAd;
    private static Boolean isInterstitialLoading = false;
    private static Boolean isSucceededInterstitialAd = false;

    public interface LoadRewardedCallback {
        public void onLoaded();
        public void onFailed();
    }

    public interface ShowRewardedCallback {
        public void onCanceled();
        public void onFailed();
        public void onRewarded();
    }

    public interface ShowInterstitialCallback {
        public void onSucceeded();
        public void onFailed();
    }

    public static void initAd() {
        Context context = MyApplication.getInstance();
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    //=============================================================================
    // Reward Ad
    //=============================================================================
    public static void loadRewardedAd(LoadRewardedCallback loadRewardedCallback) {
        Context context = MyApplication.getInstance();
        String unitId = context.getString(R.string.ad_rewarded_unit_id);

        if ("".equals(unitId)) {
            return;
        }

        isLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                isLoading = false;
                mRewardedAd = rewardedAd;
                Log.d("loadRewardedAd", "onAdLoaded");
                if (loadRewardedCallback != null) {
                    loadRewardedCallback.onLoaded();
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isLoading = false;
                Log.d("loadRewardedAd",  "onAdFailedToLoad : " + loadAdError.getMessage());
                mRewardedAd = null;
                if (loadRewardedCallback != null) {
                    loadRewardedCallback.onFailed();
                }
            }
        };

        RewardedAd.load(context, unitId, adRequest, adLoadCallback);
        Log.d("loadRewardedAd", "Request Load");
    }

    public static void showRewardedAd(Activity activity, ShowRewardedCallback showRewardedCallback) {
        isRewarded = false;
        if (mRewardedAd == null) {
            Log.d("showRewardedAd",  "The rewarded ad wasn't ready yet.");
            if (!isLoading) {
                loadRewardedAd(null);
            }
            showRewardedCallback.onFailed();
            return;
        }

        // fullscreen callback
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                Log.d("showRewardedAd", "onAdFailedToShowFullScreenContent");
                mRewardedAd = null;
                showRewardedCallback.onFailed();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d("showRewardedAd", "onAdShowedFullScreenContent");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d("showRewardedAd", "onAdDismissedFullScreenContent");
                loadRewardedAd(null);
                if (isRewarded) {
                    showRewardedCallback.onRewarded();
                } else {
                    showRewardedCallback.onCanceled();
                }
            }
        });

        mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                int rewardAmount = rewardItem.getAmount();
                String rewardType = rewardItem.getType();

                Log.d("showRewardedAd", "The user earned the reward. rewardAmount:" + rewardAmount + " rewardType:" + rewardType);
                isRewarded = true;
            }
        });
    }

    //=============================================================================
    // Interstitial Ad
    //=============================================================================
    public static void loadInterstitialAd() {
        Context context = MyApplication.getInstance();
        String unitId = context.getString(R.string.ad_interstitial_id);

        if ("".equals(unitId)) {
            return;
        }

        isInterstitialLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAdLoadCallback adLoadCallback = new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                isInterstitialLoading = false;
                mInterstitialAd = interstitialAd;
                Log.d("loadInterstitialAd", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                isInterstitialLoading = false;
                Log.d("loadInterstitialAd", "onAdFailedToLoad : " + loadAdError.getMessage());
                mInterstitialAd = null;
            }
        };

        InterstitialAd.load(context, unitId, adRequest, adLoadCallback);
        Log.d("loadInterstitialAd", "Request Load");
    }

    public static void showInterstitialAd(Activity activity, ShowInterstitialCallback showInterstitialCallback) {
        isSucceededInterstitialAd = false;
        if (mInterstitialAd == null) {
            Log.d("showInterstitialAd",  "The interstitial ad wasn't ready yet.");
            if (!isInterstitialLoading) {
                loadInterstitialAd();
            }
            // callback
            showInterstitialCallback.onFailed();
            return;
        }

        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d("loadInterstitialAd", "onAdDismissedFullScreenContent");
                mInterstitialAd = null;
                loadInterstitialAd();
                if (isSucceededInterstitialAd) {
                    showInterstitialCallback.onSucceeded();
                } else {
                    showInterstitialCallback.onFailed();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                Log.d("loadInterstitialAd", "onAdFailedToShowFullScreenContent :" + adError.getMessage());
                mInterstitialAd = null;
                showInterstitialCallback.onFailed();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d("loadInterstitialAd", "onAdShowedFullScreenContent");
                isSucceededInterstitialAd = true;
                mInterstitialAd = null;
            }
        });

        mInterstitialAd.show(activity);
    }
}
