package com.android.camera.fragment.vv;

import com.android.camera.data.DataRepository;
import com.android.camera.data.data.extra.DataItemLive;
import com.android.camera.resource.BaseResourceList;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class VVList extends BaseResourceList {
    public static final int TYPE = 1;
    public String country;

    public JSONArray getItemJsonArray(JSONObject jSONObject) {
        return jSONObject.optJSONArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
    }

    public String getLocalVersion() {
        return DataRepository.dataItemLive().getString(DataItemLive.DATA_VV_VERSION, "");
    }

    public int getResourceType() {
        return 1;
    }

    public void parseInitialData(JSONObject jSONObject) {
        this.version = jSONObject.optString("version");
        this.country = Locale.getDefault().getCountry();
    }

    public VVItem parseSingleItem(JSONObject jSONObject, int i) {
        VVItem vVItem = new VVItem();
        vVItem.parseSummaryData(jSONObject, i);
        if (!vVItem.isValid) {
            return null;
        }
        return vVItem;
    }

    public void setLocalVersion(String str) {
        this.departed = false;
        DataRepository.dataItemLive().editor().putString(DataItemLive.DATA_VV_VERSION, str).apply();
    }

    public boolean stateAllReady() {
        for (VVItem vVItem : getResourceList()) {
            if (vVItem.isCloudItem() && vVItem.getCurrentState() != 5) {
                return false;
            }
        }
        return true;
    }
}
