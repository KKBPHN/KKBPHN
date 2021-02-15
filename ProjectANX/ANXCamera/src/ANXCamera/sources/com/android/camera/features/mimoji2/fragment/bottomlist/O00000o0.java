package com.android.camera.features.mimoji2.fragment.bottomlist;

import android.view.View;
import com.android.camera.features.mimoji2.bean.MimojiBgInfo;
import com.android.camera.features.mimoji2.widget.baseview.OnRecyclerItemClickListener;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements OnRecyclerItemClickListener {
    private final /* synthetic */ FragmentMimojiBottomList O0OOoO0;

    public /* synthetic */ O00000o0(FragmentMimojiBottomList fragmentMimojiBottomList) {
        this.O0OOoO0 = fragmentMimojiBottomList;
    }

    public final void OnRecyclerItemClickListener(Object obj, int i, View view) {
        this.O0OOoO0.O000000o((MimojiBgInfo) obj, i, view);
    }
}
