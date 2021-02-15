package com.android.camera.fragment.beauty;

import java.util.ArrayList;
import java.util.List;

public class BackMainFragmentBusiness extends AbBeautyFragmentBusiness {
    List mFragments;

    public List getCurrentShowFragmentList() {
        List list = this.mFragments;
        if (list == null) {
            this.mFragments = new ArrayList();
        } else {
            list.clear();
        }
        this.mFragments.add(new BeautyLevelFragment());
        this.mFragments.add(new BeautyBodyFragment());
        return this.mFragments;
    }
}
