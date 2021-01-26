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
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Advertisement {
    private static RewardedAd mRewardedAd;
    private static Boolean isRewarded = false;
    private static Boolean isLoading = false;

    public interface LoadRewardedCallback {
        public void onLoaded();
        public void onFailed();
    }

    public interface ShowRewardedCallback {
        public void onCanceled();
        public void onFailed();
        public void onRewarded();
    }

    public static void initAd() {
        Context context = MyApplication.getInstance();
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static void loadRewardedAd(LoadRewardedCallback loadRewardedCallback) {
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
                Log.d("loadRewardedAd",  "onAdFailedToLoad" + loadAdError.getMessage());
                mRewardedAd = null;
                if (loadRewardedCallback != null) {
                    loadRewardedCallback.onFailed();
                }
            }
        };

        Context context = MyApplication.getInstance();
        String unitId = context.getString(R.string.ad_rewarded_unit_id);

        RewardedAd.load(context, unitId, adRequest, adLoadCallback);
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
}
