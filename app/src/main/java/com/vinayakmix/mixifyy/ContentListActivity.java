package com.vinayakmix.mixifyy;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentListActivity extends AppCompatActivity {

    TextView txtTitle;
    RecyclerView rvContentList;
    ContentListAdapter contentListAdapter;
    ProgressDialog progressDialog;
    ArrayList<ContentData> contentDataArrayList = new ArrayList<>();

    int type = 0;
    String typeVideo = "mix_videos.json";
    String typeGame = "mix_games.json";
    Toolbar toolBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_content_list);

        if (getIntent().hasExtra("type")) {
            type = getIntent().getExtras().getInt("type");
        }
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        toolBar.setTitle("");

        txtTitle = toolBar.findViewById(R.id.txtTitle);
        if (type == 1) {
            txtTitle.setText("Videos");
        } else {
            txtTitle.setText("Games");
        }

        AppConst.mixifyyBannerAds(ContentListActivity.this, R.id.viewBannerAds);
        rvContentList = findViewById(R.id.rvContentList);
        rvContentList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvContentList.setHasFixedSize(true);


        if (AppConst.isNetworkAvailable(ContentListActivity.this)) {
            new GetVideoGameData().execute();
        } else {
            Toast.makeText(ContentListActivity.this,
                    getString(R.string.no_internet_connection) + "",
                    Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    public class GetVideoGameData extends AsyncTask<String, Integer, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = new ProgressDialog(ContentListActivity.this);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                if (type == 1) {
                    return AppConst.mixifyyReadJsonDataFile(ContentListActivity.this,
                            typeVideo);
                } else {
                    return AppConst.mixifyyReadJsonDataFile(ContentListActivity.this,
                            typeGame);
                }
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(JSONObject jsonData) {
            super.onPostExecute(jsonData);

            try {
                try {
                    if (jsonData != null && jsonData.has("Array") &&
                            !jsonData.isNull("Array")) {
                        final JSONArray jsonArrayAction = jsonData.getJSONArray("Array");
                        contentDataArrayList.addAll(ContentData.getVideoGameList(jsonArrayAction));
                    }

                    contentListAdapter = new ContentListAdapter(ContentListActivity.this,
                            contentDataArrayList, type);
                    rvContentList.setAdapter(contentListAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}