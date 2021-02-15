package com.android.camera.fragment.vv;

import android.text.TextUtils;
import com.android.camera.resource.BaseResourceCloudItem;
import com.android.camera.resource.BaseResourceRaw;
import com.google.android.apps.photos.api.PhotosOemApi;
import io.reactivex.functions.BiFunction;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VVListBiFunction implements BiFunction {
    public String outputPath;

    public VVListBiFunction(String str) {
        this.outputPath = str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0099 A[SYNTHETIC, Splitter:B:33:0x0099] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public VVList apply(List list, BaseResourceRaw baseResourceRaw) {
        VVList vVList = new VVList();
        BufferedOutputStream bufferedOutputStream = null;
        try {
            JSONObject jSONObject = new JSONObject(baseResourceRaw.content);
            vVList.parseInitialData(jSONObject);
            JSONArray itemJsonArray = vVList.getItemJsonArray(jSONObject);
            for (int i = 0; i < itemJsonArray.length(); i++) {
                JSONObject optJSONObject = itemJsonArray.optJSONObject(i);
                if (!TextUtils.isEmpty(optJSONObject.optString("placeholder"))) {
                    String optString = optJSONObject.optString("id");
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        BaseResourceCloudItem baseResourceCloudItem = (BaseResourceCloudItem) it.next();
                        if (optString.equals(baseResourceCloudItem.keyOrID)) {
                            optJSONObject.put("uri", String.valueOf(baseResourceCloudItem.requestDownloadId));
                            optJSONObject.put("iconUrl", baseResourceCloudItem.iconUrl);
                            break;
                        }
                    }
                }
            }
            jSONObject.put(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, itemJsonArray);
            String jSONObject2 = jSONObject.toString();
            int length = jSONObject2.getBytes().length;
            BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(this.outputPath));
            try {
                bufferedOutputStream2.write(jSONObject2.getBytes(), 0, length);
                try {
                    bufferedOutputStream2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return vVList;
            } catch (Exception e2) {
                e = e2;
                bufferedOutputStream = bufferedOutputStream2;
                try {
                    e.printStackTrace();
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedOutputStream = bufferedOutputStream2;
                if (bufferedOutputStream != null) {
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            throw e;
        }
    }
}
