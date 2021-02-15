package com.android.camera.fragment.beauty;

import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import com.android.camera.data.data.runing.TypeElementsBeauty;
import java.util.HashMap;
import java.util.Map;

public class BeautySettingManager {
    private static final String TAG = "BeautySettingManager";
    private HashMap mBeautySettingBusinessArray = new HashMap();
    @ShineType
    private String mBeautyType;

    private IBeautySettingBusiness updateBeautySettingBusiness(@ShineType String str, TypeElementsBeauty typeElementsBeauty, Map map) {
        if (map.get(str) != null) {
            return (IBeautySettingBusiness) map.get(str);
        }
        char c = 65535;
        if (str.hashCode() == 1571 && str.equals("14")) {
            c = 0;
        }
        IBeautySettingBusiness beautySettingBusiness = c != 0 ? new BeautySettingBusiness(str, typeElementsBeauty) : new VideoBokehSettingBusiness(str, typeElementsBeauty);
        map.put(this.mBeautyType, beautySettingBusiness);
        return beautySettingBusiness;
    }

    public IBeautySettingBusiness constructAndGetSetting(@ShineType String str, TypeElementsBeauty typeElementsBeauty) {
        this.mBeautyType = str;
        IBeautySettingBusiness updateBeautySettingBusiness = updateBeautySettingBusiness(str, typeElementsBeauty, this.mBeautySettingBusinessArray);
        updateBeautySettingBusiness.updateExtraTable();
        return updateBeautySettingBusiness;
    }
}
