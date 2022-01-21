package com.vinayakmix.mixifyy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig fcmConfig;
    ScheduledExecutorService executorService;
    int second = 0;
    String attrValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();

    }

    public void initView() {
        fcmConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1)
                .build();
        fcmConfig.setConfigSettingsAsync(configSettings);
        fcmConfig.reset();
        fcmConfig.fetchAndActivate()
                .addOnCanceledListener(() -> {
                    try {
                        FirebaseAnalytics.getInstance(SplashActivity.this)
                                .logEvent(AppConst.fcmCancel, new Bundle());
                        checkConnection();
                    } catch (Exception e) {
                        checkConnection();
                    }
                })
                .addOnFailureListener(this, task -> {
                    try {
                        FirebaseAnalytics.getInstance(SplashActivity.this)
                                .logEvent(AppConst.fcmFailure, new Bundle());

                        checkConnection();
                    } catch (Exception e) {
                        checkConnection();
                    }
                })
                .addOnCompleteListener(this, task -> {
                    try {
                        FirebaseAnalytics.getInstance(SplashActivity.this)
                                .logEvent(AppConst.fcmComplete, new Bundle());
                        if (!fcmConfig.getString(AppConst.fcmConfig)
                                .equalsIgnoreCase("")) {
                            AppConst.ShowAds = false;
                            if (fcmConfig.getString(AppConst.fcmConfig)
                                    .startsWith("http")) {
                                AppConst.setMixifyyEndPoint(SplashActivity.this,
                                        fcmConfig.getString(AppConst.fcmConfig));
                            } else {
                                AppConst.setMixifyyEndPoint(SplashActivity.this,
                                        "https://" + fcmConfig.getString(AppConst.fcmConfig));
                            }
                            AppConst.enablePremium = false;
                            checkConnection();
                        } else {
                            FirebaseAnalytics.getInstance(SplashActivity.this)
                                    .logEvent(AppConst.fcmEmpty, new Bundle());
                            checkConnection();
                        }
                    } catch (Exception e) {
                        FirebaseAnalytics.getInstance(SplashActivity.this)
                                .logEvent(AppConst.fcmException, new Bundle());

                        checkConnection();
                    }
                });

        setFCMService();
    }

    private void setFCMService() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String action = b.getString(AppConst.actionId);
            String deeplink = b.getString(AppConst.deepLink);
            if (action != null && action.equalsIgnoreCase("1")) {
                Intent intentDeep = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
                intentDeep.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intentDeep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentDeep.setPackage("com.android.chrome");
                try {
                    startActivity(intentDeep);
                } catch (Exception ex) {
                    intentDeep = new Intent(Intent.ACTION_VIEW);
                    intentDeep.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentDeep.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentDeep.setData(Uri.parse(deeplink));
                    startActivity(intentDeep);
                }
            }
        }
    }

    public void gotoNextView() {
        if (AppConst.enablePremium) {
            Intent intent = new Intent(SplashActivity.this,
                    HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            executorService = Executors.newScheduledThreadPool(5);
            executorService.scheduleAtFixedRate(() -> {
                second = second + 1;
                attrValue = AppConst.getMixifyyReceivedAttribution(SplashActivity.this);
                if (attrValue != null && !attrValue.isEmpty()) {
                    try {
                        executorService.shutdown();
                    } catch (Exception ignored) {

                    }
                    FirebaseAnalytics.getInstance(SplashActivity.this)
                            .logEvent(AppConst.fcmCompleteOpenB, new Bundle());
                    startActivity(new Intent(SplashActivity.this,
                            PremiumFeatureActivity.class));
                    finish();
                } else if (second >= 6) {
                    try {
                        executorService.shutdown();
                    } catch (Exception ignored) {

                    }
                    FirebaseAnalytics.getInstance(SplashActivity.this)
                            .logEvent(AppConst.fcmCompleteOpenB, new Bundle());
                    startActivity(new Intent(SplashActivity.this,
                            PremiumFeatureActivity.class));
                    finish();
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        }
    }

    private void checkConnection() {
        if (!AppConst.isNetworkAvailable(SplashActivity.this)) {
            dialogConnection();
        } else {
            gotoNextView();
        }
    }

    public void dialogConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.dialogTheme);
        builder.setTitle(R.string.alertTitle);
        builder.setMessage(R.string.no_internet_connection);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_try_again, (dialog, which) -> {
            dialog.dismiss();
            dialogDismiss();
        });
        builder.show();
    }

    private void dialogDismiss() {
        new Handler(Looper.getMainLooper()).postDelayed(this::checkConnection, 150);
    }

}