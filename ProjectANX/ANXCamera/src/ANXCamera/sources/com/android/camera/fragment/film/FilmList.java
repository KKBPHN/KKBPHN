package com.android.camera.fragment.film;

import com.android.camera.resource.BaseResourceList;
import com.google.android.apps.photos.api.PhotosOemApi;
import org.json.JSONArray;
import org.json.JSONObject;

public class FilmList extends BaseResourceList {
    public static final int TYPE = 3;

    public JSONArray getItemJsonArray(JSONObject jSONObject) {
        return jSONObject.optJSONArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
    }

    public String getLocalVersion() {
        return "";
    }

    public int getResourceType() {
        return 3;
    }

    public void parseInitialData(JSONObject jSONObject) {
    }

    public FilmItem parseSingleItem(JSONObject jSONObject, int i) {
        FilmItem filmItem = new FilmItem();
        filmItem.parseSummaryData(jSONObject, i);
        return filmItem;
    }

    public void setLocalVersion(String str) {
    }
}
