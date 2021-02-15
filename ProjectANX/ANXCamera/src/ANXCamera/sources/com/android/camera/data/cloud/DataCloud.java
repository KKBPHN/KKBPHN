package com.android.camera.data.cloud;

import android.content.SharedPreferences.Editor;
import com.android.camera.data.data.config.SupportedConfigs;
import java.util.Collection;

public interface DataCloud {

    public interface CloudFeature {
        Editor editor();

        SupportedConfigs filterFeature(SupportedConfigs supportedConfigs);

        Collection getAllDisabledFeatures();

        String provideKey();

        void setReady(boolean z);
    }

    public interface CloudItem {
        Editor editor();

        boolean getCloudBoolean(String str, boolean z);

        float getCloudFloat(String str, float f);

        int getCloudInt(String str, int i);

        long getCloudLong(String str, long j);

        String getCloudString(String str, String str2);

        String provideKey();

        void setReady(boolean z);
    }

    public interface CloudManager {
        CloudFeature DataCloudFeature();

        void fillCloudValues();

        CloudItem provideDataCloudConfig(int i);

        CloudItem provideDataCloudGlobal();
    }
}
