package com.android.camera.multi;

import androidx.annotation.NonNull;
import com.android.camera.network.resource.RequestContracts.Download;
import com.android.camera.resource.BaseResourceItem;
import com.android.camera.resource.SimpleNetworkJsonRequest;
import com.google.android.apps.photos.api.PhotosOemApi;
import org.json.JSONObject;

public class PluginInfoRequest extends SimpleNetworkJsonRequest {
    public BaseResourceItem resourceItem;

    public PluginInfoRequest(String str) {
        super(Download.URL);
        addParam("id", str);
    }

    public PluginInfoRequest(String str, BaseResourceItem baseResourceItem) {
        super(Download.URL);
        addParam("id", str);
        this.resourceItem = baseResourceItem;
    }

    /* access modifiers changed from: protected */
    public PluginInfo parseJson(JSONObject jSONObject, @NonNull PluginInfo pluginInfo) {
        JSONObject optJSONObject = jSONObject.optJSONObject(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
        pluginInfo.sha1Base16 = optJSONObject.optString(Download.JSON_KEY_SHA1);
        pluginInfo.url = optJSONObject.optString("url");
        pluginInfo.resourceItem = this.resourceItem;
        return pluginInfo;
    }
}
