package com.android.camera.fragment.beauty;

import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import java.util.List;

public interface IBeautySettingBusiness extends IBeautyBusiness {
    void clearBeauty();

    int getDefaultProgressByCurrentItem();

    int getDisplayNameRes();

    int getProgressByCurrentItem();

    List getSupportedTypeArray(@ShineType String str);

    void onStateChanged();

    void resetBeauty();

    void setCurrentType(String str);

    void setDisplayNameRes(int i);

    void setProgressForCurrentItem(int i);

    void updateExtraTable();
}
