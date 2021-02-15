package com.android.camera.fragment.beauty;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.android.camera.R;
import com.android.camera.data.data.TypeItem;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsWrapper;
import java.util.HashMap;
import java.util.List;

public class RemodelingParamsFragment extends MakeupParamsFragment {
    private static final String TAG = "RemodelingParamsFragment";

    public /* synthetic */ void O000000o(AdapterView adapterView, View view, int i, long j) {
        String str = ((TypeItem) this.mItemList.get(i)).mKeyOrType;
        MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
        if (makeupProtocol != null) {
            makeupProtocol.onMakeupItemSelected(str, ((TypeItem) this.mItemList.get(i)).mDisplayNameRes, true);
            CameraStatUtils.trackBeautyClick(getShineType(), str);
        }
    }

    /* access modifiers changed from: protected */
    public String getShineType() {
        return "4";
    }

    /* access modifiers changed from: protected */
    public void initExtraType() {
        this.mAlphaElement = 1;
        this.mBetaElement = -1;
        List list = this.mItemList;
        if (list != null && !list.isEmpty()) {
            if ("pref_beautify_skin_smooth_ratio_key".equals(((TypeItem) this.mItemList.get(0)).mKeyOrType)) {
                this.mBetaElement = 2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public OnItemClickListener initOnItemClickListener() {
        return new O00000o0(this);
    }

    /* access modifiers changed from: protected */
    public void onClearClick() {
        Log.u(TAG, "onClearClick");
        super.onClearClick();
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, BeautyAttr.VALUE_BEAUTY_FACE_CLEAR);
        MistatsWrapper.mistatEvent(BeautyAttr.KEY_BEAUTY_FACE, hashMap);
        toast(getResources().getString(R.string.beauty_clear_toast));
    }

    /* access modifiers changed from: protected */
    public void onResetClick() {
        super.onResetClick();
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, BeautyAttr.VALUE_BEAUTY_FACE_RESET);
        MistatsWrapper.mistatEvent(BeautyAttr.KEY_BEAUTY_FACE, hashMap);
    }
}
