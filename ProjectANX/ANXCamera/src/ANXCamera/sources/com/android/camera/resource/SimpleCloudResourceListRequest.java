package com.android.camera.resource;

import androidx.annotation.NonNull;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.network.resource.RequestContracts.Info;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SimpleCloudResourceListRequest extends SimpleNetworkJsonRequest {
    public SimpleCloudResourceListRequest(String str) {
        super(Info.URL);
        addParam("id", str);
    }

    /* access modifiers changed from: protected */
    public List parseJson(JSONObject jSONObject, @NonNull List list) {
        JSONArray optJSONArray = jSONObject.optJSONObject(PhotosOemApi.PATH_SPECIAL_TYPE_DATA).optJSONArray(Info.JSON_KEY_ITEMS);
        int length = optJSONArray.length();
        for (int i = 0; i < length; i++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
            BaseResourceCloudItem baseResourceCloudItem = new BaseResourceCloudItem();
            baseResourceCloudItem.keyOrID = optJSONObject.optString(WatermarkConstant.ITEM_KEY);
            baseResourceCloudItem.size = optJSONObject.optLong("size");
            baseResourceCloudItem.iconUrl = optJSONObject.optString("icon");
            baseResourceCloudItem.requestDownloadId = optJSONObject.optLong("id");
            list.add(baseResourceCloudItem);
        }
        return list;
    }
}
