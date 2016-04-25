package com.ougnt.period_manager;

import com.ougnt.period_manager.event.OnAdsRequestReturnEventListener;

import java.util.Hashtable;
import java.util.UUID;

/**
 * * # Created by wacharint on 4/23/16.
 */
public abstract class IAdsManager {

    public static final String AdsUrl = "AdsUrl";
    public static final String AdsText = "AdsText";
    public static final String AdsLanguage = "AdsLanguage";

    public IAdsManager(UUID deviceId) {

        this.deviceId = deviceId;
    }

    public abstract void requestAds(OnAdsRequestReturnEventListener listener, String adsLanguage);
    public abstract void submitAndResetAdsClick();
    public abstract void addCounter();
    public abstract boolean shouldDisplayAds();

    public abstract float calculateAdsRatio();

    public UUID deviceId;
    public AdsInfo adsInfo;
}
