package com.android.camera.fragment.beauty;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.CameraSettings;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class FrontFragmentBusiness extends AbBeautyFragmentBusiness {
    List mFragments;

    public List getCurrentShowFragmentList() {
        Object obj;
        List list;
        List list2 = this.mFragments;
        if (list2 == null) {
            this.mFragments = new ArrayList();
        } else {
            list2.clear();
        }
        if (C0124O00000oO.Oo00O()) {
            list = this.mFragments;
            obj = new BeautyLevelFragment();
        } else {
            this.mFragments.add(new BeautyLevelFragment());
            list = this.mFragments;
            obj = new MakeupParamsFragment();
        }
        list.add(obj);
        if (C0122O00000o.instance().OOo00o() && CameraSettings.isSupportBeautyMakeup()) {
            this.mFragments.add(new MakeupBeautyFragment());
        }
        return this.mFragments;
    }
}
