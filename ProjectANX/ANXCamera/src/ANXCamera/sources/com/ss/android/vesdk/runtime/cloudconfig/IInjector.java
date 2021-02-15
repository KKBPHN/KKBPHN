package com.ss.android.vesdk.runtime.cloudconfig;

import androidx.annotation.NonNull;
import java.util.Map;
import org.json.JSONObject;

public interface IInjector {
    void inject(Map map, @NonNull VECloudConfig vECloudConfig);

    Map parse(@NonNull JSONObject jSONObject);
}
