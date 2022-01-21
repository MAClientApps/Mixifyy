package com.vinayakmix.mixifyy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentData {

    String Title, Thumbnail, Content, Description;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public String getContent() {
        return Content;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setContent(String content) {
        Content = content;
    }

    public static ArrayList<ContentData> getVideoGameList(JSONArray jsonArray) {

        ArrayList<ContentData> contentDataArrayList = new ArrayList<>();
        try {

            if (jsonArray != null && jsonArray.length() > 0) {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);

                    contentDataArrayList.add(getDataFromJson(jObj));

                }
            }
            return contentDataArrayList;
        } catch (Exception e) {
            return contentDataArrayList;
        }
    }

    private static ContentData getDataFromJson(JSONObject jsonObject) {
        ContentData contentData = new ContentData();
        try {
            if (jsonObject.has("Title") && !jsonObject.isNull("Title"))
                contentData.setTitle(jsonObject.getString("Title"));

            if (jsonObject.has("Thumbnail_Large") && !jsonObject.isNull("Thumbnail_Large"))
                contentData.setThumbnail(jsonObject.getString("Thumbnail_Large"));

            if (jsonObject.has("Content") && !jsonObject.isNull("Content"))
                contentData.setContent(jsonObject.getString("Content"));

            if (jsonObject.has("Description") && !jsonObject.isNull("Description"))
                contentData.setDescription(jsonObject.getString("Description"));

            return contentData;
        } catch (JSONException e) {
            return contentData;
        }
    }
}
