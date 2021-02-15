package com.android.camera.data.observeable;

import com.android.camera.fragment.vv.VVList;
import com.android.camera.module.impl.component.LiveSubVVImpl;
import com.android.camera.resource.SimpleNativeResourceInfoListFillRequest;
import io.reactivex.Observable;
import java.util.Locale;

public class VlogViewModel extends VMBase {
    private VVList mVVList;

    public /* synthetic */ VVList O00000o0(VVList vVList) {
        this.mVVList = vVList;
        return this.mVVList;
    }

    /* access modifiers changed from: protected */
    public boolean achieveEndOfCycle() {
        return false;
    }

    public VVList getVVList() {
        String country = Locale.getDefault().getCountry();
        VVList vVList = this.mVVList;
        if (vVList == null || !vVList.country.equals(country)) {
            return null;
        }
        return this.mVVList;
    }

    public Observable getVVListObservable() {
        return new SimpleNativeResourceInfoListFillRequest("vv/info.json", LiveSubVVImpl.TEMPLATE_PATH).startObservable(VVList.class).map(new C0137O00000oo(this));
    }

    /* access modifiers changed from: protected */
    public void rollbackData() {
        this.mVVList = null;
    }
}
