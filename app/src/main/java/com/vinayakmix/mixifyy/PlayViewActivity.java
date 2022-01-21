package com.vinayakmix.mixifyy;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


public class PlayViewActivity extends AppCompatActivity {

    private WebView viewPlay;
    private String playlink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_view);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                playlink = null;
            } else {
                playlink = extras.getString("playlink");
            }
        } else {
            playlink = (String) savedInstanceState.getSerializable("playlink");
        }
        initView();
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void initView() {
        if (playlink.contains("vimeo")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        viewPlay = findViewById(R.id.viewPlay);
        AppConst.mixifyyBannerAds(PlayViewActivity.this, R.id.viewBannerAds);

        CookieManager.getInstance().setAcceptCookie(true);
        viewPlay.getSettings().setJavaScriptEnabled(true);
        viewPlay.getSettings().setUseWideViewPort(true);
        viewPlay.getSettings().setLoadWithOverviewMode(true);
        viewPlay.getSettings().setDomStorageEnabled(true);
        viewPlay.getSettings().setPluginState(WebSettings.PluginState.ON);
        viewPlay.setWebChromeClient(new WebChromeClient());
        viewPlay.setVisibility(View.VISIBLE);

        viewPlay.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        viewPlay.loadUrl(playlink);

    }


    @Override
    public void onResume() {
        super.onResume();
        viewPlay.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPlay.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (viewPlay.canGoBack()) {
                    viewPlay.goBack();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPlay.loadUrl("about:blank");
        AppConst.mixifyyInterstitialAds(PlayViewActivity.this);
    }

}