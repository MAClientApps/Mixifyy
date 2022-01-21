package com.vinayakmix.mixifyy;

import static android.content.Context.MODE_PRIVATE;
import static com.adjust.sdk.Util.md5;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Base64;

import com.appodeal.ads.Appodeal;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class AppConst {

    public static String AdsId = "918299ecfd895742e64a7830e6e13a4e527e998a044eaf70";
    public static int AdsCounter = 0;
    public static Boolean ShowAds = true;
    public static Boolean enablePremium = true;

    public static final String adjustToken = "qxood4mjz56o";
    public static final String adjustEventToken = "864q2x";
    public static final String fcmInstanceId = "jdp1bm";

    public static final String PrefName = "Mixifyy";
    public static final String userUUID = "user_uuid";
    public static final String eventValue = "eventValue";
    public static final String configValue = "config_value";
    public static final String actionId = "action_id";
    public static final String deepLink = "deeplink";
    public static final String fcmConfig = "mixifyy";
    public static final String adjustAttribute = "adjust_attribute";
    public static final String fcmCompleteOpenB = "remote_config_complete_open_b";
    public static final String fcmComplete = "remote_config_complete";
    public static final String fcmFailure = "remote_config_failure";
    public static final String fcmCancel = "remote_config_cancel";
    public static final String fcmEmpty = "remote_config_complete_empty";
    public static final String fcmException = "remote_config_complete_exception";


    public static void setMixifyyUserUUID(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PrefName,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(userUUID, value);
            editor.apply();
        }
    }

    public static String getMixifyyUserUUID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PrefName,
                MODE_PRIVATE);
        return preferences.getString(userUUID, "");
    }

    public static void setMixifyyEndPoint(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PrefName,
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(configValue, value);
            editor.apply();
        }
    }

    public static String getMixifyyEndPoint(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PrefName,
                MODE_PRIVATE);
        return preferences.getString(configValue, "");
    }

    public static void setMixifyyReceivedAttribution(Context context, String value) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(PrefName, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(adjustAttribute, value);
            editor.apply();
        }
    }

    public static String getMixifyyReceivedAttribution(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PrefName, MODE_PRIVATE);
        return preferences.getString(adjustAttribute, "");
    }

    public static String generateMixifyyUserUUID(Context context) {
        String md5uuid = getMixifyyUserUUID(context);
        if (md5uuid == null || md5uuid.isEmpty()) {
            String guid = "";
            final String uniqueID = UUID.randomUUID().toString();
            Date date = new Date();
            long timeMilli = date.getTime();
            guid = uniqueID + timeMilli;
            md5uuid = md5(guid);
            setMixifyyUserUUID(context, md5uuid);
        }
        return md5uuid;
    }

    public static String generateMixifyyPremiumLink(Context context) {
        String mixifyyPremiumLink = "";
        try {
            String strPackUrl = context.getPackageName() + "-" +
                    generateMixifyyUserUUID(context);
            String base64 = Base64.encodeToString(strPackUrl.getBytes(StandardCharsets.UTF_8),
                    Base64.DEFAULT);
            mixifyyPremiumLink = getMixifyyEndPoint(context) + "?" + base64 + ";2;";
            String attribute = URLEncoder.encode(getMixifyyReceivedAttribution(context), "utf-8");
            mixifyyPremiumLink = mixifyyPremiumLink + attribute;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mixifyyPremiumLink;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null && cm
                .getActiveNetworkInfo().isConnectedOrConnecting();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static JSONObject mixifyyReadJsonDataFile(final Context context, final String filename) {
        JSONObject json = null;
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            final String jsonStrings = new String(buffer, StandardCharsets.UTF_8);
            json = new JSONObject(jsonStrings);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    public static void mixifyyInitializeAds(Activity activity) {
        Appodeal.initialize(activity, AdsId,
                Appodeal.REWARDED_VIDEO | Appodeal.BANNER);
    }

    public static void mixifyyBannerAds(Activity activity, int layoutBannerAds) {
        try {
            if (ShowAds && isNetworkAvailable(activity)) {
                Appodeal.setBannerViewId(layoutBannerAds);
                Appodeal.show(activity, Appodeal.BANNER_VIEW);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mixifyyInterstitialAds(Activity activity) {
        try {
            if (ShowAds && isNetworkAvailable(activity)) {
                AdsCounter++;
                if (AdsCounter == 3) {
                    if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                        Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
                    }
                    AdsCounter = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mixifyyRewardedAds(Activity activity) {
        try {
            if (ShowAds && isNetworkAvailable(activity)) {
                if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
                    Appodeal.show(activity, Appodeal.REWARDED_VIDEO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
