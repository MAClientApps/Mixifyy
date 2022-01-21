package com.vinayakmix.mixifyy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


public class PremiumFeatureActivity extends AppCompatActivity {

    private WebView wvMixifyPremium;
    LinearLayout layoutError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_feature);
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupView() {
        wvMixifyPremium = findViewById(R.id.wvMixifyPremium);
        layoutError = findViewById(R.id.layoutError);
        CookieManager.getInstance().setAcceptCookie(true);
        wvMixifyPremium.getSettings().setJavaScriptEnabled(true);
        wvMixifyPremium.getSettings().setUseWideViewPort(true);
        wvMixifyPremium.getSettings().setLoadWithOverviewMode(true);
        wvMixifyPremium.getSettings().setDomStorageEnabled(true);
        wvMixifyPremium.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvMixifyPremium.setWebChromeClient(new WebChromeClient());
        wvMixifyPremium.setVisibility(View.VISIBLE);

        wvMixifyPremium.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                String url = request.getUrl().toString();
                if (url.startsWith("http")) {
                    startActivity(new Intent(PremiumFeatureActivity.this,
                            HomeActivity.class));
                    finish();
                } else {
                    if (!url.startsWith(getString(R.string.app_scheme))) {
                        startActivity(new Intent(PremiumFeatureActivity.this,
                                HomeActivity.class));
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        loadView();
    }

    public void checkConnection() {
        layoutError.setVisibility(View.VISIBLE);
        Button btnRetry = findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(view -> {
            layoutError.setVisibility(View.GONE);
            loadView();
        });
    }

    protected void loadView() {
        if (AppConst.isNetworkAvailable(this)) {
            wvMixifyPremium.loadUrl(AppConst.generateMixifyyPremiumLink(PremiumFeatureActivity.this));
        } else {
            checkConnection();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        wvMixifyPremium.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wvMixifyPremium.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wvMixifyPremium.loadUrl("about:blank");
    }

    @Override
    public void onBackPressed() {
    }
}
