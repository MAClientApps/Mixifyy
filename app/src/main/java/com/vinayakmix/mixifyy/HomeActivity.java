package com.vinayakmix.mixifyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;

public class HomeActivity extends AppCompatActivity implements RewardedVideoCallbacks{

    Button btnVideoList, btnGameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        btnVideoList = findViewById(R.id.btnVideoContent);
        btnVideoList.setOnClickListener(v -> {
            if (AppConst.isNetworkAvailable(HomeActivity.this)) {
                AppConst.mixifyyInterstitialAds(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this,
                        ContentListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this,
                        getString(R.string.no_internet_connection) + "",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnGameList = findViewById(R.id.btnGameContent);
        btnGameList.setOnClickListener(v -> {
            AppConst.mixifyyInterstitialAds(HomeActivity.this);
            if (AppConst.isNetworkAvailable(HomeActivity.this)) {
                Intent intent = new Intent(HomeActivity.this,
                        ContentListActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this,
                        getString(R.string.no_internet_connection) + "",
                        Toast.LENGTH_LONG).show();
            }
        });

        try {
            if (AppConst.ShowAds && AppConst.isNetworkAvailable(HomeActivity.this)) {
                AppConst.mixifyyInitializeAds(HomeActivity.this);
                AppConst.mixifyyBannerAds(HomeActivity.this,
                        R.id.viewBannerAds);
                Appodeal.setRewardedVideoCallbacks(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRewardedVideoLoaded(boolean isPrecache) {
        AppConst.mixifyyRewardedAds(HomeActivity.this);
    }

    @Override
    public void onRewardedVideoFailedToLoad() {

    }

    @Override
    public void onRewardedVideoShown() {

    }

    @Override
    public void onRewardedVideoShowFailed() {

    }

    @Override
    public void onRewardedVideoFinished(double amount, String name) {

    }

    @Override
    public void onRewardedVideoClosed(boolean finished) {

    }

    @Override
    public void onRewardedVideoExpired() {

    }

    @Override
    public void onRewardedVideoClicked() {

    }
}