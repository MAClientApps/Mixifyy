package com.vinayakmix.mixifyy;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MixifyyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }

        AdjustConfig config = new AdjustConfig(this, AppConst.adjustToken,
                AdjustConfig.ENVIRONMENT_PRODUCTION);
        Adjust.addSessionCallbackParameter(AppConst.userUUID,
                AppConst.generateMixifyyUserUUID(getApplicationContext()));
        try {
            FirebaseAnalytics.getInstance(this)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> {
                        AdjustEvent adjustEvent = new AdjustEvent(AppConst.fcmInstanceId);
                        adjustEvent.addCallbackParameter(AppConst.eventValue,
                                task.getResult());
                        adjustEvent.addCallbackParameter(AppConst.userUUID,
                                AppConst.generateMixifyyUserUUID(getApplicationContext()));
                        Adjust.trackEvent(adjustEvent);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        config.setOnAttributionChangedListener(attribution ->
                AppConst.setMixifyyReceivedAttribution(getApplicationContext(),
                        attribution.toString()));
        Adjust.onCreate(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    private static final class AdjustLifecycleCallbacks
            implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity,
                                      @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity,
                                                @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }
}
