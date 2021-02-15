package com.android.camera.fragment.beauty;

import java.util.ArrayList;
import java.util.List;

public class LiveBeautyFragmentBusiness implements IBeautyFragmentBusiness {
    List mFragments;

    public List getCurrentShowFragmentList() {
        List list = this.mFragments;
        if (list == null) {
            this.mFragments = new ArrayList();
        } else {
            list.clear();
        }
        this.mFragments.add(new LiveBeautyFilterFragment());
        this.mFragments.add(new LiveBeautyModeFragment());
        return this.mFragments;
    }

    public Object operate(Object obj) {
        return null;
    }
}
