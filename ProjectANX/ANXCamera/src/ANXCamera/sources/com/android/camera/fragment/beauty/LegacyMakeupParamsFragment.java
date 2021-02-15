package com.android.camera.fragment.beauty;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.MakeupProtocol;
import com.miui.filtersdk.beauty.BeautyParameterType;

@Deprecated
public class LegacyMakeupParamsFragment extends MakeupParamsFragment {
    /* access modifiers changed from: protected */
    public OnItemClickListener initOnItemClickListener() {
        return new OnItemClickListener() {
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                if (i == 0) {
                    BeautyParameterType beautyParameterType = BeautyParameterType.SHRINK_FACE_RATIO;
                } else if (i == 1 || i != 2) {
                    BeautyParameterType beautyParameterType2 = BeautyParameterType.WHITEN_STRENGTH;
                } else {
                    BeautyParameterType beautyParameterType3 = BeautyParameterType.SMOOTH_STRENGTH;
                }
                MakeupProtocol makeupProtocol = (MakeupProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(180);
                if (makeupProtocol != null) {
                    makeupProtocol.onMakeupItemSelected(null, 0, true);
                }
            }
        };
    }
}
