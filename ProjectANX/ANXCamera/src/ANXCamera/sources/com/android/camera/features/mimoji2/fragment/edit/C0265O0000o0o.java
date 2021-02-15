package com.android.camera.features.mimoji2.fragment.edit;

import android.view.View;
import com.android.camera.features.mimoji2.bean.MimojiEmoticonInfo;
import com.android.camera.features.mimoji2.widget.baseview.OnRecyclerItemClickListener;

/* compiled from: lambda */
/* renamed from: com.android.camera.features.mimoji2.fragment.edit.O0000o0o reason: case insensitive filesystem */
public final /* synthetic */ class C0265O0000o0o implements OnRecyclerItemClickListener {
    private final /* synthetic */ FragmentMimojiEmoticon O0OOoO0;

    public /* synthetic */ C0265O0000o0o(FragmentMimojiEmoticon fragmentMimojiEmoticon) {
        this.O0OOoO0 = fragmentMimojiEmoticon;
    }

    public final void OnRecyclerItemClickListener(Object obj, int i, View view) {
        this.O0OOoO0.O000000o((MimojiEmoticonInfo) obj, i, view);
    }
}
